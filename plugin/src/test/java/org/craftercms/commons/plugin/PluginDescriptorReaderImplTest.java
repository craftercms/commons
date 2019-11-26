/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Link as published by
 * the Free Software Foundation, either version 3 of the Link, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public Link for more details.
 *
 * You should have received a copy of the GNU General Public Link
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.plugin;

import java.io.IOException;
import java.io.InputStream;

import org.craftercms.commons.plugin.exception.PluginException;
import org.craftercms.commons.plugin.impl.PluginDescriptorReaderImpl;
import org.craftercms.commons.plugin.model.BlueprintDescriptor;
import org.craftercms.commons.plugin.model.Parameter;
import org.craftercms.commons.plugin.model.Plugin;
import org.craftercms.commons.plugin.model.PluginDescriptor;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author joseross
 */
@SuppressWarnings("deprecation")
public class PluginDescriptorReaderImplTest {

    protected Resource descriptorV1 = new ClassPathResource("plugin/craftercms-plugin-v1.yaml");
    protected Resource descriptorV2 = new ClassPathResource("plugin/craftercms-plugin-v2.yaml");
    protected Resource descriptorParams = new ClassPathResource("plugin/craftercms-plugin-params.yaml");
    protected PluginDescriptorReader reader = new PluginDescriptorReaderImpl();

    @Test
    public void testV1() throws IOException, PluginException {
        try (InputStream is = descriptorV1.getInputStream()) {
            PluginDescriptor descriptor = reader.read(is);
            assertEquals("1", descriptor.getDescriptorVersion());
            assertNotNull(descriptor.getBlueprint());
            assertNull(descriptor.getPlugin());

            BlueprintDescriptor.Blueprint blueprint = descriptor.getBlueprint();
            assertEquals("Website Editorial Blueprint", blueprint.getName());
        }
    }

    @Test
    public void testV2() throws IOException, PluginException {
        try (InputStream is = descriptorV2.getInputStream()) {
            PluginDescriptor descriptor = reader.read(is);
            assertEquals("2", descriptor.getDescriptorVersion());
            assertNull(descriptor.getBlueprint());
            assertNotNull(descriptor.getPlugin());

            Plugin plugin = descriptor.getPlugin();
            assertEquals("Website Editorial Blueprint", plugin.getName());
            assertEquals("blueprint", plugin.getType());
            assertEquals(3, plugin.getTags().size());
            assertEquals(3, plugin.getCrafterCmsVersions().get(0).getMajor());
            assertEquals(2, plugin.getCrafterCmsEditions().size());
            assertEquals(6, plugin.getMedia().getScreenshots().size());
            assertEquals("Crafter Software", plugin.getDeveloper().getCompany().getName());
        }
    }

    @Test
    public void testParameters() throws IOException, PluginException {
        Parameter param = new Parameter();
        param.setLabel("Optional Key");
        param.setName("optionalKey");
        param.setRequired(false);
        param.setType(Parameter.Type.PASSWORD);

        try (InputStream is = descriptorParams.getInputStream()) {
            PluginDescriptor descriptor = reader.read(is);
            assertNull(descriptor.getBlueprint());
            assertNotNull(descriptor.getPlugin());

            Plugin plugin = descriptor.getPlugin();
            assertNotNull(plugin.getParameters());
            assertEquals(3, plugin.getParameters().size());
            assertEquals("AWS Access Key", plugin.getParameters().get(0).getLabel());
            assertEquals("awsAccessKey", plugin.getParameters().get(0).getName());

            assertEquals(param, plugin.getParameters().get(2));
        }
    }

}
