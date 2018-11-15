package org.craftercms.commons.file.stores.impl;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Simple {@link org.craftercms.commons.file.stores.RemoteFileStore} where the IDs are basically URLs from where
 * the files can be downloaded.
 *
 * @author avasquez
 */
public class UrlBasedFileStore extends AbstractRemoteFileStore {

    @Override
    protected void doDownload(String remoteId, Path downloadPath) throws IOException {
        FileUtils.copyURLToFile(new URL(remoteId), downloadPath.toFile());
    }

}
