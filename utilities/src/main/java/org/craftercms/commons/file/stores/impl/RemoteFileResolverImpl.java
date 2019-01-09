/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.file.stores.impl;

import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.RemoteFileResolver;
import org.craftercms.commons.file.stores.RemoteFileStore;
import org.craftercms.commons.file.stores.RemotePath;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link RemoteFileResolver}. Uses {@link RemotePathParser} to parse the path and determine
 * the store type, and then resolves the file using the actual {@link RemoteFileStore}.
 *
 * @author avasquez
 */
public class RemoteFileResolverImpl implements RemoteFileResolver {

    protected Map<String, RemotePathParser> pathParsers;
    protected Map<String, RemoteFileStore> stores;

    @Required
    public void setPathParsers(Map<String, RemotePathParser> pathParsers) {
        this.pathParsers = pathParsers;
    }

    @Required
    public void setStores(Map<String, RemoteFileStore> stores) {
        this.stores = stores;
    }

    @Override
    public RemoteFile resolve(String path) throws IOException, IllegalArgumentException {
        RemotePath remotePath = null;

        for (Map.Entry<String, RemotePathParser> entry : pathParsers.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(path);

            if (matcher.matches()) {
                remotePath = entry.getValue().parse(path, matcher);
                break;
            }
        }

        if (remotePath != null) {
            RemoteFileStore store = stores.get(remotePath.getStoreType());
            if (store != null) {
                return store.getFile(remotePath);
            } else {
                throw new IllegalArgumentException("Store type '" + remotePath.getStoreType() + "' couldn't be matched " +
                                                   "to any of the known remote file stores: " + stores.keySet());
            }
        } else {
            throw new IllegalArgumentException("Path " + path + " is not a supported remote file");
        }
    }

}
