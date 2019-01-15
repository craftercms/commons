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

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a file stored remotely.
 *
 * @author avasquez
 */
public interface RemoteFile {

    /**
     * Returns information about the path of the file in the remote store.
     */
    RemotePath getPath();

    /**
     * Returns an input stream to the content of the file.
     *
     * @throws IOException if an error occurs while trying to access the file content
     */
    InputStream getInputStream() throws IOException;

    /**
     * Returns the content length of the file.
     * @throws IOException if an error occurs while trying to access the file
     */
    long getContentLength() throws IOException;

    /**
     * Returns the remote file as a Spring {@code Resource}.
     *
     * @throws IOException if the file can't be resolved successfully as a resource
     */
    Resource toResource() throws IOException;

}
