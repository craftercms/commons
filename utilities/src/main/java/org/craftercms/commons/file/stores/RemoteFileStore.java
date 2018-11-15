package org.craftercms.commons.file.stores;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface for "proxies" to remote file stores.
 *
 * @author avasquez
 */
public interface RemoteFileStore {

    /**
     * Downloads the remote file for the given ID. The downloaded file is temporary, so it should be deleted after
     * being used.
     *
     * @param id the ID of the file
     *
     * @return the path to the downloaded file
     *
     * @throws IOException if an error occurs while accessing file
     */
    Path downloadFile(String id) throws IOException;

}
