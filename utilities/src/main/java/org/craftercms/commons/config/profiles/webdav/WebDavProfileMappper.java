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

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.profiles.AbstractProfileConfigMapper;

import static org.craftercms.commons.config.ConfigUtils.*;

/**
 * Configuration mapper for {@link WebDavProfile}s.
 *
 * @author joseross
 * @author avasquez
 */
public class WebDavProfileMappper extends AbstractProfileConfigMapper<WebDavProfile> {

    private static final String CONFIG_KEY_WEBDAV = "webdav";
    private static final String CONFIG_KEY_BASE_URL = "baseUrl";
    private static final String CONFIG_KEY_DELIVERY_URL = "deliveryBaseUrl";
    private static final String CONFIG_KEY_USERNAME = "username";
    private static final String CONFIG_KEY_PASSWORD = "password";

    public WebDavProfileMappper() {
        super(CONFIG_KEY_WEBDAV);
    }

    @Override
    protected WebDavProfile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException {
        WebDavProfile profile = new WebDavProfile();
        profile.setBaseUrl(getRequiredStringProperty(profileConfig, CONFIG_KEY_BASE_URL));
        if(profileConfig.containsKey(CONFIG_KEY_DELIVERY_URL)) {
            profile.setDeliveryBaseUrl(getStringProperty(profileConfig, CONFIG_KEY_DELIVERY_URL));
        }
        profile.setUsername(getRequiredStringProperty(profileConfig, CONFIG_KEY_USERNAME));
        profile.setPassword(getRequiredStringProperty(profileConfig, CONFIG_KEY_PASSWORD));

        return profile;
    }

}
