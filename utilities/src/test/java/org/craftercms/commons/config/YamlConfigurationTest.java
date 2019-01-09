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

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link YamlConfiguration}.
 *
 * @author avasquez
 */
public class YamlConfigurationTest {

    private YamlConfiguration yamlConfiguration;

    @Before
    public void setUp() throws Exception {
        yamlConfiguration = new YamlConfiguration();
    }

    @Test
    public void testRead() throws Exception {
        Resource invoiceResource = new ClassPathResource("yaml/invoice.yaml");

        yamlConfiguration.read(invoiceResource.getInputStream());

        assertEquals(34843, yamlConfiguration.getInt("invoice"));
        assertEquals("Royal Oak", yamlConfiguration.getString("billTo.address.city"));
        assertEquals("Super Hoop", yamlConfiguration.getString("product(1).description"));
        assertEquals(4443.52, yamlConfiguration.getDouble("total"), 0);
    }

}
