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

import com.box.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static org.craftercms.commons.lang.UrlUtils.RANGE_HEADER_FORMAT;
import static org.craftercms.commons.lang.UrlUtils.RANGE_HEADER_NAME;
import static org.craftercms.commons.lang.UrlUtils.RANGE_NO_END_HEADER_FORMAT;

/**
 * Implementation of {@link Resource} for Box files.
 *
 * @author avasquez
 */
public class BoxResource implements RangeAwareResource {

    private static final Logger logger = LoggerFactory.getLogger(BoxResource.class);

    private static final String FILE_INFO_SIZE_FIELD = "size";
    private static final String FILE_INFO_MODIFIED_AT_FIELD = "modified_at";
    private static final String FILE_INFO_NAME_FIELD = "name";

    private BoxAPIConnection apiConnection;
    private String fileId;

    public BoxResource(BoxAPIConnection apiConnection, String fileId) {
        this.apiConnection = apiConnection;
        this.fileId = fileId;
    }

    @Override
    public boolean exists() {
        try {
            getFileInfo(FILE_INFO_SIZE_FIELD).getSize();

            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);

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
        return getFileInfo(FILE_INFO_SIZE_FIELD).getSize();
    }

    @Override
    public long lastModified() throws IOException {
        return getFileInfo(FILE_INFO_MODIFIED_AT_FIELD).getModifiedAt().getTime();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new IOException(getDescription() + " doesn't support relative resources");
    }

    @Override
    public String getFilename() {
        try {
            return getFileInfo(FILE_INFO_NAME_FIELD).getName();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);

            return fileId;
        }
    }

    @Override
    public String getDescription() {
        return "BoxResource{" +
               "fileId='" + fileId + '\'' +
               '}';
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            BoxAPIRequest request = createGetContentRequest();
            BoxAPIResponse response = request.send();

            return new BoxApiResponseBodyWrapper(response.getBody(), response);
        } catch (BoxAPIException e) {
            throw new IOException("Error while trying to get content for " + getDescription(), e);
        }
    }

    @Override
    public InputStream getInputStream(long start, long end) throws IOException {
        try {
            BoxAPIRequest request = createGetContentRequest();

            if (end > 0) {
                request.addHeader(RANGE_HEADER_NAME, String.format(RANGE_HEADER_FORMAT, Long.toString(start),
                                                                   Long.toString(end)));
            } else {
                request.addHeader(RANGE_HEADER_NAME, String.format(RANGE_NO_END_HEADER_FORMAT, Long.toString(start)));
            }

            BoxAPIResponse response = request.send();

            return new BoxApiResponseBodyWrapper(response.getBody(), response);
        } catch (BoxAPIException e) {
            if (e.getResponseCode() == 404) {
                throw new FileNotFoundException(getDescription() + " not found");
            } else {
                throw new IOException("Error while trying to get content for " + getDescription(), e);
            }
        }
    }

    private BoxFile.Info getFileInfo(String... fields) throws IOException {
        try {
            return new BoxFile(apiConnection, fileId).getInfo(fields);
        } catch (BoxAPIException e) {
            if (e.getResponseCode() == 404) {
                throw new FileNotFoundException(getDescription() + " not found");
            } else {
                if (e.getResponseCode() == 404) {
                    throw new FileNotFoundException(getDescription() + " not found");
                } else {
                    throw new IOException("Error while trying to retrieve info for " + getDescription(), e);
                }
            }
        }
    }

    private BoxAPIRequest createGetContentRequest() {
        URL url = BoxFile.CONTENT_URL_TEMPLATE.build(apiConnection.getBaseURL(), fileId);

        return new BoxAPIRequest(apiConnection, url, "GET");
    }

    private static class BoxApiResponseBodyWrapper extends InputStream {

        private InputStream wrappedInputStream;
        private BoxAPIResponse apiResponse;

        public BoxApiResponseBodyWrapper(InputStream wrappedInputStream, BoxAPIResponse apiResponse) {
            this.wrappedInputStream = wrappedInputStream;
            this.apiResponse = apiResponse;
        }

        @Override
        public int read(byte[] b) throws IOException {
            return wrappedInputStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return wrappedInputStream.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return wrappedInputStream.skip(n);
        }

        @Override
        public int available() throws IOException {
            return wrappedInputStream.available();
        }

        @Override
        public synchronized void mark(int readlimit) {
            wrappedInputStream.mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            wrappedInputStream.reset();
        }

        @Override
        public boolean markSupported() {
            return wrappedInputStream.markSupported();
        }

        @Override
        public int read() throws IOException {
            return wrappedInputStream.read();
        }

        @Override
        public void close() {
            apiResponse.disconnect();
        }

    }

}
