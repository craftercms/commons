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

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.i10n.I10nLogger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility class to create and delete cookies with common domain, path and max age.
 *
 * @author avasquez
 */
public class CookieManager {

    private static final I10nLogger logger = new I10nLogger(CookieManager.class, "crafter.commons.messages.logging");

    public static final String LOG_KEY_ADDED_COOKIE =   "http.cookie.addedCookie";
    public static final String LOG_KEY_DELETED_COOKIE = "http.cookie.deletedCookie";

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

    /**
     * Add a new cookie, using the configured domain, path and max age, to the response.
     *
     * @param name  the name of the cookie
     * @param value the value of the cookie
     */
    public void addCookie(String name, String value, HttpServletResponse response) {
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

        response.addCookie(cookie);

        logger.debug(LOG_KEY_ADDED_COOKIE, name);
    }

    /**
     * Add a "delete" cookie to the response to indicate the that the stored cookie should be deleted.
     *
     * @param name the name of the cookie
     */
    public void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);

        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotEmpty(path)) {
            cookie.setPath(path);
        }

        cookie.setMaxAge(0);

        response.addCookie(cookie);

        logger.debug(LOG_KEY_DELETED_COOKIE, name);
    }

}
