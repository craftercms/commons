/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.ebus.config;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.craftercms.commons.ebus.annotations.EventHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import reactor.core.Observable;
import reactor.event.Event;
import reactor.event.selector.Selector;
import reactor.function.Consumer;
import reactor.function.Function;
import reactor.spring.factory.config.ConsumerBeanAutoConfiguration;
import reactor.util.StringUtils;

import static reactor.event.selector.Selectors.object;
import static reactor.event.selector.Selectors.regex;

/**
 * {@link org.springframework.context.ApplicationListener} implementation that finds beans registered in the current
 * {@link org.springframework.context.ApplicationContext} that look like a {@link org.craftercms.commons.ebus
 * .annotations.EListener} bean and interrogates it for event handling methods.
 *
 * @author Dejan Brkic
 */
public class EBusBeanAutoConfiguration implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final ReflectionUtils.MethodFilter LISTENER_METHOD_FILTER = new ReflectionUtils.MethodFilter() {
        @Override
        public boolean matches(final Method method) {
            return AnnotationUtils.findAnnotation(method, EventHandler.class) != null;
        }
    };

    private boolean started = false;

    private ApplicationContext applicationContext;
    private ConversionService conversionService;
    private BeanResolver beanResolver;
    private TemplateAwareExpressionParser expressionParser;

    public EBusBeanAutoConfiguration() {
        this.expressionParser = new SpelExpressionParser();
    }

    private static Set<Method> findHandlerMethods(final Class<?> handlerType, final ReflectionUtils.MethodFilter
        listenerMethodFilter) {

        final Set<Method> handlerMethods = new LinkedHashSet<Method>();

        if (handlerType == null) {
            return handlerMethods;
        }

        Set<Class<?>> handlerTypes = new LinkedHashSet<Class<?>>();
        Class<?> specifiedHandlerType = null;
        if (!Proxy.isProxyClass(handlerType)) {
            handlerTypes.add(handlerType);
            specifiedHandlerType = handlerType;
        }
        handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
        for (Class<?> currentHandlerType : handlerTypes) {
            final Class<?> targetClass = (specifiedHandlerType != null? specifiedHandlerType: currentHandlerType);
            ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
                    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                    Method bridgeMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
                    if (listenerMethodFilter.matches(specificMethod) && (bridgeMethod == specificMethod ||
                        !listenerMethodFilter.matches(bridgeMethod))) {
                        handlerMethods.add(specificMethod);
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }

        return handlerMethods;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext ctx = contextRefreshedEvent.getApplicationContext();

        if (applicationContext != ctx) {
            return;
        }

        if (null == beanResolver) {
            beanResolver = new BeanFactoryResolver(ctx);
        }

        if (null == conversionService) {
            try {
                conversionService = ctx.getBean(ConsumerBeanAutoConfiguration.REACTOR_CONVERSION_SERVICE_BEAN_NAME,
                    ConversionService.class);
            } catch (BeansException be) {
                // TODO: log that conversion service is not found.
            }
        }

        synchronized (this) {
            if (started) {
                return;
            }

            Set<Method> methods;
            Class<?> type;
            for (String beanName : ctx.getBeanDefinitionNames()) {
                type = ctx.getType(beanName);
                methods = findHandlerMethods(type, LISTENER_METHOD_FILTER);
                if (methods != null && methods.size() > 0) {
                    wireBean(ctx.getBean(beanName), methods);
                }
            }

            started = true;
        }
    }

    @SuppressWarnings("unchecked") //cortiz, OK Generics are ok.
    private void wireBean(final Object bean, final Set<Method> methods) {
        if (methods == null || methods.isEmpty()) {
            return;
        }

        EventHandler eventHandlerAnnotation;
        Observable reactor;
        Selector selector;
        Consumer consumer;

        for (Method method : methods) {
            // scanAnnotation method
            eventHandlerAnnotation = AnnotationUtils.findAnnotation(method, EventHandler.class);
            reactor = fetchObservable(eventHandlerAnnotation, bean);
            selector = fetchSelector(eventHandlerAnnotation, bean, method);

            // register consumer
            Invoker handler = new Invoker(bean, method, conversionService);
            consumer = new ServiceConsumer(handler);
            reactor.on(selector, consumer);
        }
    }

    @SuppressWarnings("unchecked") //cortiz, OK
    private <T> T expression(String selector, Object bean) {
        if (selector == null) {
            return null;
        }

        StandardEvaluationContext evalCtx = new StandardEvaluationContext();
        evalCtx.setRootObject(bean);
        evalCtx.setBeanResolver(beanResolver);

        return (T)expressionParser.parseExpression(selector).getValue(evalCtx);
    }

    private Observable fetchObservable(final EventHandler eventHandlerAnnotation, final Object bean) {
        return expression(eventHandlerAnnotation.ebus(), bean);
    }

    protected Object parseSelector(EventHandler eventHandlerAnnotation, Object bean, Method method) {
        if (!StringUtils.isEmpty(eventHandlerAnnotation.event())) {
            return eventHandlerAnnotation.event();
        }

        try {
            return expression(eventHandlerAnnotation.event(), bean);
        } catch (Exception e) {
            return eventHandlerAnnotation.event();
        }
    }

    private Selector fetchSelector(final EventHandler eventHandlerAnnotation, final Object bean, final Method method) {
        Object selector = parseSelector(eventHandlerAnnotation, bean, method);

        switch (eventHandlerAnnotation.type()) {
            case OBJECT:
                return object(selector);
            case REGEX:
                return regex(selector.toString());
        }

        return object(selector);
    }

    protected final static class ServiceConsumer implements Consumer<Event> {

        private final Invoker handler;

        public ServiceConsumer(final Invoker handler) {
            this.handler = handler;
        }

        public Invoker getHandler() {
            return handler;
        }

        @Override
        public void accept(final Event event) {
            handler.apply(event);
        }
    }

    protected final static class Invoker implements Function<Event, Object> {

        final private Method method;
        final private Object bean;
        final private Class<?>[] argTypes;
        final private ConversionService conversionService;

        Invoker(Object bean, Method method, ConversionService conversionService) {
            this.bean = bean;
            this.method = method;
            this.argTypes = method.getParameterTypes();
            this.conversionService = conversionService;
        }

        public Method getMethod() {
            return method;
        }

        public Object getBean() {
            return bean;
        }

        public Class<?>[] getArgTypes() {
            return argTypes;
        }

        @Override
        public Object apply(final Event event) {
            if (argTypes == null || argTypes.length < 1) {
                return ReflectionUtils.invokeMethod(method, bean);
            }

            if (argTypes.length > 1) {
                throw new IllegalStateException("Multiple parameters not yet supported.");
            }

            if (Event.class.isAssignableFrom(argTypes[0])) {
                return ReflectionUtils.invokeMethod(method, bean, event);
            }

            if (null == event.getData() || argTypes[0].isAssignableFrom(event.getData().getClass())) {
                return ReflectionUtils.invokeMethod(method, bean, event.getData());
            }

            if (!argTypes[0].isAssignableFrom(event.getClass()) && conversionService.canConvert(event.getClass(),
                argTypes[0])) {
                ReflectionUtils.invokeMethod(method, bean, conversionService.convert(event, argTypes[0]));
            }

            if (conversionService.canConvert(event.getData().getClass(), argTypes[0])) {
                Object convertedObj = conversionService.convert(event.getData(), argTypes[0]);
                return ReflectionUtils.invokeMethod(method, bean, convertedObj);
            }

            throw new IllegalArgumentException("Cannot invoke method " + method + " passing parameter " + event
                .getData());
        }
    }
}
