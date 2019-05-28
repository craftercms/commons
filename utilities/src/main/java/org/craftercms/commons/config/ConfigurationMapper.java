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
package org.craftercms.commons.config;

import java.io.InputStream;

/**
 * Interface for classes that map configuration data to Java bean classes.
 *
 * @author avasquez
 */
public interface ConfigurationMapper<T> {

    /**
     * Reads the configuration from the specified input stream, extracts the specific sub-configuration identified by
     * {@code configId} and maps the configuration to a Java bean.
     *
     * @param inputStream the IS of the configuration file
     * @param encoding the charset encoding of the configuration file
     * @param configId the ID of the specific sub-configuration to map
     *
     * @return the configuration object
     *
     * @throws ConfigurationException if an error occurs
     */
    T readConfig(InputStream inputStream, String encoding, String configId) throws ConfigurationException;

}
