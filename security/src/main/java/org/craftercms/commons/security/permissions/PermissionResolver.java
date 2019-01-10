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

import org.craftercms.commons.security.exception.PermissionException;

/**
 * Resolves the permissions for a particular type of subjects and resources.
 *
 * @author avasquez
 */
public interface PermissionResolver<S, R> {

    /**
     * Returns the global permission (that applies to any or all resources) associated to the given subject.
     *
     * @param subject the subject
     * @return the global permission, or null if no permission found
     */
    Permission getGlobalPermission(S subject) throws PermissionException;

    /**
     * Returns the permission associated to the given subject and resource.
     *
     * @param subject the subject (not null)
     * @param resource  the protected resource or ID/IDs of the protected resource (not null).
     * @return the object/subject permission, or null if no permission found
     */
    Permission getPermission(S subject, R resource) throws PermissionException;

}
