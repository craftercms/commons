/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.file.stores.impl.webdav;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.profiles.webdav.WebDavProfile;
import org.craftercms.commons.file.stores.RemoteFile;
import org.craftercms.commons.file.stores.WebDavUtils;
import org.craftercms.commons.file.stores.impl.AbstractProfileAwareRemoteFileStore;
import org.craftercms.commons.file.stores.impl.ProfileAwareRemotePath;
import org.craftercms.commons.file.stores.impl.ResourceBasedRemoteFile;
import org.craftercms.commons.spring.resources.WebDavResource;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

/**
 * Implementation of {@link org.craftercms.commons.file.stores.RemoteFileStore} for WebDAV
 *
 * @author joseross
 * @since 3.1.4
 */
public class WebDavFileStore extends AbstractProfileAwareRemoteFileStore<WebDavProfile> {

    @Override
    protected RemoteFile doGetFile(final ProfileAwareRemotePath path, final WebDavProfile profile)
            throws MalformedURLException {
        String fullUrl = StringUtils.appendIfMissing(profile.getBaseUrl(), "/") + path.getPath();
        Resource resource = new WebDavResource(WebDavUtils.createClient(profile), fullUrl);

        return new ResourceBasedRemoteFile(path, resource);
    }

}
