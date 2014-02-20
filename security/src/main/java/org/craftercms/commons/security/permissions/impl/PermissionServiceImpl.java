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
package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.*;

/**
 * Default implementation of {@link org.craftercms.commons.security.permissions.PermissionService}
 *
 * @author avasquez
 */
public class PermissionServiceImpl implements PermissionService {

    protected SubjectResolver subjectResolver;
    protected PermissionResolver permissionResolver;
    protected ParentResolver parentResolver;

    public PermissionServiceImpl() {
        permissionResolver = new SecuredObjectPermissionResolver();
    }

    public void setPermissionResolver(PermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    public void setParentResolver(ParentResolver parentResolver) {
        this.parentResolver = parentResolver;
    }

    @Override
    public boolean allow(Object object, String action) throws PermissionException {
        if (subjectResolver == null) {
            throw new PermissionException("No SubjectResolver found. Unable to infer current subject");
        }

        return allow(subjectResolver.getCurrentSubject(), object, action);
    }

    @Override
    public boolean allow(Object subject, Object object, String action) throws PermissionException {
        Iterable<Permission> permissions = permissionResolver.getPermissions(object);

        if (permissions != null) {
            for (Permission permission : permissions) {
                Boolean permitted = checkPermission(permission, subject, action);
                if (permitted != null) {
                    return permitted;
                } else if (parentResolver != null) {
                    Object parent = parentResolver.getParent(object);
                    if (parent != null) {
                        allow(subject, object, action);
                    }
                }
            }
        }

        return false;
    }

    protected Boolean checkPermission(Permission permission, Object subject, String action) {
        if (permission.appliesTo(subject)) {
            return permission.isActionAllowed(action);
        } else {
            return null;
        }
    }

}
