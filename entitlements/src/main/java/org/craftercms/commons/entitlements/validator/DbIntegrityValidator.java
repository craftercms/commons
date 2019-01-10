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

package org.craftercms.commons.entitlements.validator;

import java.sql.Connection;
import java.sql.SQLException;

import org.craftercms.commons.entitlements.exception.EntitlementException;

/**
 * Defines the operations to validate a database.
 *
 * @author joseross
 */
public interface DbIntegrityValidator {

    /**
     * Generates the validator value for a new database.
     * @param connection connection to the database
     * @throws SQLException if there is any connection error
     */
    void store(Connection connection) throws SQLException;

    /**
     * Checks the validation value for an existing database.
     * @param connection connection to the database
     * @throws EntitlementException if the validation fails
     * @throws SQLException if there is any connection error
     */
    void validate(Connection connection) throws EntitlementException, SQLException;

}
