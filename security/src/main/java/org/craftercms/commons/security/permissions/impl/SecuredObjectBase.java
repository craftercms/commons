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

import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.SecuredObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for {@link org.craftercms.commons.security.permissions.SecuredObject} that provides already the
 * permissions.
 *
 * @author avasquez
 */
public class SecuredObjectBase implements SecuredObject {

    protected List<Permission> permissions;

    @Override
    public Iterable<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }

        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

}
