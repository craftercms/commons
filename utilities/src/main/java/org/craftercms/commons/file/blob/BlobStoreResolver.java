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

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationProvider;

import java.io.IOException;

/**
 * Provides access to all known implementations of {@link BlobStore}
 *
 * @author joseross
 * @since 3.1.6
 */
public interface BlobStoreResolver {

    /**
     * Returns the {@link BlobStore} for the given id
     * @param provider provider to read the configuration file
     * @param storeId the id
     * @return the blob store
     */
    BlobStore getById(ConfigurationProvider provider, String storeId) throws ConfigurationException;

}
