/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.lang;

/**
 * Represents a general callback that can be used almost in any method that needs this pattern.
 *
 * @author avasquez
 */
public interface Callback<T> {

    /**
     * Executes the callback, returning the result or throwing a runtime exception if an error occurs.
     *
     * @return the result of the execution
     */
    T execute();

}
