package org.craftercms.commons.config;

/**
 * Represents a collection of configuration properties that are commonly used together, for example, to open a
 * connection or accessing a server.
 *
 * @author avasquez
 */
public class ConfigurationProfile {

    private String profileId;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

}
