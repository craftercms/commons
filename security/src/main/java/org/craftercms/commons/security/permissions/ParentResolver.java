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

import org.craftercms.commons.security.exception.PermissionException;

/**
 * Resolves the parent of a given object.
 *
 * @author avasquez
 */
public interface ParentResolver {

    /***
     * Returns the parent of the given object.
     *
     * @param object    the object whose parent should be looked for
     *
     * @return  the object's parent, or null if not found.
     */
    Object getParent(Object object) throws PermissionException;

}
