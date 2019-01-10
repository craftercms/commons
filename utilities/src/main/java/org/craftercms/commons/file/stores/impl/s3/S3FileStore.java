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
package org.craftercms.commons.file.stores.impl.s3;

import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.S3Utils;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;
import org.craftercms.commons.file.stores.impl.ProfileAwareRemotePath;
import org.craftercms.commons.file.stores.impl.ResourceBasedRemoteFile;
import org.craftercms.commons.spring.resources.S3Resource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to S3.
 *
 * @author avasquez
 */
public class S3FileStore extends AbstractProfileAwareRemoteFileStore<S3Profile> {

    @Override
    protected RemoteFile doGetFile(ProfileAwareRemotePath path, S3Profile profile) throws IOException {
        Resource resource = new S3Resource(S3Utils.createClient(profile), profile.getBucketName(), path.getPath());

        return new ResourceBasedRemoteFile(path, resource);
    }

}
