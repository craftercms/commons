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

import java.util.Map;

/**
 * Service used to check permissions for subjects.
 *
 * @author avasquez
 */
public interface PermissionService {

    /**
     * Checks if the given subject is allowed to perform the specified action to the given object
     *
     * @param subject                   the subject
     * @param object                    the object whose permissions should be checked
     * @param action                    the action the subject wants to perform
     * @param variables                 the variable values. Placeholders in the  the subject condition, with the
     *                                  format {value}, are replaced using this values. Eg.: isOwner({ugcId}).
     * @param checkAncestorPermissions  true if the object's ancestor permissions should be checked if the subject
     *                                  doesn't match any of the current permissions
     * @return true if the subject is allowed to execute the action, false otherwise
     */
    boolean allow(Object subject, Object object, String action, Map<String, String> variables,
                  boolean checkAncestorPermissions) throws PermissionException;

}
