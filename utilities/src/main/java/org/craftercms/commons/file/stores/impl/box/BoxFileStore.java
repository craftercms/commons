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
package org.craftercms.commons.file.stores.impl.box;

import org.apache.commons.io.FilenameUtils;
import org.craftercms.commons.config.profiles.box.BoxProfile;
import org.craftercms.commons.file.stores.BoxUtils;
import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;
import org.craftercms.commons.file.stores.impl.ProfileAwareRemotePath;
import org.craftercms.commons.file.stores.impl.ResourceBasedRemoteFile;
import org.craftercms.commons.spring.resources.BoxResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * A {@link org.craftercms.commons.file.stores.RemoteFileStore} to Box.
 *
 * @author avasquez
 */
public class BoxFileStore extends AbstractProfileAwareRemoteFileStore<BoxProfile> {

    @Override
    protected RemoteFile doGetFile(ProfileAwareRemotePath path, BoxProfile profile) throws IOException {
        String fileId = FilenameUtils.getBaseName(path.getPath());
        Resource resource = new BoxResource(BoxUtils.createConnection(profile), fileId);

        return new ResourceBasedRemoteFile(path, resource);
    }

}
