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

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.profiles.ConfigurationProfile;
import org.craftercms.commons.config.profiles.ConfigurationProfileLoader;
import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.RemoteFileStore;
import org.craftercms.commons.file.stores.RemotePath;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;

/**
 * Base {@link RemoteFileStore} for stores that are aware of configuration profiles that should be used to connect
 * to the remote store.
 *
 * @author avasquez
 */
public abstract class AbstractProfileAwareRemoteFileStore<T extends ConfigurationProfile> implements RemoteFileStore {

    protected ConfigurationProfileLoader<T> profileLoader;

    @Required
    public void setProfileLoader(ConfigurationProfileLoader<T> profileLoader) {
        this.profileLoader = profileLoader;
    }

    @Override
    public RemoteFile getFile(RemotePath path) throws IOException {
        if (path instanceof ProfileAwareRemotePath) {
            ProfileAwareRemotePath p = (ProfileAwareRemotePath) path;

            return doGetFile(p, loadProfile(p.getProfile()));
        } else {
            throw new IllegalArgumentException(path + " expected to be an instance of " + ProfileAwareRemotePath.class);
        }
    }

    protected abstract RemoteFile doGetFile(ProfileAwareRemotePath path, T profile) throws IOException;

    protected T loadProfile(String profile) throws IOException {
        try {
            return profileLoader.loadProfile(profile);
        } catch (ConfigurationException e) {
            throw new IOException("Unable to load configuration profile with ID " + profile, e);
        }
    }

}
