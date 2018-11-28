/*
 * Copyright (C) 2007-2018 Crafter Software Corporation.
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

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * Utility methods for Apache Commons based configuration.
 *
 * @author avasquez
 */
public class ConfigUtils {

    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Reads the XML configuration from the specified input stream, using the {@link #DEFAULT_ENCODING}.
     *
     * @param input         the input stream from where to read the configuration
     *
     * @return the loaded XML configuration, as a an Apache Commons {@link HierarchicalConfiguration}
     *
     * @throws ConfigurationException if an error occurs while reading the configuration
     */
    public static HierarchicalConfiguration<ImmutableNode> readXmlConfiguration(InputStream input)
            throws ConfigurationException {
        return readXmlConfiguration(input, null);
    }

    /**
     * Reads the XML configuration from the specified input stream, using the given file encoding.
     *
     * @param input         the input stream from where to read the configuration
     * @param fileEncoding  the encoding of the file. If not specified {@link #DEFAULT_ENCODING} will be used
     *
     * @return the loaded XML configuration, as a an Apache Commons {@link HierarchicalConfiguration}
     *
     * @throws ConfigurationException if an error occurs while reading the configuration
     */
    public static HierarchicalConfiguration<ImmutableNode> readXmlConfiguration(InputStream input, String fileEncoding)
            throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class);
        XMLConfiguration config = builder.configure(params.xml()).getConfiguration();
        FileHandler fileHandler = new FileHandler(config);

        fileHandler.setEncoding(StringUtils.isNotBlank(fileEncoding) ? fileEncoding : DEFAULT_ENCODING);
        fileHandler.load(input);

        return config;
    }

    private ConfigUtils() {
    }

}
