package org.craftercms.commons.config.aws;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;

/**
 * Configuration mapper for {@link S3Profile}s.
 *
 * @author joseross
 * @auhor avasquez
 */
public class S3ProfileMapper extends AbstractAwsProfileMapper<S3Profile> {

    private static final String CONFIG_KEY_BUCKET = "bucketName";

    @Override
    protected S3Profile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig)
            throws ConfigurationException {
        S3Profile profile = super.mapProfile(profileConfig);
        profile.setBucketName(profileConfig.getString(CONFIG_KEY_BUCKET));

        return profile;
    }

    @Override
    protected AbstractAwsProfile createProfile() {
        return new S3Profile();
    }

}
