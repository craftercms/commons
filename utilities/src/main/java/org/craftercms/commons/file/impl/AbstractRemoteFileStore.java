package org.craftercms.commons.file.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.file.RemoteFileStore;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractRemoteFileStore implements RemoteFileStore {

    private static final String DEFAULT_TEMP_FOLDER_PREFIX = "downloads";

    protected String tempFolderPrefix;
    protected Path downloadsFolder;

    public void setTempFolderPrefix(String tempFolderPrefix) {
        this.tempFolderPrefix = tempFolderPrefix;
    }

    public void setDownloadsFolder(Path downloadsFolder) {
        this.downloadsFolder = downloadsFolder;
    }

    @PostConstruct
    public void init() throws IOException {
        if (StringUtils.isEmpty(tempFolderPrefix)) {
            tempFolderPrefix = DEFAULT_TEMP_FOLDER_PREFIX;
        }
        if (downloadsFolder == null) {
            downloadsFolder = Files.createTempDirectory(tempFolderPrefix);
        }
    }

    @Override
    public Path downloadFile(String path) throws IOException {
        Path downloadPath = Files.createTempFile(downloadsFolder, FilenameUtils.getBaseName(path), null);

        doDownload(path, downloadPath);

        return downloadPath;
    }

    protected abstract void doDownload(String remotePath, Path downloadPath) throws IOException;

}
