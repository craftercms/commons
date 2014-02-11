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
import org.craftercms.commons.security.permissions.PermissionSource;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link org.craftercms.commons.security.permissions.PermissionSource} that uses a
 * pre-existing list.
 *
 * @author avasquez
 */
public class SimplePermissionSource implements PermissionSource {

    protected List<Permission> permissions;

    @Required
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Iterable<Permission> getPermissions(String resourceUri) {
        List<Permission> matchingPermissions = new ArrayList<Permission>();

        for (Permission permission : permissions) {
            if (permission.getResourceUri().equals(resourceUri)) {
                matchingPermissions.add(permission);
            }
        }

        return matchingPermissions;
    }

}
