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
import org.craftercms.commons.cal10n.Cal10nUtils;
import org.craftercms.commons.security.exception.ActionDeniedException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.SecurityErrorCode;
import org.craftercms.commons.security.logging.SecurityLogMessage;
import org.craftercms.commons.security.permissions.PermissionEvaluator;
import org.slf4j.cal10n.LocLogger;

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

    private static final LocLogger logger = Cal10nUtils.DEFAULT_LOC_LOGGER_FACTORY.getLocLogger(
            HasPermissionAnnotationHandler.class);

    protected Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators;

    public void setPermissionEvaluators(Map<Class<?>, PermissionEvaluator<?, ?>> permissionEvaluators) {
        this.permissionEvaluators = permissionEvaluators;
    }

    @Around("@annotation(HasPermission) || @target(HasPermission)")
    public Object checkPermissions(ProceedingJoinPoint pjp) throws Throwable {
        boolean allowed;
        Method method = getActualMethod(pjp);
        HasPermission hasPermission = getHasPermissionAnnotation(method, pjp);
        Class<?> type = hasPermission.type();
        String action = hasPermission.action();
        Object securedObject = getAnnotatedSecuredObject(method, pjp);
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(type);

        if (securedObject != null) {
            logger.debug(SecurityLogMessage.PROTECTED_METHOD_INTERCEPTED, method, hasPermission, securedObject);
        } else {
            logger.debug(SecurityLogMessage.PROTECTED_METHOD_INTERCEPTED_NO_SEC_OBJ, method, hasPermission);
        }

        if (permissionEvaluator == null) {
            throw new PermissionException(SecurityErrorCode.PERMISSION_EVALUATOR_NOT_FOUND, type);
        }

        try {
            allowed = permissionEvaluator.isAllowed(securedObject, action);
        } catch (IllegalArgumentException | PermissionException e) {
            throw new PermissionException(SecurityErrorCode.PERMISSION_EVALUATION_FAILED, e);
        }

        if (allowed) {
            return pjp.proceed();
        } else if (securedObject != null) {
            throw new ActionDeniedException(hasPermission.action(), securedObject);
        } else {
            throw new ActionDeniedException(hasPermission.action());
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
                throw new PermissionException(SecurityErrorCode.IMPLEMENTING_METHOD_NOT_FOUND, method, e);
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
