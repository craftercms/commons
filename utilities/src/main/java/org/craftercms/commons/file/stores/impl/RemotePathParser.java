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
 * Internal helper interface that parses a matched remote path.
 *
 * @author avasquez
 */
public interface RemotePathParser {

    /**
     * Parses the remote path string.
     *
     * @param pathStr the remote path str
     * @param matcher the matcher used to match the paths. Can be used to get matched groups that represent
     *                the path parts
     *
     * @return the parsed {@link RemotePath}
     */
    RemotePath parse(String pathStr, Matcher matcher);

}
