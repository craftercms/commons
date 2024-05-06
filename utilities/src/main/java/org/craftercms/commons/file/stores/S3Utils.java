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

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.profiles.aws.AbstractAwsProfile;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

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
    public static final S3Client createClient(AbstractAwsProfile profile, boolean useCustomEndpoint) {
        S3ClientBuilder builder = S3Client.builder()
                .credentialsProvider(profile.getCredentialsProvider());

        if (useCustomEndpoint && StringUtils.isNotEmpty(profile.getEndpoint()) &&
            StringUtils.isNotEmpty(profile.getRegion())) {
            builder.endpointOverride(URI.create(profile.getEndpoint()))
                    .region(Region.of(profile.getRegion()));
            if (profile instanceof S3Profile) {
                builder.serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(((S3Profile) profile).isPathStyleAccessEnabled())
                        .build());
            }
        } else if (StringUtils.isNotEmpty(profile.getRegion())) {
            builder.region(Region.of(profile.getRegion()));
        }

        return builder.build();
    }

    public static final S3Client createClient(AbstractAwsProfile profile) {
        return createClient(profile, true);
    }

}
