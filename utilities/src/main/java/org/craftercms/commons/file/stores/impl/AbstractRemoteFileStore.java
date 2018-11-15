package org.craftercms.commons.file.stores.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.file.stores.RemoteFileStore;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Base class for {@link RemoteFileStore}s. Uses a temporary download folder to keep the downloaded files.
 *
 * @author avasquez.
 */
public abstract class AbstractRemoteFileStore implements RemoteFileStore {

    private static final String DEFAULT_TEMP_FOLDER_PREFIX = "downloads";

    protected String tempFolderPrefix;
    protected Path downloadsFolder;

    /**
     * Sets the prefix of the temporary download folder.
     */
    public void setTempFolderPrefix(String tempFolderPrefix) {
        this.tempFolderPrefix = tempFolderPrefix;
    }

    /**
     * Sets the specific download folder (ignored if there's a prefix).
     */
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

    @PreDestroy
    public void destroy() throws IOException {
        Files.delete(downloadsFolder);
    }

    @Override
    public Path downloadFile(String id) throws IOException {
        Path downloadPath = Files.createTempFile(downloadsFolder, FilenameUtils.getBaseName(id), null);

        doDownload(id, downloadPath);

        return downloadPath;
    }

    protected abstract void doDownload(String remoteId, Path downloadPath) throws IOException;

}
