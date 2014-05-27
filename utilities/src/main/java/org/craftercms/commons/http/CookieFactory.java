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

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to create cookies with common domain, path and max age.
 *
 * @author avasquez
 */
public class CookieFactory {

    private String domain;
    private String path;
    private Integer maxAge;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);

        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotEmpty(path)) {
            cookie.setPath(path);
        }
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }

        return cookie;
    }

}
