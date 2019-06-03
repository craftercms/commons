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

import java.io.InputStream;
import java.io.Reader;

import org.craftercms.commons.plugin.exception.PluginException;
import org.craftercms.commons.plugin.model.PluginDescriptor;

/**
 * Parses a {@link PluginDescriptor} from various sources
 *
 * @author joseross
 * @since 3.1.1
 */
public interface PluginDescriptorReader {

    /**
     * Parses a {@link PluginDescriptor} from the given stream
     * @param is the stream to parse
     * @return the plugin descriptor
     * @throws PluginException if there is any error parsing the stream
     */
    PluginDescriptor read(InputStream is) throws PluginException;

    /**
     * Parses a {@link PluginDescriptor} from the given reader
     * @param reader the reader to parse
     * @return the plugin descriptor
     * @throws PluginException if there is any error parsing the reader
     */
    PluginDescriptor read(Reader reader) throws PluginException;

}
