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

import org.apache.commons.configuration2.HierarchicalConfiguration;

/**
 * Provides access to configuration files taking into account the active environment
 *
 * @author joseross
 * @since 3.1.6
 */
public interface ConfigurationResolver {

    /**
     * Get the content of a given configuration file
     *
     * @param module the name of the module
     * @param path the path of the file
     * @param provider the provider to read the file
     * @return the configuration instance
     * @throws ConfigurationException if there is any error reading the configuration
     */
    HierarchicalConfiguration<?> getXmlConfiguration(String module, String path, ConfigurationProvider provider)
            throws ConfigurationException;

}
