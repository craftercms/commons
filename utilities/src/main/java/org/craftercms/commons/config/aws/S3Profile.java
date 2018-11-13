package org.craftercms.commons.config.aws;

/**
 * Holds the information to connect to AWS S3.
 *
 * @author joseross
 */
public class S3Profile extends AbstractAwsProfile {

    /**
     * Name of the bucket.
     */
    protected String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

}