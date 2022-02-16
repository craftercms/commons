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
package org.craftercms.commons.file.blob;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.springframework.core.io.Resource;

/**
 * Defines the operations to handle blob files
 *
 * @author joseross
 * @since 3.1.6
 */
public interface BlobStore {

    String CONFIG_KEY_ID = "id";
    String CONFIG_KEY_PATTERN = "pattern";
    String CONFIG_KEY_MAPPING = "mappings.mapping";
    String CONFIG_KEY_MAPPING_PUBLISHING_TARGET = "publishingTarget";
    String CONFIG_KEY_MAPPING_STORE_TARGET = "storeTarget";
    String CONFIG_KEY_MAPPING_PREFIX = "prefix";
    String CONFIG_KEY_CONFIGURATION = "configuration";

    /**
     * Performs all setup needed with the given configuration
     *
     * @param config the configuration object
     * @throws ConfigurationException is any error occurs
     */
    void init(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException;

    /**
     * Returns the unique id of the store
     * @return the id
     */
    String getId();

    /**
     * Indicates if the given path is compatible with the store
     * @param path path to check
     * @return true if the path is compatible
     */
    boolean isCompatible(String path);

    /**
     * Resolves the given blob to a readable resource
     * @param path the path of the resource
     * @param blob the blob file
     * @return the resource object
     */
    Resource getResource(String path, Blob blob);

}
