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
package org.craftercms.commons.security.permissions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.craftercms.commons.cal10n.Cal10nUtils;
import org.slf4j.cal10n.LocLogger;

import java.util.HashSet;
import java.util.Set;

/**
 * Base for {@link org.craftercms.commons.security.permissions.Permission} that uses a set of allowed actions to
 * check
 */
public abstract class PermissionBase implements Permission {

    public static final String ANY_ACTION = "*";

    protected Set<String> allowedActions;

    @Override
    public boolean isAllowed(String action) {
        return CollectionUtils.isNotEmpty(allowedActions) &&
               (allowedActions.contains(ANY_ACTION) || allowedActions.contains(action));
    }

    public PermissionBase allowAny() {
        allow(ANY_ACTION);

        return this;
    }

    public PermissionBase allow(String action) {
        if (allowedActions == null) {
            allowedActions = new HashSet<>();
        }

        allowedActions.add(action);

        return this;
    }

    public PermissionBase allow(String... actions) {
        if (ArrayUtils.isNotEmpty(actions)){
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
}
