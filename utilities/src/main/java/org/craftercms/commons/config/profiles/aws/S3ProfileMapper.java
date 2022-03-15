/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.config.profiles.aws;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationResolver;

import java.beans.ConstructorProperties;

import static org.craftercms.commons.config.ConfigUtils.*;

/**
 * Configuration mapper for {@link S3Profile}s.
 *
 * @author joseross
 * @author avasquez
 */
public class S3ProfileMapper extends AbstractAwsProfileMapper<S3Profile> {

    private static final String CONFIG_KEY_S3 = "s3";
    private static final String CONFIG_KEY_BUCKET = "bucketName";
    private static final String CONFIG_KEY_PREFIX = "prefix";
    private static final String CONFIG_KEY_PATH_STYLE = "pathStyleAccess";

    @ConstructorProperties({"configurationResolver"})
    public S3ProfileMapper(ConfigurationResolver configurationResolver) {
        super(CONFIG_KEY_S3, configurationResolver);
    }

    @Override
    protected S3Profile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig)
            throws ConfigurationException {
        S3Profile profile = super.mapProfile(profileConfig);
        profile.setBucketName(getStringProperty(profileConfig, CONFIG_KEY_BUCKET));
        profile.setPrefix(getStringProperty(profileConfig, CONFIG_KEY_PREFIX));
        profile.setPathStyleAccessEnabled(getBooleanProperty(profileConfig, CONFIG_KEY_PATH_STYLE, false));

        return profile;
    }

    @Override
    protected AbstractAwsProfile createProfile() {
        return new S3Profile();
    }

}
