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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ConfigurationResolver}
 *
 * @author joseross
 * @since 3.1.6
 */
public class ConfigurationResolverImpl implements ConfigurationResolver {

    public static final String DEFAULT_ENVIRONMENT = "default";

    public static final String PLACEHOLDER_MODULE = "module";

    public static final String PLACEHOLDER_ENVIRONMENT = "environment";

    /**
     * The active environment
     */
    protected String environment;

    /**
     * The path pattern for the default environment
     */
    protected String basePath;

    /**
     * The path pattern for a specific environment
     */
    protected String envPath;

    protected EncryptionAwareConfigurationReader configurationReader;

    @ConstructorProperties({"environment", "basePath", "envPath", "configurationReader"})
    public ConfigurationResolverImpl(String environment, String basePath, String envPath,
                                     EncryptionAwareConfigurationReader configurationReader) {
        this.environment = environment;
        this.basePath = basePath;
        this.envPath = envPath;
        this.configurationReader = configurationReader;
    }

    @Override
    public HierarchicalConfiguration<?> getXmlConfiguration(String module, String path, ConfigurationProvider provider)
            throws ConfigurationException {
        Map<String, String> values = new HashMap<>();
        values.put(PLACEHOLDER_MODULE, module);
        values.put(PLACEHOLDER_ENVIRONMENT, environment);
        StrSubstitutor substitutor = new StrSubstitutor(values, "{", "}");

        String url = null;

        try {
            if (!StringUtils.equals(environment, DEFAULT_ENVIRONMENT)) {
                url = Paths.get(substitutor.replace(envPath), path).toString();

                if (!provider.configExists(url)) {
                    url = Paths.get(substitutor.replace(basePath), path).toString();
                }
            } else {
                url = Paths.get(substitutor.replace(basePath), path).toString();
            }

            if (provider.configExists(url)) {
                return configurationReader.readXmlConfiguration(provider.getConfig(url));
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ConfigurationException("Error reading configuration file at " + url, e);
        }
    }

}
