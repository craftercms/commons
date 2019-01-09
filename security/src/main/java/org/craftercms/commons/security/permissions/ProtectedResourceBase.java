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
package org.craftercms.commons.security.permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for protected resources that provide already attached permissions.
 *
 * @author avasquez
 */
public class ProtectedResourceBase<P extends Permission> {

    protected List<P> permissions;

    public List<P> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }

        return permissions;
    }

    public void setPermissions(List<P> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(P permission) {
        getPermissions().add(permission);
    }

    public void removePermission(P permission) {
        getPermissions().remove(permission);
    }

}
