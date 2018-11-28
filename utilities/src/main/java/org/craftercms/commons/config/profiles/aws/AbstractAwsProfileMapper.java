/*
 * Copyright (C) 2007-2018 Crafter Software Corporation.
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
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.profiles.AbstractProfileConfigMapper;
import org.craftercms.commons.config.ConfigurationException;

/**
 * Base class for configuration mappers that map to {@link AbstractAwsProfile}s.
 *
 * @author joseross
 * @auhor avasquez
 */
public abstract class AbstractAwsProfileMapper<T extends AbstractAwsProfile> extends AbstractProfileConfigMapper<T> {

    private static final String CONFIG_KEY_REGION = "region";
    private static final String CONFIG_KEY_ACCESS_KEY = "credentials.accessKey";
    private static final String CONFIG_KEY_SECRET_KEY = "credentials.secretKey";

    @Override
    @SuppressWarnings("unchecked")
    protected T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException {
        AbstractAwsProfile profile = createProfile();
        if (profileConfig.containsKey(CONFIG_KEY_ACCESS_KEY) && profileConfig.containsKey(CONFIG_KEY_SECRET_KEY)) {
            String accessKey = profileConfig.getString(CONFIG_KEY_ACCESS_KEY);
            String secretKey = profileConfig.getString(CONFIG_KEY_SECRET_KEY);

            profile.setCredentialsProvider(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
        } else {
            profile.setCredentialsProvider(new DefaultAWSCredentialsProviderChain());
        }

        if (profileConfig.containsKey(CONFIG_KEY_REGION)) {
            profile.setRegion(profileConfig.getString(CONFIG_KEY_REGION));
        }

        return (T) profile;
    }

    protected abstract AbstractAwsProfile createProfile();

}
