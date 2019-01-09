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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.InputStreamSupport;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * <p>
 * Implementation of Apache Commons HierarchicalConfiguration that parses a YAML configuration file and loads
 * the configuration in memory for easy access.
 * </p>
 *
 * <p>Example configuration file:</p>
 *
 * <pre>
 * server:
 *      hostname: localhost
 *      port: 8080
 * mimeTypes:
 *      - application/pdf
 *      - application/msword
 * </pre>
 *
 * <p>How to read and access the configuration</p>
 *
 * <pre>
 * YamlConfiguration config = new YamlConfiguration();
 * config.read(new FileReader("config.yaml"));
 *
 * String serverHostname = config.getString("server.hostname");
 * String serverPort = config.getInt("server.port");
 * String pdfMimeType = config.getString("mimeTypes(0)");
 * String mswordMimeType = config.getString("mimeTypes(1)");
 * </pre>
 *
 * @author avasquez
 */
public class YamlConfiguration extends BaseHierarchicalConfiguration implements FileBasedConfiguration, InputStreamSupport {

    public static final String LOG_KEY_YAML_LOADED = "configuration.yaml.yamlLoaded";
    public static final String ERROR_KEY_WRITE_NOT_SUPPORTED = "configuration.yaml.writeNotSupported";
    public static final String ERROR_KEY_LOAD_ERROR = "configuration.yaml.loadError";

    private static final I10nLogger logger = new I10nLogger(YamlConfiguration.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    @Override
    public void read(Reader in) throws ConfigurationException, IOException {
        load(in);
    }

    @Override
    public void read(InputStream in) throws ConfigurationException, IOException {
        load(new InputStreamReader(in, "UTF-8"));
    }

    @Override
    public void write(Writer out) throws ConfigurationException, IOException {
        throw new UnsupportedOperationException(I10nUtils.getLocalizedMessage(I10nUtils.DEFAULT_ERROR_MESSAGE_BUNDLE_NAME,
                                                                              ERROR_KEY_WRITE_NOT_SUPPORTED));
    }

    @SuppressWarnings("unchecked")
    protected void load(Reader in) throws ConfigurationException {
        try {
            Yaml yaml = new Yaml();

            Map<String, Object> yamlObj = yaml.loadAs(in, Map.class);

            logger.debug(LOG_KEY_YAML_LOADED, yamlObj);

            buildConfig(yamlObj);
        } catch (Exception e) {
            throw new ConfigurationException(I10nUtils.getLocalizedMessage(I10nUtils.DEFAULT_ERROR_MESSAGE_BUNDLE_NAME,
                                                                           ERROR_KEY_LOAD_ERROR), e);
        }
    }

    protected void buildConfig(Map<String, Object> yamlObj) {
        ImmutableNode.Builder root = new ImmutableNode.Builder();

        if (MapUtils.isNotEmpty(yamlObj)) {
            buildConfigFromMap(yamlObj, root);
        }

        addNodes(null, root.create().getChildren());
    }

    @SuppressWarnings("unchecked")
    protected void buildConfigFromKeyValuePair(String name, Object value, ImmutableNode.Builder parent) {
        if (value instanceof Map) {
            ImmutableNode.Builder node = new ImmutableNode.Builder();
            node.name(name);

            buildConfigFromMap((Map<String, Object>)value, node);

            parent.addChild(node.create());
        } else if (value instanceof Collection) {
            buildConfigFromCollection(name, (Collection<Object>)value, parent);
        } else {
            ImmutableNode.Builder node = new ImmutableNode.Builder();
            node.name(name);
            node.value(value);

            parent.addChild(node.create());
        }
    }

    protected void buildConfigFromMap(Map<String, Object> map, ImmutableNode.Builder parent) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            buildConfigFromKeyValuePair(name, value, parent);
        }
    }

    protected void buildConfigFromCollection(String name, Collection<Object> collection, ImmutableNode.Builder parent) {
        for (Object value : collection) {
            buildConfigFromKeyValuePair(name, value, parent);
        }
    }

}
