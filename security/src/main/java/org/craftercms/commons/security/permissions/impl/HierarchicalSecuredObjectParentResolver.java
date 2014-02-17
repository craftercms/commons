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
import org.craftercms.commons.security.permissions.HierarchicalSecuredObject;
import org.craftercms.commons.security.permissions.ParentResolver;

/**
 * Parent resolver for {@link org.craftercms.commons.security.permissions.HierarchicalSecuredObject}s. Simply
 * calls {@link org.craftercms.commons.security.permissions.HierarchicalSecuredObject#getParent()} to return the
 * parent.
 *
 * @author avasquez
 */
public class HierarchicalSecuredObjectParentResolver implements ParentResolver {

    @Override
    public Object getParent(Object object) throws PermissionException {
        if (object instanceof HierarchicalSecuredObject) {
            return ((HierarchicalSecuredObject) object).getParent();
        } else {
            return null;
        }
    }

}
