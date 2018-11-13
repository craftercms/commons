package org.craftercms.commons.file.impl;

import org.craftercms.commons.file.RemoteFileStore;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class RegexMappedCompositeFileStore implements RemoteFileStore {

    private Map<String, RemoteFileStore> regexMappedFileStores;

    @Required
    public void setRegexMappedFileStores(Map<String, RemoteFileStore> regexMappedFileStores) {
        this.regexMappedFileStores = regexMappedFileStores;
    }

    @Override
    public Path downloadFile(String path) throws IOException {
        for (Map.Entry<String, RemoteFileStore> entry : regexMappedFileStores.entrySet()) {
            if (path.matches(entry.getKey())) {
                return entry.getValue().downloadFile(path);
            }
        }

        throw new IllegalArgumentException("Path " + path + " can't be mapped to a remote file store");
    }

}
