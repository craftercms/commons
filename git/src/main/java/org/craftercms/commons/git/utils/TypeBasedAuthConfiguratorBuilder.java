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

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.git.auth.BasicUsernamePasswordAuthConfigurator;
import org.craftercms.commons.git.auth.GitAuthenticationConfigurator;
import org.craftercms.commons.git.auth.NoopAuthConfigurator;
import org.craftercms.commons.git.auth.SshPrivateKeyAuthConfigurator;

import java.io.File;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * Implementation of {@link AbstractAuthConfiguratorBuilder} that creates {@link GitAuthenticationConfigurator} objects
 * based on a given authentication type.
 *
 * @author joseross
 * @since 4.0.0
 */
public class TypeBasedAuthConfiguratorBuilder extends AbstractAuthConfiguratorBuilder {

    /**
     * The authentication type to use
     */
    protected String authType;

    public TypeBasedAuthConfiguratorBuilder(File sshConfig, String authType) {
        super(sshConfig);
        this.authType = authType;
    }

    @Override
    public GitAuthenticationConfigurator build() {
        switch (authType) {
            case AuthenticationType.NONE:
                logger.debug("No authentication will be used");
                return new NoopAuthConfigurator();
            case AuthenticationType.BASIC:
                if (isEmpty(username) && isEmpty(password)) {
                    throw new IllegalStateException("basic auth requires a username or password");
                }
                logger.debug("Username/password authentication will be used");
                return new BasicUsernamePasswordAuthConfigurator(username, password);
            case AuthenticationType.TOKEN:
                if (isEmpty(username)) {
                    throw new IllegalStateException("token auth requires a username");
                }
                logger.debug("Token authentication will be used");
                return new BasicUsernamePasswordAuthConfigurator(username, StringUtils.EMPTY);
            case AuthenticationType.PRIVATE_KEY:
                logger.debug("SSH private key authentication will be used");
                return new SshPrivateKeyAuthConfigurator(sshConfig, privateKeyPath, privateKeyPassphrase);
            default:
                throw new IllegalStateException("Unsupported auth type " + authType);
        }
    }

}
