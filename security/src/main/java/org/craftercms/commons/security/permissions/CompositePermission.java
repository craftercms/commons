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

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a permission that is a collection of other permissions. If any of the permissions evaluates to false,
 * the final result will be false.
 *
 * @author avasquez
 */
public class CompositePermission implements Permission {

    protected Collection<Permission> permissions;

    public CompositePermission(Permission... permissions) {
        this(Arrays.asList(permissions));
    }

    public CompositePermission(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean isAllowed(String action) {
        for (Permission permission : permissions) {
            if (!permission.isAllowed(action)) {
                return false;
            }
        }

        return true;
    }

}
