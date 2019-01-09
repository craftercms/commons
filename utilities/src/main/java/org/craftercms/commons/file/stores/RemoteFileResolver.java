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
package org.craftercms.commons.file.stores;

import java.io.IOException;

/**
 * Resolves special paths to files in remote stores.
 *
 * @author avasquez
 */
public interface RemoteFileResolver {

    /**
     * Given the specified path, returns a {@link RemoteFile} associated to the path.
     *
     * @param path the path to the remote file
     *
     * @return the remote file
     *
     * @throws IOException if an IO error occurs while trying to resolve the file
     * @throws IllegalArgumentException if the path can't be recognized as a remote path
     */
    RemoteFile resolve(String path) throws IOException, IllegalArgumentException;

}
