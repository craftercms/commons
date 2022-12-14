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
package org.craftercms.commons.rest;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.exceptions.InvalidManagementTokenException;

/**
 * Provides convenience functionality to validate token against configured secure token.
 *
 * @author jmendeza
 * @since 4.0.3
 */
public interface ManagementTokenAware {

    String getConfiguredToken();

    default void validateToken(final String requestToken) throws InvalidManagementTokenException {
        if (!StringUtils.equals(requestToken, getConfiguredToken())) {
            throw new InvalidManagementTokenException("Management authorization failed, invalid token.");
        }
    }
}
