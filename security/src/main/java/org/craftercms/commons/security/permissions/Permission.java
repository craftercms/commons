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

/**
 * Represents a permission that allows or denies a subject (a user, an application, etc.) the execution of an action
 * or set of actions on a resource.
 *
 * @author avasquez
 */
public interface Permission {

    /**
     * Returns true if action execution is allowed
     *
     * @param action the action to execute
     * @return true if action execution is allowed, false otherwise.
     */
    boolean isAllowed(String action);

}
