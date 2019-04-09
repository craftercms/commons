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
package org.craftercms.commons.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.List;

/**
 * {@code BeanFactoryPostProcessor} that allows to add additional elements to a list property value of an already
 * defined bean. Useful to add additional locations to a {@code PropertySourcesPlaceholderConfigurer}, for example.
 *
 * @author avasquez
 */
public class ListMergingPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    private int order = Ordered.LOWEST_PRECEDENCE;

    private String beanName;
    private String propertyName;
    private List<String> additionalElements;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setAdditionalElements(List<String> additionalElements) {
        this.additionalElements = additionalElements;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        assertRequiredProperty("beanName", beanName);
        assertRequiredProperty("propertyName", propertyName);
        assertRequiredProperty("additionalElements", additionalElements);

        if (registry.containsBeanDefinition(beanName)) {
            BeanDefinition bean = registry.getBeanDefinition(this.beanName);
            MutablePropertyValues values = bean.getPropertyValues();

            PropertyValue propertyValue = values.getPropertyValue(propertyName);
            if (propertyValue == null) {
                values.add(propertyName, additionalElements);
            } else {
                Object value = propertyValue.getValue();
                if (value == null) {
                    values.add(propertyName, additionalElements);
                } else if (value instanceof List) {
                    List<TypedStringValue> mergedList = new ManagedList<>();
                    mergedList.addAll((List) value);

                    for (String element : additionalElements) {
                        mergedList.add(new TypedStringValue(element));
                    }

                    values.add(propertyName, mergedList);
                } else {
                    throw new IllegalStateException("Property '" + propertyName + "' of bean '" + beanName +
                                                    "' expected to be of type " + List.class.getName() +
                                                    ", but instead is of type " + value.getClass().getName());
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op but required by interface class hierarchy
    }

    private void assertRequiredProperty(String name, Object value) {
        if (value == null) {
            throw new IllegalStateException("Required property '" + name + "' not specified");
        }
    }

}
