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

import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import static org.craftercms.commons.lang.UrlUtils.*;

/**
 * {@link UrlResource} extension that implements {@link RangeAwareResource} in order to provide
 * random access to content (range content queries).
 *
 * @author avasquez
 */
public class RangeAwareUrlResource extends UrlResource implements RangeAwareResource {

    public RangeAwareUrlResource(URI uri) throws MalformedURLException {
        super(uri);
    }

    public RangeAwareUrlResource(URL url) {
        super(url);
    }

    public RangeAwareUrlResource(String path) throws MalformedURLException {
        super(path);
    }

    public RangeAwareUrlResource(String protocol, String location) throws MalformedURLException {
        super(protocol, location);
    }

    public RangeAwareUrlResource(String protocol, String location, String fragment) throws MalformedURLException {
        super(protocol, location, fragment);
    }

    @Override
    public InputStream getInputStream(long start, long end) throws IOException {
        URLConnection conn = getURL().openConnection();
        conn.setDefaultUseCaches(false);

        if (end > 0) {
            conn.setRequestProperty(RANGE_HEADER_NAME, String.format(RANGE_HEADER_FORMAT, Long.toString(start),
                                                                     Long.toString(end)));
        } else {
            conn.setRequestProperty(RANGE_HEADER_NAME, String.format(RANGE_NO_END_HEADER_FORMAT, Long.toString(start)));
        }

        try {
            return conn.getInputStream();
        } catch (IOException ex) {
            // Close the HTTP connection (if applicable).
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
            throw ex;
        }
    }

}
