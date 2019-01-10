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
package org.craftercms.commons.file.stores.impl;

import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.RemoteFileStore;
import org.craftercms.commons.file.stores.RemotePath;
import org.craftercms.commons.spring.resources.RangeAwareUrlResource;

import java.io.IOException;

/**
 * Simple {@link org.craftercms.commons.file.stores.RemoteFileStore} where the paths are basically URLs from where
 * the files can be accessed.
 *
 * @author avasquez
 */
public class UrlBasedFileStore implements RemoteFileStore {

    @Override
    public RemoteFile getFile(RemotePath path) throws IOException {
        return new ResourceBasedRemoteFile(path, new RangeAwareUrlResource(path.getPath()));
    }

}
