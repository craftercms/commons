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
 * Evaluates or checks permissions.
 *
 * @author avasquez
 */
public interface PermissionEvaluator<S, R> {

    /**
     * Checks if the current subject (according to {@link org.craftercms.commons.security.permissions.SubjectResolver})
     * is allowed to perform the specified action on the given resource.
     *
     * @param resource the resource or ID/IDs of the resource whose permissions should be checked. If null,
     *                 the global permission should be checked
     * @param action the action the subject wants to perform (not null)
     * @return true if the subject is allowed to execute the action, false otherwise
     */
    boolean isAllowed(R resource, String action) throws PermissionException;

    /**
     * Checks if the given subject is allowed to perform the specified action on the given resource
     *
     * @param subject the subject (not null)
     * @param resource the resource or ID/IDs of the resource whose permissions should be checked. If null,
     *                 the global permission should be checked
     * @param action the action the subject wants to perform (not null)
     * @return true if the subject is allowed to execute the action, false otherwise
     */
    boolean isAllowed(S subject, R resource, String action) throws PermissionException;

}
