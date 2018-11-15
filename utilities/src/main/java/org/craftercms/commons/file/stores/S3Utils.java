package org.craftercms.commons.file.stores;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.aws.S3Profile;

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
    public static final AmazonS3 createClient(S3Profile profile) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                                                             .withCredentials(profile.getCredentialsProvider());
        if(StringUtils.isNotEmpty(profile.getRegion())) {
            builder.withRegion(profile.getRegion());
        }

        return builder.build();
    }

}
