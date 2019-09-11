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

package org.craftercms.commons.spring.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

import static org.craftercms.commons.lang.UrlUtils.RANGE_HEADER_FORMAT;
import static org.craftercms.commons.lang.UrlUtils.RANGE_HEADER_NAME;
import static org.craftercms.commons.lang.UrlUtils.RANGE_NO_END_HEADER_FORMAT;

/**
 * Implementation of {@link Resource} for WebDAV
 *
 * @author joseross
 * @since 3.1.4
 */
public class WebDavResource implements RangeAwareResource {

    private static final Logger logger = LoggerFactory.getLogger(WebDavResource.class);

    protected Sardine client;
    protected String path;

    public WebDavResource(final Sardine client, final String path) {
        this.client = client;
        this.path = path;
    }

    @Override
    public InputStream getInputStream(final long start, final long end) throws IOException {
        Map<String, String> headers;
        if (end > 0) {
            headers = Collections.singletonMap(RANGE_HEADER_NAME, String.format(RANGE_HEADER_FORMAT, start, end));
        } else {
            headers = Collections.singletonMap(RANGE_HEADER_NAME, String.format(RANGE_NO_END_HEADER_FORMAT, start));
        }
        return client.get(path, headers);
    }

    @Override
    public boolean exists() {
        try {
            return client.exists(path);
        } catch (IOException e) {
            logger.error("Error checking file at {}", path, e);
            return false;
        }
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a URL");
    }

    @Override
    public URI getURI() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a URI");
    }

    @Override
    public File getFile() throws IOException {
        throw new IOException(getDescription() + " can't be resolved to a File");
    }

    @Override
    public long contentLength() throws IOException {
        return getResource().getContentLength();
    }

    @Override
    public long lastModified() throws IOException {
        return getResource().getModified().getTime();
    }

    @Override
    public Resource createRelative(final String relativePath) throws IOException {
        throw new IOException(getDescription() + " doesn't support relative resources");
    }

    @Override
    public String getFilename() {
        try {
            return getResource().getName();
        } catch (IOException e) {
            logger.error("Error getting file name for {}", path, e);
            return path;
        }
    }

    @Override
    public String getDescription() {
        try {
            return getResource().getDisplayName();
        } catch (IOException e) {
            logger.error("Error getting file description for {}", path, e);
            return null;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return client.get(path);
    }

    protected DavResource getResource() throws IOException {
        return client.list(path, 1, true).stream().findFirst().orElseThrow(FileNotFoundException::new);
    }

}
