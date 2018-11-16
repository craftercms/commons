package org.craftercms.commons.file.stores.impl.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.FileUtils;
import org.craftercms.commons.config.aws.S3Profile;
import org.craftercms.commons.file.stores.S3Utils;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to S3.
 *
 * @author avasquez
 */
public class S3FileStore extends AbstractProfileAwareRemoteFileStore<S3Profile> {


    @Override
    protected void doDownload(S3Profile profile, String remoteId, Path downloadPath) throws IOException {
        try {
            AmazonS3 s3Client = S3Utils.createClient(profile);
            S3Object s3Object = s3Client.getObject(profile.getBucketName(), remoteId);

            FileUtils.copyInputStreamToFile(s3Object.getObjectContent(), downloadPath.toFile());
        } catch (Exception e) {
            throw new IOException("Error while downloading S3 file " + remoteId);
        }
    }

}
