package org.craftercms.commons.config.webdav;

import org.craftercms.commons.config.ConfigurationProfile;

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
     */
    public String getDeliveryBaseUrl() {
        return deliveryBaseUrl;
    }

    /**
     * Sets the base url to generate asset urls.
     */
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
