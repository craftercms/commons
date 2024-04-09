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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationResolverImplTest {

    private static final String SITE_NAME = "test-site";
    private static final String SITE_NAME_VARIABLE = "siteName";
    private static final String CONFIG_PATH = "test-configuration-path.xml";
    private static final String BASE_PATH = "";
    private static final String ENVIRONMENT = "test-env";
    private static final String ENV_PATH = "envs/test-env";

    @Mock
    EncryptionAwareConfigurationReader configurationReader;
    @Mock
    ConfigurationProvider configurationProvider;
    @InjectMocks
    private ConfigurationResolverImpl configurationResolver;

    @Before
    public void setUp() throws Exception {
        when(configurationProvider.configExists(anyString())).thenReturn(true);
        Map<String, String> lookupVariables = Map.of(SITE_NAME_VARIABLE, SITE_NAME);
        when(configurationProvider.getLookupVariables()).thenReturn(lookupVariables);
        configurationResolver = new ConfigurationResolverImpl(ENVIRONMENT, BASE_PATH, ENV_PATH, configurationReader);
    }

    @Test
    public void testConfigurationWithVariable() throws ConfigurationException {
        configurationResolver.getXmlConfiguration("", CONFIG_PATH, configurationProvider);
        verify(configurationReader).readXmlConfiguration(isNull(InputStream.class),
                argThat(variables -> variables.get(SITE_NAME_VARIABLE).equals(SITE_NAME)));
    }
}
