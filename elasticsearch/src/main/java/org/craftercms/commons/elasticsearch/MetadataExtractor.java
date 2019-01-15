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

package org.craftercms.commons.elasticsearch;

import java.util.Map;

import org.springframework.core.io.Resource;

/**
 * Defines the basic operation to process binary files and produce relevant metadata
 * @param <T> the type of object that provides the metadata
 * @author joseross
 */
public interface MetadataExtractor<T> {

    /**
     * Takes relevant metadata from the source object to the properties parameter
     * @param resource the original file containing the metadata (in case any extra operation is needed)
     * @param metadata the object that provides the metadata
     * @param properties the properties that will be indexed
     */
    void extract(Resource resource, T metadata, Map<String, Object> properties);

}
