/*
 * Copyright (C) 2007-2024 Crafter Software Corporation. All Rights Reserved.
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

import com.amazonaws.util.StringInputStream;
import junit.framework.TestCase;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.crypto.TextEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EncryptionAwareConfigurationReaderTest extends TestCase {
    private static final String CONFIG_CONTENT = "<configuration>" +
            "<header>This is the site: ${siteName}</header>" +
            "<java-version>${sys:java.version}</java-version>" +
            "<env-var>${env:TEST_ENV_PROPERTY1}</env-var>" +
            "<secret>${enc:ENCRYPTED_VALUE}</secret>" +
            "</configuration>";
    private static final String SITE_NAME = "test-site";
    private static final String SITE_NAME_VARIABLE = "siteName";
    private static final String CONFIGURATION_PROPERTY_KEY = "header";
    private static final String ENV_VARIABLE_PROPERTY_KEY = "env-var";
    private static final String ENV_VARIABLE_VALUE = "CUSTOM_VALUE";
    public static final String SECRET_PROPERTY_KEY = "secret";
    private static final String DECRYPTED_SECRET_VALUE = "this is the secret";
    private static final String ENCRYPTED_VALUE = "ENCRYPTED_VALUE";
    private Map<String, String> lookupVariables;

    @Mock
    protected TextEncryptor textEncryptor;
    protected EncryptionAwareConfigurationReader encryptionAwareConfigurationReader;

    @Override
    @Before
    public void setUp() throws Exception {
        when(textEncryptor.decrypt(ENCRYPTED_VALUE)).thenReturn(DECRYPTED_SECRET_VALUE);
        encryptionAwareConfigurationReader = new EncryptionAwareConfigurationReader(textEncryptor);
        lookupVariables = Map.of(SITE_NAME_VARIABLE, SITE_NAME);
    }

    @Test
    public void testReadXmlLookupVariable() throws UnsupportedEncodingException, ConfigurationException {
        HierarchicalConfiguration<?> xmlConfiguration = encryptionAwareConfigurationReader
                .readXmlConfiguration(new StringInputStream(CONFIG_CONTENT), lookupVariables);
        assertEquals("This is the site: " + SITE_NAME, xmlConfiguration.getString(CONFIGURATION_PROPERTY_KEY));
    }

    @Test
    public void testReadXmlEnvVariable() throws UnsupportedEncodingException, ConfigurationException {
        HierarchicalConfiguration<?> xmlConfiguration = encryptionAwareConfigurationReader
                .readXmlConfiguration(new StringInputStream(CONFIG_CONTENT), lookupVariables);
        assertEquals(ENV_VARIABLE_VALUE, xmlConfiguration.getString(ENV_VARIABLE_PROPERTY_KEY));
    }

    @Test
    public void testReadXmlEncryptedValue() throws UnsupportedEncodingException, ConfigurationException {
        HierarchicalConfiguration<?> xmlConfiguration = encryptionAwareConfigurationReader
                .readXmlConfiguration(new StringInputStream(CONFIG_CONTENT), lookupVariables);
        assertEquals(DECRYPTED_SECRET_VALUE, xmlConfiguration.getString(SECRET_PROPERTY_KEY));
    }
}
