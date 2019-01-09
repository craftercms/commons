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
package org.craftercms.commons.jackson.mvc;

/**
 * Defines how the SecureProperty Should be handel.
 */
public interface SecurePropertyHandler {
    /**
     * Checks if the property is allowed for the given role.
     * @param roles Roles to check if Property is allowed.
     * @param propertyName Property name to check. (this will be a full class name + actual property name)
     * @return True if the property must be suppress from the output, False otherwise.
     */
    boolean suppressProperty(final Object propertyName, final String[] roles);
}
