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

package org.craftercms.commons.file.stores;

import org.craftercms.commons.config.profiles.webdav.WebDavProfile;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

/**
 * Utility methods for WebDAV
 *
 * @author joseross
 * @since 3.1.4
 */
public class WebDavUtils {

    /**
     * Creates a WebDAV client based on the given profile config
     * @param profile the configuration profile
     * @return a WebDAV client
     */
    public static Sardine createClient(WebDavProfile profile) {
        return SardineFactory.begin(profile.getUsername(), profile.getPassword());
    }

}
