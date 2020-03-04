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
package org.craftercms.commons.config.profiles;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationMapper;
import org.craftercms.commons.config.EncryptionAwareConfigurationReader;

import java.io.InputStream;
import java.util.List;

import static org.craftercms.commons.config.ConfigUtils.getRequiredStringProperty;

/**
 * Base class for configuration mappers that read configuration profiles and map them to profile classes.
 *
 * @author avasquez
 */
public abstract class AbstractProfileConfigMapper<T extends ConfigurationProfile> implements ConfigurationMapper<T> {

    private static final String CONFIG_KEY_PROFILE = "profile";
    private static final String CONFIG_KEY_ID = "id";

    protected String serviceName;

    protected EncryptionAwareConfigurationReader configurationReader;

    public AbstractProfileConfigMapper(final String serviceName,
                                       final EncryptionAwareConfigurationReader configurationReader) {
        this.serviceName = serviceName;
        this.configurationReader = configurationReader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T readConfig(InputStream inputStream, String encoding, String profileId) throws ConfigurationException {
        HierarchicalConfiguration<ImmutableNode> config =
            (HierarchicalConfiguration<ImmutableNode>) configurationReader.readXmlConfiguration(inputStream, encoding);

        List<HierarchicalConfiguration<ImmutableNode>> profiles =
            config.configurationsAt(serviceName + "." + CONFIG_KEY_PROFILE);
        HierarchicalConfiguration<ImmutableNode> profileConfig = profiles
                .stream()
                .filter(c -> profileId.equals(c.getString(CONFIG_KEY_ID)))
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("Profile '" + profileId + "' not found"));

        T profile = processConfig(profileConfig);
        profile.setProfileId(profileId);
        return profile;
    }

    @Override
    public T processConfig(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException {
        return mapProfile(config);
    }

    protected abstract T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException;

}
