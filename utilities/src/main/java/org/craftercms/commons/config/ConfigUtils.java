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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.List;

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
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<XMLConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(XMLConfiguration.class);
            XMLConfiguration config = builder.configure(params.xml()).getConfiguration();
            FileHandler fileHandler = new FileHandler(config);

            fileHandler.setEncoding(StringUtils.isNotBlank(fileEncoding) ? fileEncoding : DEFAULT_ENCODING);
            fileHandler.load(input);

            return config;
        } catch (Exception e) {
            throw new ConfigurationException("Unable to read XML configuration", e);
        }
    }

    /**
     * Returns the specified String property from the configuration
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the String value of the property, or null if not found
     * @throws ConfigurationException if an error occurred
     */
    public static String getStringProperty(Configuration config, String key) throws ConfigurationException {
        return getStringProperty(config, key, null);
    }

    /**
     * Returns the specified String property from the configuration
     *
     * @param config       the configuration
     * @param key          the key of the property
     * @param defaultValue the default value if the property is not found
     * @return the String value of the property, or the default value if not found
     * @throws ConfigurationException if an error occurred
     */
    public static String getStringProperty(Configuration config, String key,
                                           String defaultValue) throws ConfigurationException {
        try {
            return config.getString(key, defaultValue);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve property '" + key + "'", e);
        }
    }

    /**
     * Returns the specified String property from the configuration. If the property is missing a
     * {@link MissingConfigurationException} is thrown.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the String value of the property
     * @throws MissingConfigurationException if the property is missing from the configuration
     * @throws ConfigurationException        if an error occurred
     */
    public static String getRequiredStringProperty(Configuration config,
                                                   String key) throws ConfigurationException {
        String property = getStringProperty(config, key);
        if (StringUtils.isEmpty(property)) {
            throw new MissingConfigurationException(key);
        } else {
            return property;
        }
    }

    /**
     * Returns the specified Boolean property from the configuration
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Boolean value of the property, or null if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Boolean getBooleanProperty(Configuration config, String key) throws ConfigurationException {
        return getBooleanProperty(config, key, null);
    }

    /**
     * Returns the specified Boolean property from the configuration
     *
     * @param config       the configuration
     * @param key          the key of the property
     * @param defaultValue the default value if the property is not found
     * @return the Boolean value of the property, or the default value if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Boolean getBooleanProperty(Configuration config, String key,
                                             Boolean defaultValue) throws ConfigurationException {
        try {
            return config.getBoolean(key, defaultValue);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve property '" + key + "'", e);
        }
    }

    /**
     * Returns the specified Boolean property from the configuration. If the property is missing a
     * {@link MissingConfigurationException} is thrown.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Boolean value of the property
     * @throws MissingConfigurationException if the property is missing from the configuration
     * @throws ConfigurationException        if an error occurred
     */
    public static Boolean getRequiredBooleanProperty(Configuration config,
                                                     String key) throws ConfigurationException {
        Boolean property = getBooleanProperty(config, key);
        if (property == null) {
            throw new MissingConfigurationException(key);
        } else {
            return property;
        }
    }

    /**
     * Returns the specified Integer property from the configuration
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Integer value of the property, or null if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Integer getIntegerProperty(Configuration config, String key) throws ConfigurationException {
        return getIntegerProperty(config, key, null);
    }

    /**
     * Returns the specified Integer property from the configuration
     *
     * @param config       the configuration
     * @param key          the key of the property
     * @param defaultValue the default value if the property is not found
     * @return the Integer value of the property, or the default value if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Integer getIntegerProperty(Configuration config, String key,
                                             Integer defaultValue) throws ConfigurationException {
        try {
            return config.getInteger(key, defaultValue);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve property '" + key + "'", e);
        }
    }

    /**
     * Returns the specified Integer property from the configuration. If the property is missing a
     * {@link MissingConfigurationException} is thrown.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Integer value of the property
     * @throws MissingConfigurationException if the property is missing from the configuration
     * @throws ConfigurationException        if an error occurred
     */
    public static Integer getRequiredIntegerProperty(Configuration config,
                                                     String key) throws ConfigurationException {
        Integer property = getIntegerProperty(config, key);
        if (property == null) {
            throw new MissingConfigurationException(key);
        } else {
            return property;
        }
    }

    /**
     * Returns the specified Long property from the configuration
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Long value of the property, or null if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Long getLongProperty(Configuration config, String key) throws ConfigurationException {
        return getLongProperty(config, key, null);
    }

    /**
     * Returns the specified Long property from the configuration
     *
     * @param config       the configuration
     * @param key          the key of the property
     * @param defaultValue the default value if the property is not found
     * @return the Long value of the property, or the default value if not found
     * @throws ConfigurationException if an error occurred
     */
    public static Long getLongProperty(Configuration config, String key,
                                       Long defaultValue) throws ConfigurationException {
        try {
            return config.getLong(key, defaultValue);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve property '" + key + "'", e);
        }
    }

    /**
     * Returns the specified Long property from the configuration. If the property is missing a
     * {@link MissingConfigurationException} is thrown.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the Long value of the property
     * @throws MissingConfigurationException if the property is missing from the configuration
     * @throws ConfigurationException        if an error occurred
     */
    public static Long getRequiredLongProperty(Configuration config,
                                               String key) throws ConfigurationException {
        Long property = getLongProperty(config, key);
        if (property == null) {
            throw new MissingConfigurationException(key);
        } else {
            return property;
        }
    }

    /**
     * Returns the specified String array property from the configuration. A String array property is normally
     * specified as a String with values separated by commas in the configuration.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the String array value of the property, or null if not found
     * @throws ConfigurationException if an error occurred
     */
    public static String[] getStringArrayProperty(Configuration config,
                                                  String key) throws ConfigurationException {
        try {
            return config.getStringArray(key);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve property '" + key + "'", e);
        }
    }

    /**
     * Returns the specified String array property from the configuration. If the property is missing a
     * {@link MissingConfigurationException} is thrown. A String array property is normally specified as
     * a String with values separated by commas in the configuration.
     *
     * @param config the configuration
     * @param key    the key of the property
     * @return the String value of the property
     * @throws MissingConfigurationException if the property is missing from the configuration
     * @throws ConfigurationException        if an error occurred
     */
    public static String[] getRequiredStringArrayProperty(Configuration config,
                                                          String key) throws ConfigurationException {
        String[] property = getStringArrayProperty(config, key);
        if (ArrayUtils.isEmpty(property)) {
            throw new MissingConfigurationException(key);
        } else {
            return property;
        }
    }

    /**
     * Returns the sub-configuration whose root is the specified key.
     *
     * @param config the main configuration
     * @param key    the sub-configuration key
     * @return the sub-configuration, or null if not found
     * @throws ConfigurationException if an error occurs
     */
    public static HierarchicalConfiguration<ImmutableNode> getConfigurationAt(
            HierarchicalConfiguration<ImmutableNode> config, String key) throws ConfigurationException {
        try {
            return config.configurationAt(key);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve sub-configurations at '" + key + "'", e);
        }
    }

    /**
     * Returns the sub-configuration whose root is the specified key. If it's missing a
     * {@link MissingConfigurationException} is thrown.
     *
     * @param config the main configuration
     * @param key    the sub-configuration key
     * @return the sub-configuration, or null if not found
     * @throws MissingConfigurationException if the sub-configuration is missing from the configuration
     * @throws ConfigurationException        if an error occurs
     */
    public static HierarchicalConfiguration<ImmutableNode> getRequiredConfigurationAt(
            HierarchicalConfiguration<ImmutableNode> config, String key) throws ConfigurationException {
        try {
            return config.configurationAt(key);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve sub-configurations at '" + key + "'", e);
        }
    }


    /**
     * Returns the sub-configurations whose root is the specified key.
     *
     * @param config the main configuration
     * @param key    the sub-configurations key
     * @return the sub-configurations, or null if not found
     * @throws ConfigurationException if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static List<HierarchicalConfiguration<ImmutableNode>> getConfigurationsAt(
            HierarchicalConfiguration<ImmutableNode> config, String key) throws ConfigurationException {
        try {
            return config.configurationsAt(key);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to retrieve sub-configurations at '" + key + "'", e);
        }
    }

    /**
     * Returns the sub-configurations whose root is the specified key. If they're missing a
     * {@link MissingConfigurationException} is thrown
     *
     * @param config the main configuration
     * @param key    the sub-configurations key
     * @return the sub-configurations
     * @throws MissingConfigurationException if the sub-configurations are missing from the configuration
     * @throws ConfigurationException        if an error occurs
     */
    @SuppressWarnings("unchecked")
    public static List<HierarchicalConfiguration<ImmutableNode>> getRequiredConfigurationsAt(
            HierarchicalConfiguration<ImmutableNode> config, String key) throws ConfigurationException {
        List<HierarchicalConfiguration<ImmutableNode>> configs = getConfigurationsAt(config, key);
        if (CollectionUtils.isEmpty(configs)) {
            throw new MissingConfigurationException(key);
        } else {
            return configs;
        }
    }

    protected ConfigUtils() {
    }

}
