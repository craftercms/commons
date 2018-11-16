package org.craftercms.commons.config.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import org.craftercms.commons.config.ConfigurationProfile;

/**
 * Holds the basic information required by all AWS connections.
 *
 * @author joseross
 */
public abstract class AbstractAwsProfile extends ConfigurationProfile {

    /**
     * Provides the credentials to authenticate in AWS services.
     */
    private AWSCredentialsProvider credentialsProvider;

    /**
     * Region to use in AWS services.
     */
    private String region;

    public AWSCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(final AWSCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}