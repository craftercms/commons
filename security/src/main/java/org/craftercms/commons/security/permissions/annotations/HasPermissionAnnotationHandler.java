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
package org.craftercms.commons.security.permissions.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.craftercms.commons.aop.AopUtils;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.security.exception.ActionDeniedException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.PermissionEvaluator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.annotation.Order;

/**
 * Aspect that handles {@link org.craftercms.commons.security.permissions.annotations.HasPermission} annotations,
 * by doing appropriate permission checking.
 *
 * @author avasquez
 */
@Aspect
@Order(-1)
public class HasPermissionAnnotationHandler {

    private static final I10nLogger logger = new I10nLogger(HasPermissionAnnotationHandler.class,
                                                            "crafter.security.messages.logging");

    private static final String LOG_KEY_METHOD_INT = "security.permission.methodIntercepted";
    private static final String LOG_KEY_METHOD_INT_NO_SEC_OBJ = "security.permission.methodInterceptedNoSecObject";

    private static final String ERROR_KEY_EVALUATOR_NOT_FOUND = "security.permission.evaluatorNotFound";
    private static final String ERROR_KEY_EVALUATION_FAILED = "security.permission.evaluationFailed";

    protected Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators;

    @Required
    public void setPermissionEvaluators(Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators) {
        this.permissionEvaluators = permissionEvaluators;
    }

    @SuppressWarnings("unchecked") //cortiz, OK permissionEvaluator.isAllowed
    @Around("@within(org.craftercms.commons.security.permissions.annotations.HasPermission) || " +
            "@annotation(org.craftercms.commons.security.permissions.annotations.HasPermission)")
    public Object checkPermissions(ProceedingJoinPoint pjp) throws Throwable {
        boolean allowed;
        Method method = AopUtils.getActualMethod(pjp);
        HasPermission hasPermission = getHasPermissionAnnotation(method, pjp);
        Class<?> type = hasPermission.type();
        String action = hasPermission.action();
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(type);

        Object securedResource = getAnnotatedProtectedResource(method, pjp);
        if (securedResource == null) {
            securedResource = getAnnotatedProtectedResourceIds(method, pjp);
        }

        if (securedResource != null) {
            logger.debug(LOG_KEY_METHOD_INT, method, hasPermission, securedResource);
        } else {
            logger.debug(LOG_KEY_METHOD_INT_NO_SEC_OBJ, method, hasPermission);
        }

        if (permissionEvaluator == null) {
            throw new PermissionException(ERROR_KEY_EVALUATOR_NOT_FOUND, type);
        }

        try {
            allowed = permissionEvaluator.isAllowed(securedResource, action);
        } catch (PermissionException e) {
            throw new PermissionException(ERROR_KEY_EVALUATION_FAILED, e);
        }

        if (allowed) {
            return pjp.proceed();
        } else if (securedResource != null) {
            throw new ActionDeniedException(hasPermission.action(), securedResource);
        } else {
            throw new ActionDeniedException(hasPermission.action());
        }
    }

    protected HasPermission getHasPermissionAnnotation(Method method, ProceedingJoinPoint pjp) {
        HasPermission hasPermission = method.getAnnotation(HasPermission.class);

        if (hasPermission == null) {
            Class<?> targetClass = pjp.getTarget().getClass();
            hasPermission = targetClass.getAnnotation(HasPermission.class);
        }

        return hasPermission;
    }

    protected Object getAnnotatedProtectedResource(Method method, ProceedingJoinPoint pjp) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] params = pjp.getArgs();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof ProtectedResource) {
                    return params[i];
                }
            }
        }

        return null;
    }

    protected Map<String, Object> getAnnotatedProtectedResourceIds(Method method, ProceedingJoinPoint pjp) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] params = pjp.getArgs();
        Map<String, Object> resourceIds = null;

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof ProtectedResourceId) {
                    String idName = ((ProtectedResourceId) a).value();

                    if (resourceIds == null) {
                        resourceIds = new HashMap<>();
                    }

                    resourceIds.put(idName, params[i]);
                }
            }
        }

        return resourceIds;
    }

}
