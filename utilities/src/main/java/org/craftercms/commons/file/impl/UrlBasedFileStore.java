package org.craftercms.commons.file.impl;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class UrlBasedFileStore extends AbstractRemoteFileStore {

    @Override
    protected void doDownload(String remotePath, Path downloadPath) throws IOException {
        FileUtils.copyURLToFile(new URL(remotePath), downloadPath.toFile());
    }

}
