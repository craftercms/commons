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
package org.craftercms.commons.config.profiles.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.profiles.AbstractProfileConfigMapper;
import org.craftercms.commons.config.ConfigurationException;

import static org.craftercms.commons.config.ConfigUtils.*;

/**
 * Base class for configuration mappers that map to {@link AbstractAwsProfile}s.
 *
 * @author joseross
 * @author avasquez
 */
public abstract class AbstractAwsProfileMapper<T extends AbstractAwsProfile> extends AbstractProfileConfigMapper<T> {

    private static final String CONFIG_KEY_REGION = "region";
    private static final String CONFIG_KEY_ACCESS_KEY = "credentials.accessKey";
    private static final String CONFIG_KEY_SECRET_KEY = "credentials.secretKey";
    private static final String CONFIG_KEY_ENDPOINT = "endpoint";

    public AbstractAwsProfileMapper(final String serviceName) {
        super(serviceName);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException {
        AbstractAwsProfile profile = createProfile();
        if (profileConfig.containsKey(CONFIG_KEY_ACCESS_KEY) && profileConfig.containsKey(CONFIG_KEY_SECRET_KEY)) {
            String accessKey = getStringProperty(profileConfig, CONFIG_KEY_ACCESS_KEY);
            String secretKey = getStringProperty(profileConfig, CONFIG_KEY_SECRET_KEY);

            profile.setCredentialsProvider(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
        }

        if (profileConfig.containsKey(CONFIG_KEY_REGION)) {
            profile.setRegion(getStringProperty(profileConfig, CONFIG_KEY_REGION));
        }

        if (profileConfig.containsKey(CONFIG_KEY_ENDPOINT)) {
            profile.setEndpoint(getStringProperty(profileConfig, CONFIG_KEY_ENDPOINT));
        }

        return (T) profile;
    }

    protected abstract AbstractAwsProfile createProfile();

}
