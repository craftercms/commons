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
package org.craftercms.commons.file.stores.impl;

import org.craftercms.commons.file.stores.RemotePath;

/**
 * Extension of {@link RemotePath} that adds the concept of a remote configuration profile to the path.
 *
 * @author avasquez
 */
public class ProfileAwareRemotePath extends RemotePath {

    private String profile;

    public ProfileAwareRemotePath(String storeType, String path, String profile) {
        super(storeType, path);
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

}
