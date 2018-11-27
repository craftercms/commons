/*
 * Copyright (C) 2007-2018 Crafter Software Corporation.
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
package org.craftercms.commons.file.stores;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for "proxies" to remote file stores.
 *
 * @author avasquez
 */
public interface RemoteFileStore {

//    /**
//     * Downloads the remote file for the given ID. The downloaded file is temporary, so it should be deleted after
//     * being used.
//     *
//     * @param path the path of the file in the remote store
//     *
//     * @return the path to the downloaded file
//     *
//     * @throws IOException if an error occurs while accessing file
//     */
//    Path downloadFile(RemotePath path) throws IOException;
//
//    /**
//     * Returns the remote file as a Spring {@code resource}
//     *
//     * @param path the path of the file in the remote store
//     *
//     * @return the file as a {@code Resource}
//     *
//     * @throws IOException if an error occurs while retrieving the file
//     */
//    Resource getFileAsResource(RemotePath path) throws IOException;

    RemoteFile getFile(RemotePath path) throws IOException;

}
