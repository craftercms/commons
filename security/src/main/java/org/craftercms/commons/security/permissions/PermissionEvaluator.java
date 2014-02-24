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
 * Evaluates or checks permissions.
 *
 * @author avasquez
 */
public interface PermissionEvaluator<S, O> {

    /**
     * Checks if the current subject (according to {@link org.craftercms.commons.security.permissions.SubjectResolver})
     * is allowed to perform the specified action to the given object.
     *
     * @param object                    the object whose permissions should be checked. If null, a global permission
     *                                  should be checked
     * @param action                    the action the subject wants to perform
     *
     * @return true if the subject is allowed to execute the action, false otherwise
     */
    boolean isAllowed(O object, String action) throws IllegalArgumentException, PermissionException;

    /**
     * Checks if the given subject is allowed to perform the specified action to the given object
     *
     * @param subject                   the subject
     * @param object                    the object whose permissions should be checked. If null, a global permission
     *                                  should be checked
     * @param action                    the action the subject wants to perform
     *
     * @return true if the subject is allowed to execute the action, false otherwise
     */
    boolean isAllowed(S subject, O object, String action) throws IllegalArgumentException, PermissionException;

}
