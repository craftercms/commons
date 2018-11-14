package org.craftercms.commons.config.webdav;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.AbstractProfileConfigMapper;

/**
 * Configuration mapper for {@link WebDavProfile}s.
 *
 * @author joseross
 * @auhor avasquez
 */
public class WebDavProfileMappper extends AbstractProfileConfigMapper<WebDavProfile> {

    private static final String CONFIG_KEY_BASE_URL = "baseUrl";
    private static final String CONFIG_KEY_DELIVERY_URL = "deliveryBaseUrl";
    private static final String CONFIG_KEY_USERNAME = "username";
    private static final String CONFIG_KEY_PASSWORD = "password";

    @Override
    protected WebDavProfile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) {
        WebDavProfile profile = new WebDavProfile();
        profile.setBaseUrl(profileConfig.getString(CONFIG_KEY_BASE_URL));
        if(profileConfig.containsKey(CONFIG_KEY_DELIVERY_URL)) {
            profile.setDeliveryBaseUrl(profileConfig.getString(CONFIG_KEY_DELIVERY_URL));
        }
        profile.setUsername(profileConfig.getString(CONFIG_KEY_USERNAME));
        profile.setPassword(profileConfig.getString(CONFIG_KEY_PASSWORD));

        return profile;
    }

}
