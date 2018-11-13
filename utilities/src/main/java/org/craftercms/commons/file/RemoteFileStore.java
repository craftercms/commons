package org.craftercms.commons.file;

import java.io.IOException;
import java.nio.file.Path;

public interface RemoteFileStore {

    Path downloadFile(String path) throws IOException;

}
