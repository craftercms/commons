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
package org.craftercms.commons.config.profiles;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigUtils;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationMapper;

import java.io.InputStream;
import java.util.List;

/**
 * Base class for configuration mappers that read configuration profiles and map them to profile classes.
 *
 * @author avasquez
 */
public abstract class AbstractProfileConfigMapper<T extends ConfigurationProfile> implements ConfigurationMapper<T> {

    private static final String CONFIG_KEY_PROFILE = "profile";
    private static final String CONFIG_KEY_ID = "id";

    protected String serviceName;

    public AbstractProfileConfigMapper(final String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T readConfig(InputStream inputStream, String encoding, String profileId) throws ConfigurationException {
        HierarchicalConfiguration<ImmutableNode> config = ConfigUtils.readXmlConfiguration(inputStream, encoding);

        List<HierarchicalConfiguration<ImmutableNode>> profiles =
            config.configurationsAt(serviceName + "." + CONFIG_KEY_PROFILE);
        HierarchicalConfiguration profileConfig = profiles
                .stream()
                .filter(c -> profileId.equals(c.getString(CONFIG_KEY_ID)))
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("Profile '" + profileId + "' not found"));

        ConfigurationProfile profile = mapProfile(profileConfig);
        profile.setProfileId(profileId);

        return (T) profile;
    }

    protected abstract T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException;

}
