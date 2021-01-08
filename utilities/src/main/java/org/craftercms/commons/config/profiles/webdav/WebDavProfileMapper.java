/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.config.profiles.webdav;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationResolver;
import org.craftercms.commons.config.profiles.AbstractProfileConfigMapper;

import static org.craftercms.commons.config.ConfigUtils.*;

/**
 * Configuration mapper for {@link WebDavProfile}s.
 *
 * @author joseross
 * @author avasquez
 */
public class WebDavProfileMapper extends AbstractProfileConfigMapper<WebDavProfile> {

    private static final String CONFIG_KEY_WEBDAV = "webdav";
    private static final String CONFIG_KEY_BASE_URL = "baseUrl";
    private static final String CONFIG_KEY_DELIVERY_URL = "deliveryBaseUrl";
    private static final String CONFIG_KEY_USERNAME = "username";
    private static final String CONFIG_KEY_PASSWORD = "password";
    private static final String CONFIG_KEY_PREEMPTIVE_AUTH = "preemptiveAuth";

    public WebDavProfileMapper(ConfigurationResolver configurationResolver) {
        super(CONFIG_KEY_WEBDAV, configurationResolver);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected WebDavProfile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException {
        WebDavProfile profile = new WebDavProfile();
        profile.setBaseUrl(getRequiredStringProperty(profileConfig, CONFIG_KEY_BASE_URL));
        if (profileConfig.containsKey(CONFIG_KEY_DELIVERY_URL)) {
            profile.setDeliveryBaseUrl(getStringProperty(profileConfig, CONFIG_KEY_DELIVERY_URL));
        }
        profile.setUsername(getRequiredStringProperty(profileConfig, CONFIG_KEY_USERNAME));
        profile.setPassword(getRequiredStringProperty(profileConfig, CONFIG_KEY_PASSWORD));

        if (profileConfig.containsKey(CONFIG_KEY_PREEMPTIVE_AUTH)) {
            profile.setPreemptiveAuth(getBooleanProperty(profileConfig, CONFIG_KEY_PREEMPTIVE_AUTH));
        }

        return profile;
    }

}
