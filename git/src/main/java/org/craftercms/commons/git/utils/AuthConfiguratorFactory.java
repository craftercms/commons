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
package org.craftercms.commons.git.utils;

import java.io.File;

/**
 * Utility class used to create {@link AuthConfiguratorBuilder} objects
 *
 * @author joseross
 * @since 4.0.0
 */
public class AuthConfiguratorFactory {

    protected final File sshConfig;

    public AuthConfiguratorFactory(File sshConfig) {
        this.sshConfig = sshConfig;
    }

    /**
     * Creates a new instance of {@link UrlBasedAuthConfiguratorBuilder}
     * @param url the repository url
     * @return the builder
     */
    public UrlBasedAuthConfiguratorBuilder forUrl(String url) {
        return new UrlBasedAuthConfiguratorBuilder(sshConfig, url);
    }

    /**
     * Creates a new instance of {@link TypeBasedAuthConfiguratorBuilder}
     * @param type the auth type
     * @return the builder
     */
    public TypeBasedAuthConfiguratorBuilder forType(String type) {
        return new TypeBasedAuthConfiguratorBuilder(sshConfig, type);
    }

}
