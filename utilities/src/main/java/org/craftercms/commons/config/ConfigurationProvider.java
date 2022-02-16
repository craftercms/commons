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
package org.craftercms.commons.config;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines the operations to access configuration files
 *
 * @author joseross
 * @since 3.1.6
 */
public interface ConfigurationProvider {

    /**
     * Checks if the given configuration path exists
     * @param path the configuration path
     * @return true if the configuration exists
     */
    boolean configExists(String path);

    /**
     * Get the content of the given configuration file
     * @param path the configuration path
     * @return the content of the configuration
     * @throws IOException if there is any error reading the configuration
     */
    InputStream getConfig(String path) throws IOException;

}
