package org.craftercms.commons.file.stores.impl.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import org.craftercms.commons.config.box.BoxProfile;
import org.craftercms.commons.file.stores.BoxUtils;
import org.craftercms.commons.file.stores.impl.AbstractRemoteFileStore;
import org.springframework.beans.factory.ObjectFactory;

import java.io.*;
import java.nio.file.Path;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to Box.
 *
 * @author avasquez
 */
public class BoxFileStore extends AbstractRemoteFileStore {

    private ObjectFactory<BoxProfile> boxProfileLoader;

    public BoxFileStore(ObjectFactory<BoxProfile> boxProfileLoader) {
        this.boxProfileLoader = boxProfileLoader;
    }

    @Override
    protected void doDownload(String remoteId, Path downloadPath) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(downloadPath.toFile()))) {
            BoxProfile profile = boxProfileLoader.getObject();
            BoxAPIConnection connection = BoxUtils.createConnection(profile);
            BoxFile remoteFile = new BoxFile(connection, remoteId);

            remoteFile.download(out);
        } catch (Exception e) {
            throw new IOException("Error while downloading Box file " + remoteId);
        }
    }

}
