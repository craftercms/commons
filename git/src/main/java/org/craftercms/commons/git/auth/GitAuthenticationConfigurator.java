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
package org.craftercms.commons.git.auth;

import org.eclipse.jgit.api.TransportCommand;

/**
 * Utility class that configures a Git connection based on an authentication strategy.
 *
 * @author avasquez
 */
public interface GitAuthenticationConfigurator {

    /**
     * Configures the authentication of the given {@link TransportCommand} based on a specific authentication strategy,
     * like HTTP basic authentication, SSH username/password authentication and SSH RSA key pair authentication.
     *
     * @param command the command to configure
     */
    void configureAuthentication(TransportCommand<?, ?> command);

}
