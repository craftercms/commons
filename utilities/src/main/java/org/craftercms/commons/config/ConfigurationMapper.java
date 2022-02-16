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
import org.apache.commons.configuration2.tree.ImmutableNode;

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
     * @param provider the provider to read the configuration file
     * @param encoding the charset encoding of the configuration file
     * @param configId the ID of the specific sub-configuration to map
     *
     * @return the configuration object
     *
     * @throws ConfigurationException if an error occurs
     */
    T readConfig(ConfigurationProvider provider, String module, String path, String encoding, String configId)
            throws ConfigurationException;

    /**
     * Reads a single profile from the given configuration
     *
     * @param config the profile configuration
     *
     * @return the configuration object
     *
     * @throws ConfigurationException if an error occurs
     */
    T processConfig(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException;

}
