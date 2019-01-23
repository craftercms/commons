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
import org.craftercms.commons.file.stores.RemotePath;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link RemoteFile} that basically is facade to a Spring {@code Resource}.
 *
 * @author avasquez
 */
public class ResourceBasedRemoteFile implements RemoteFile {

    private RemotePath path;
    private Resource resource;

    public ResourceBasedRemoteFile(RemotePath path, Resource resource) {
        this.path = path;
        this.resource = resource;
    }

    @Override
    public RemotePath getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public long getContentLength() throws IOException {
        return resource.contentLength();
    }

    @Override
    public Resource toResource() throws IOException {
        return resource;
    }

}
