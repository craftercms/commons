/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.craftercms.commons.security.exception.AuthorizationException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.RuntimePermissionException;
import org.craftercms.commons.security.permissions.PermissionEvaluator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Aspect that handles {@link org.craftercms.commons.security.permissions.annotations.HasPermission} annotations,
 * by doing appropriate permission checking.
 *
 * @author avasquez
 */
@Aspect
public class HasPermissionAnnotationHandler {

    protected Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators;

    public void setPermissionEvaluators(Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators) {
        this.permissionEvaluators = permissionEvaluators;
    }

    @Around("@annotation(HasPermission) || @target(HasPermission)")
    public Object checkPermissions(ProceedingJoinPoint pjp) throws Throwable {
        boolean allowed;
        Method method = getActualMethod(pjp);
        HasPermission hasPermission = getHasPermissionAnnotation(method, pjp);
        Object securedObject = getAnnotatedSecuredObject(method, pjp);
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(hasPermission.type());

        if (permissionEvaluator == null) {
            throw new RuntimePermissionException("No permission evaluator found for " + hasPermission.type());
        }

        try {
            allowed = permissionEvaluator.isAllowed(securedObject, hasPermission.action());
        } catch (IllegalArgumentException | PermissionException e) {
            throw new RuntimePermissionException("Permission checking failed", e);
        }

        if (allowed) {
            return pjp.proceed();
        } else {
            throw new AuthorizationException("Execution of action '" + hasPermission.action() + "' denied");
        }
    }

    protected Method getActualMethod(ProceedingJoinPoint pjp) {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();

        if (method.getDeclaringClass().isInterface()) {
            Class<?> targetClass = pjp.getTarget().getClass();
            try {
                method = targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                // Shouldn't happen, anyway
                throw new RuntimePermissionException("Implementing method not found for " + method, e);
            }
        }

        return method;
    }

    protected HasPermission getHasPermissionAnnotation(Method method, ProceedingJoinPoint pjp) {
        HasPermission hasPermission = method.getAnnotation(HasPermission.class);

        if (hasPermission == null) {
            Class<?> targetClass = pjp.getTarget().getClass();
            hasPermission = targetClass.getAnnotation(HasPermission.class);
        }

        return hasPermission;
    }

    protected Object getAnnotatedSecuredObject(Method method, ProceedingJoinPoint pjp) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] params = pjp.getArgs();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a: paramAnnotations[i]) {
                if (a instanceof SecuredObject) {
                    return params[i];
                }
            }
        }

        return null;
    }

}
