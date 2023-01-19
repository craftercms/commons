/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.proxy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class ProxyUtils {
    public static final List<String> IGNORE_REQUEST_HEADERS = Arrays.asList("authorization", "x-xsrf-token");
    public static final List<String> IGNORE_REQUEST_COOKIES = Arrays.asList("xsrf-token", "jsessionid", "refresh_token");

    /**
     * Rebuild cookie header to filter out cookies should be ignored by the proxy
     * @param request current request
     * @return cookies' header string
     */
    public static String getProxyCookieHeader(HttpServletRequest request) {
        StringBuilder cookieBuilder = new StringBuilder();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (!IGNORE_REQUEST_COOKIES.contains(cookie.getName().toLowerCase())) {
                if (cookieBuilder.length() == 0) {
                    cookieBuilder.append(format("%s=%s", cookie.getName(), cookie.getValue()));
                } else {
                    cookieBuilder.append(format("; %s=%s", cookie.getName(), cookie.getValue()));
                }
            }
        }

        return cookieBuilder.toString();
    }
}