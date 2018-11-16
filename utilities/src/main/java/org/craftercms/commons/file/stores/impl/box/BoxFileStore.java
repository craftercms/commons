package org.craftercms.commons.file.stores.impl.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import org.craftercms.commons.config.box.BoxProfile;
import org.craftercms.commons.file.stores.BoxUtils;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to Box.
 *
 * @author avasquez
 */
public class BoxFileStore extends AbstractProfileAwareRemoteFileStore<BoxProfile> {

    @Override
    protected void doDownload(BoxProfile profile, String remoteId, Path downloadPath) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(downloadPath.toFile()))) {
            BoxAPIConnection connection = BoxUtils.createConnection(profile);
            BoxFile remoteFile = new BoxFile(connection, remoteId);

            remoteFile.download(out);
        } catch (Exception e) {
            throw new IOException("Error while downloading Box file " + remoteId);
        }
    }

}
