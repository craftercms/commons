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
package org.craftercms.commons.config.profiles.webdav;

import org.craftercms.commons.config.profiles.ConfigurationProfile;

/**
 * Holds the necessary information to connect to a WebDAV server.
 *
 * @author joseross
 */
public class WebDavProfile extends ConfigurationProfile {

    protected String baseUrl;
    protected String deliveryBaseUrl;
    protected String username;
    protected String password;

    /**
     * Returns the base url of the webdav server.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url of the webdav server.
     */
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns the base url to generate asset urls.
     * @deprecated The new service will ignore this property and use a /remote-assets/... URL instead
     */
    @Deprecated
    public String getDeliveryBaseUrl() {
        return deliveryBaseUrl;
    }

    /**
     * Sets the base url to generate asset urls.
     * @deprecated The new service will ignore this property and use a /remote-assets/... URL instead
     */
    @Deprecated
    public void setDeliveryBaseUrl(final String deliveryBaseUrl) {
        this.deliveryBaseUrl = deliveryBaseUrl;
    }

    /**
     * Returns the username used to connect to the server.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username used to connect to the server.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the password used to connect to the server.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password used to connect to the server.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

}
