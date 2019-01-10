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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Default {@link org.craftercms.commons.security.permissions.Permission} that uses a set of allowed actions to
 * check if a subject is allowed to perform a specific action.
 *
 * @author avasquez
 */
public class DefaultPermission implements Permission {

    public static final String ANY_ACTION = "*";

    protected Set<String> allowedActions;

    @Override
    public boolean isAllowed(String action) {
        return CollectionUtils.isNotEmpty(allowedActions) &&
               (allowedActions.contains(ANY_ACTION) || allowedActions.contains(action));
    }

    public Set<String> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(Set<String> allowedActions) {
        this.allowedActions = allowedActions;
    }

    public DefaultPermission allowAny() {
        allow(ANY_ACTION);

        return this;
    }

    public DefaultPermission allow(String action) {
        if (allowedActions == null) {
            allowedActions = new HashSet<>();
        }

        allowedActions.add(action);

        return this;
    }

    public DefaultPermission allow(String... actions) {
        if (ArrayUtils.isNotEmpty(actions)) {
            for (String action : actions) {
                allow(action);
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "allowedActions=" + allowedActions +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultPermission that = (DefaultPermission)o;

        if (allowedActions != null? !allowedActions.equals(that.allowedActions): that.allowedActions != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return allowedActions != null? allowedActions.hashCode(): 0;
    }

}
