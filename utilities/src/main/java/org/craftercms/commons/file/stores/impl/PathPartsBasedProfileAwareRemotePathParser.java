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

import org.craftercms.commons.file.stores.RemotePath;

import java.util.regex.Matcher;

/**
 * {@link RemotePathParser} that builds {@link ProfileAwareRemotePath}s based on the parts of the given path,
 * returned by the groups of the regex matcher.
 *
 * @author avasquez
 */
public class PathPartsBasedProfileAwareRemotePathParser implements RemotePathParser {

    private static final int DEFAULT_STORE_TYPE_GROUP = 1;
    private static final int DEFAULT_PROFILE_GROUP = 2;
    private static final int DEFAULT_ACTUAL_PATH_GROUP = 3;

    private int storeTypeGroup;
    private int profileGroup;
    private int actualPathGroup;

    public PathPartsBasedProfileAwareRemotePathParser() {
        storeTypeGroup = DEFAULT_STORE_TYPE_GROUP;
        profileGroup = DEFAULT_PROFILE_GROUP;
        actualPathGroup = DEFAULT_ACTUAL_PATH_GROUP;
    }

    public void setStoreTypeGroup(int storeTypeGroup) {
        this.storeTypeGroup = storeTypeGroup;
    }

    public void setProfileGroup(int profileGroup) {
        this.profileGroup = profileGroup;
    }

    public void setActualPathGroup(int actualPathGroup) {
        this.actualPathGroup = actualPathGroup;
    }

    @Override
    public RemotePath parse(String pathStr, Matcher matcher) {
        String storeType = matcher.group(storeTypeGroup);
        String profile = matcher.group(profileGroup);
        String actualPath = matcher.group(actualPathGroup);

        return new ProfileAwareRemotePath(storeType, actualPath, profile);
    }

}
