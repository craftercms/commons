package org.craftercms.commons.file.stores.impl.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.FileUtils;
import org.craftercms.commons.config.aws.S3Profile;
import org.craftercms.commons.file.stores.S3Utils;
import org.craftercms.commons.file.stores.impl.AbstractRemoteFileStore;
import org.springframework.beans.factory.ObjectFactory;

import java.io.*;
import java.nio.file.Path;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to S3.
 *
 * @author avasquez
 */
public class S3FileStore extends AbstractRemoteFileStore {

    private ObjectFactory<S3Profile> s3ProfileLoader;

    public S3FileStore(ObjectFactory<S3Profile> s3ProfileLoader) {
        this.s3ProfileLoader = s3ProfileLoader;
    }

    @Override
    protected void doDownload(String remoteId, Path downloadPath) throws IOException {
        try {
            S3Profile profile = s3ProfileLoader.getObject();
            AmazonS3 s3Client = S3Utils.createClient(profile);
            S3Object s3Object = s3Client.getObject(profile.getBucketName(), remoteId);

            FileUtils.copyInputStreamToFile(s3Object.getObjectContent(), downloadPath.toFile());
        } catch (Exception e) {
            throw new IOException("Error while downloading S3 file " + remoteId);
        }
    }

}
