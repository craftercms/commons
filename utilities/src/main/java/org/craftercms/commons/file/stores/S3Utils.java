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
package org.craftercms.commons.file.stores;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.profiles.aws.AbstractAwsProfile;
import org.craftercms.commons.config.profiles.aws.S3Profile;

/**
 * Utility methods for S3.
 *
 * @author avasquez
 */
public class S3Utils {

    /**
     * Creates an {@code AmazonS3} based on the given profile config.
     *
     * @param profile the configuration profile
     *
     * @return a client to an Amazon S3 account
     */
    public static final AmazonS3 createClient(AbstractAwsProfile profile, boolean useCustomEndpoint) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                                                             .withCredentials(profile.getCredentialsProvider());

        if (useCustomEndpoint && StringUtils.isNotEmpty(profile.getEndpoint()) &&
            StringUtils.isNotEmpty(profile.getRegion())) {
            builder.withEndpointConfiguration(
                new AmazonS3ClientBuilder.EndpointConfiguration(profile.getEndpoint(), profile.getRegion()));
            if (profile instanceof S3Profile) {
                builder.withPathStyleAccessEnabled(((S3Profile) profile).isPathStyleAccessEnabled());
            }
        } else if (StringUtils.isNotEmpty(profile.getRegion())) {
            builder.withRegion(profile.getRegion());
        }

        return builder.build();
    }

    public static final AmazonS3 createClient(AbstractAwsProfile profile) {
        return createClient(profile, true);
    }

}
