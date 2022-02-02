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
import org.craftercms.commons.git.auth.SshPasswordAuthConfigurator;

import java.io.File;

/**
 * Implementation of {@link AbstractAuthConfiguratorBuilder} that creates {@link GitAuthenticationConfigurator} objects
 * based on the scheme of a given URL
 *
 * @author joseross
 * @since 4.0.0
 */
public class UrlBasedAuthConfiguratorBuilder extends AbstractAuthConfiguratorBuilder {

    protected static final String GIT_SSH_URL_REGEX = "^(ssh://.+)|([a-zA-Z0-9._-]+@.+)$";

    /**
     * The Git URL
     */
    protected String url;

    public UrlBasedAuthConfiguratorBuilder(File sshConfig, String url) {
        super(sshConfig);
        this.url = url;
    }

    @Override
    public GitAuthenticationConfigurator build() {
        if (url.matches(GIT_SSH_URL_REGEX)) {
            if (StringUtils.isNotEmpty(privateKeyPath)) {
                logger.debug("SSH private key authentication will be used to connect to {}", url);
                return new SshPrivateKeyAuthConfigurator(sshConfig, privateKeyPath, privateKeyPassphrase);
            } else if (StringUtils.isNotEmpty(password)) {
                logger.debug("SSH username/password authentication will be used to connect to {}", url);
                return new SshPasswordAuthConfigurator(sshConfig, password);
            }
        } else if (StringUtils.isNotEmpty(username) || StringUtils.isNotEmpty(password)) {
            logger.debug("Username/password authentication will be used to connect to {}", url);
            return new BasicUsernamePasswordAuthConfigurator(username, password);
        }

        logger.debug("No authentication will be used to connect to {}", url);
        return new NoopAuthConfigurator();
    }

}
