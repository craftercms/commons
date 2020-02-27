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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.interpol.Lookup;
import org.craftercms.commons.crypto.TextEncryptor;
import org.springframework.core.io.Resource;

import static java.util.Collections.singletonMap;

/**
 * Base class that loads configuration files handling encrypted properties
 *
 * @since 3.1.5
 * @author joseross
 */
public class EncryptionAwareConfigurationReader {

    public static final char DEFAULT_LIST_DELIMITER = ',';
    public static final String DEFAULT_ENCRYPTED_VALUE_PREFIX = "enc";

    protected char configListDelimiter = DEFAULT_LIST_DELIMITER;
    protected String encryptedValuePrefix = DEFAULT_ENCRYPTED_VALUE_PREFIX;
    protected TextEncryptor textEncryptor;

    protected Map<String, Lookup> prefixLookups;

    public EncryptionAwareConfigurationReader(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;

        if(textEncryptor != null) {
            prefixLookups = singletonMap(encryptedValuePrefix, new DecryptionLookup(textEncryptor));
        }
    }

    public EncryptionAwareConfigurationReader(char configListDelimiter, String encryptedValuePrefix,
                                              TextEncryptor textEncryptor) {
        this(textEncryptor);
        this.configListDelimiter = configListDelimiter;
        this.encryptedValuePrefix = encryptedValuePrefix;

    }

    public HierarchicalConfiguration<?> readXmlConfiguration(InputStream inputStream, String encoding)
        throws ConfigurationException {
        return ConfigUtils.readXmlConfiguration(inputStream, prefixLookups, encoding);
    }

    public HierarchicalConfiguration<?> readXmlConfiguration(Resource resource) throws ConfigurationException {
        return ConfigUtils.readXmlConfiguration(resource, configListDelimiter, prefixLookups);
    }

    public HierarchicalConfiguration<?> readYamlConfiguration(Reader reader) throws ConfigurationException {
        return ConfigUtils.readYamlConfiguration(reader, prefixLookups);
    }

    public HierarchicalConfiguration<?> readYamlConfiguration(File file) throws ConfigurationException {
        try(Reader reader = new FileReader(file)) {
            return readYamlConfiguration(reader);
        } catch (IOException e) {
            throw new ConfigurationException("Error reading YAML file at " + file, e);
        }
    }

    public HierarchicalConfiguration<?> readYamlConfiguration(Resource resource) throws ConfigurationException {
        try(Reader reader = new InputStreamReader(resource.getInputStream())) {
            return readYamlConfiguration(reader);
        } catch (IOException e) {
            throw new ConfigurationException("Error reading YAML file at " + resource, e);
        }
    }

}
