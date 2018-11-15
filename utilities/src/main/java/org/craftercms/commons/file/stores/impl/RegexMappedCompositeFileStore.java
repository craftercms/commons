package org.craftercms.commons.file.stores.impl;

import org.craftercms.commons.file.stores.RemoteFileStore;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Composite {@link RemoteFileStore} that matches and ID to a store based on a regex.
 *
 * @author avasquez
 */
public class RegexMappedCompositeFileStore implements RemoteFileStore {

    private Map<String, RemoteFileStore> regexMappedFileStores;

    @Required
    public void setRegexMappedFileStores(Map<String, RemoteFileStore> regexMappedFileStores) {
        this.regexMappedFileStores = regexMappedFileStores;
    }

    @Override
    public Path downloadFile(String id) throws IOException {
        for (Map.Entry<String, RemoteFileStore> entry : regexMappedFileStores.entrySet()) {
            if (id.matches(entry.getKey())) {
                return entry.getValue().downloadFile(id);
            }
        }

        throw new IllegalArgumentException("File ID " + id + " can't be mapped to a remote file store");
    }

}
