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

package org.craftercms.commons.file.stores;

import org.craftercms.commons.config.profiles.webdav.WebDavProfile;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.net.MalformedURLException;
import java.net.URL;

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
    public static Sardine createClient(WebDavProfile profile) throws MalformedURLException {
        Sardine client = SardineFactory.begin(profile.getUsername(), profile.getPassword());
        if (profile.isPreemptiveAuth()) {
            client.enablePreemptiveAuthentication(new URL(profile.getBaseUrl()));
        }

        return client;
    }

}
