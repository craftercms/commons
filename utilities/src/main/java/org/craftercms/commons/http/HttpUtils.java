/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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
package org.craftercms.commons.http;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility methods for HTTP related stuff.
 *
 * @author avasquez
 */
public class HttpUtils {

    public static final String HTTP_SCHEME =        "http";
    public static final String HTTPS_SCHEME =       "https";
    public static final int DEFAULT_HTTP_PORT =     80;
    public static final int DEFAULT_HTTPS_PORT =    443;

    /**
     * Returns the portion from the URL that includes the scheme, server name and port number, without the server
     * path.
     *
     * @param request       the request object used to build the base URL
     * @param forceHttps    if HTTPS should be enforced
     *
     * @return the base request URL
     */
    public static final StringBuilder getBaseRequestUrl(HttpServletRequest request, boolean forceHttps) {
        String scheme = forceHttps ? HTTPS_SCHEME : request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        StringBuilder url = new StringBuilder();

        url.append(scheme).append("://").append(serverName);

        if (!(scheme.equals(HTTP_SCHEME) && serverPort == DEFAULT_HTTP_PORT) &&
            !(scheme.equals(HTTPS_SCHEME) && serverPort == DEFAULT_HTTPS_PORT)) {
            url.append(":").append(serverPort);
        }

        return url;
    }

}
