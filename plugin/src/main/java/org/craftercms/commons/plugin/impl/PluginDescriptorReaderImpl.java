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

package org.craftercms.commons.plugin.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.craftercms.commons.plugin.model.PluginDescriptor;
import org.craftercms.commons.plugin.PluginDescriptorReader;
import org.craftercms.commons.plugin.exception.PluginException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Default implementation of {@link PluginDescriptorReader} that parses YAML files.
 *
 * @author joseross
 * @since 3.1.1
 */
public class PluginDescriptorReaderImpl implements PluginDescriptorReader {

    /**
     * {@inheritDoc}
     */
    @Override
    public PluginDescriptor read(final InputStream is) throws PluginException {
        return read(new InputStreamReader(is));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PluginDescriptor read(final Reader reader) throws PluginException {
        try {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(PluginDescriptor.class), representer);
            return yaml.loadAs(reader, PluginDescriptor.class);
        } catch (Exception e) {
            throw new PluginException("Error reading plugin descriptor from reader", e);
        }
    }

}
