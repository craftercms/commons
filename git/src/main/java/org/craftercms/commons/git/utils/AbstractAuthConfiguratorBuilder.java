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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Base class for implementations of {@link AuthConfiguratorBuilder}
 *
 * @author joseross
 * @since 4.0.0
 */
public abstract class AbstractAuthConfiguratorBuilder implements AuthConfiguratorBuilder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected File sshConfig;
    protected String username;
    protected String password;
    protected String privateKeyPath;
    protected String privateKeyPassphrase;

    public AbstractAuthConfiguratorBuilder(File sshConfig) {
        this.sshConfig = sshConfig;
    }

    public AbstractAuthConfiguratorBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public AbstractAuthConfiguratorBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public AbstractAuthConfiguratorBuilder withPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
        return this;
    }

    public AbstractAuthConfiguratorBuilder withPrivateKeyPassphrase(String privateKeyPassphrase) {
        this.privateKeyPassphrase = privateKeyPassphrase;
        return this;
    }

}
