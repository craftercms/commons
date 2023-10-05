/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.PermissionEvaluator;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for Permission Annotation Handlers.
 */
public abstract class AbstractPermissionAnnotationHandler {
    private static final String ERROR_KEY_EVALUATOR_NOT_FOUND = "security.permission.evaluatorNotFound";
    private static final String ERROR_KEY_EVALUATION_FAILED = "security.permission.evaluationFailed";

    protected final Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators;

    public AbstractPermissionAnnotationHandler(Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators) {
        this.permissionEvaluators = permissionEvaluators;
    }

    /**
     * Get a permission annotation of the given type from the given method or its containing class if exists
     *
     * @param method         the method to get the annotation from
     * @param pjp            the join point
     * @param annotationType the desired annotation type
     * @param <T>            the annotation type
     * @return the annotation, or null if none found
     */
    protected <T extends Annotation> T getHasPermissionAnnotation(Method method, ProceedingJoinPoint pjp, Class<T> annotationType) {
        T hasPermission = method.getAnnotation(annotationType);

        if (hasPermission == null) {
            Class<?> targetClass = pjp.getTarget().getClass();
            hasPermission = targetClass.getAnnotation(annotationType);
        }

        return hasPermission;
    }

    /**
     * Gets the protected resource (parameter annotated with {@link ProtectedResource}) from the method parameters.
     *
     * @param method the method to extract protected resource from
     * @param params the parameter values
     * @return the protected resource, or null if none found
     */
    protected Object getAnnotatedProtectedResource(Method method, Object[] params) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof ProtectedResource) {
                    return params[i];
                }
            }
        }

        return null;
    }

    /**
     * Gets the protected resource ids (e.g.: siteId, path) from the method parameters.
     *
     * @param method      the method to extract parameters from
     * @param paramValues the parameter values
     * @return a map with the protected resource ids
     */
    protected Map<String, Object> getAnnotatedProtectedResourceIds(Method method, Object[] paramValues) {
        Parameter[] methodParameters = method.getParameters();
        Map<String, Object> resourceIds = null;

        for (int i = 0; i < methodParameters.length; i++) {
            ProtectedResourceId resourceIdAnnotation = AnnotationUtils.findAnnotation(methodParameters[i], ProtectedResourceId.class);
            if (resourceIdAnnotation != null) {
                String idName = resourceIdAnnotation.value();

                if (resourceIds == null) {
                    resourceIds = new HashMap<>();
                }

                resourceIds.put(idName, paramValues[i]);
            }
        }

        return resourceIds;
    }

    /**
     * Checks the permissions to perform the action configured in the {@link HasPermission} to the securedResource (if any)
     *
     * @param method          the {@link Method} to secure
     * @param hasPermission   the {@link HasPermission} annotation
     * @param securedResource the securedResource, if any
     * @return true if the action is allowed, false otherwise
     */
    @SuppressWarnings("unchecked")
    protected boolean checkPermissions(final Method method, final HasPermission hasPermission, final Object securedResource) {
        Class<?> type = hasPermission.type();
        String action = hasPermission.action();
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(type);

        if (permissionEvaluator == null) {
            throw new PermissionException(ERROR_KEY_EVALUATOR_NOT_FOUND, type);
        }

        try {
            return permissionEvaluator.isAllowed(securedResource, action);
        } catch (PermissionException e) {
            throw new PermissionException(ERROR_KEY_EVALUATION_FAILED, e);
        }
    }
}
