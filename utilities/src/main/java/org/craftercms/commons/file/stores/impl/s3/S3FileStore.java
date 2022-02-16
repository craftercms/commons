/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.file.stores.impl.s3;

import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.aws.S3ClientCachingFactory;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;
import org.craftercms.commons.file.stores.impl.ProfileAwareRemotePath;
import org.craftercms.commons.file.stores.impl.ResourceBasedRemoteFile;
import org.craftercms.commons.spring.resources.S3Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to S3.
 *
 * @author avasquez
 */
public class S3FileStore extends AbstractProfileAwareRemoteFileStore<S3Profile> {

    protected S3ClientCachingFactory clientFactory;

    @Required
    public void setClientFactory(S3ClientCachingFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected RemoteFile doGetFile(ProfileAwareRemotePath path, S3Profile profile) throws IOException {
        Resource resource = new S3Resource(clientFactory, profile, path.getPath());

        return new ResourceBasedRemoteFile(path, resource);
    }

}
