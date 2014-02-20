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
import org.craftercms.commons.security.exception.ActionDeniedException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.RuntimePermissionException;
import org.craftercms.commons.security.permissions.PermissionService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Aspect that handles {@link org.craftercms.commons.security.permissions.annotations.HasPermission} annotations,
 * by doing appropriate permission checking.
 *
 * @author avasquez
 */
@Aspect
public class HasPermissionAnnotationHandler {

    protected Map<String, PermissionService> permissionServices;

    @Required
    public void setPermissionServices(Map<String, PermissionService> permissionServices) {
        this.permissionServices = permissionServices;
    }

    @Around("@annotation(hasPermission)")
    public Object checkPermissions(ProceedingJoinPoint pjp, HasPermission hasPermission) throws Throwable {
        Object[] args = pjp.getArgs();
        Object securedObject = null;

        for (Object arg : args) {
            if (arg.getClass().isAnnotationPresent(SecuredObject.class)) {
                securedObject = arg;
                break;
            }
        }

        if (securedObject == null) {
            securedObject = hasPermission.securedObject();
        }

        PermissionService permissionService = permissionServices.get(hasPermission.type());
        boolean allowed;
        try {
            allowed = permissionService.allow(securedObject, hasPermission.action());
        } catch (PermissionException e) {
            throw new RuntimePermissionException("Permission checking failed", e);
        }

        if (allowed) {
            return pjp.proceed();
        } else {
            throw new ActionDeniedException("Execution of action '" + hasPermission.action() + "' denied");
        }
    }

}
