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

import org.craftercms.commons.git.jgit.SshSessionFactory;
import org.craftercms.commons.git.jgit.SshUsernamePasswordCredentialsProvider;
import org.eclipse.jgit.api.TransportCommand;

import java.io.File;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * {@link GitAuthenticationConfigurator} that configures the {@code TransportCommand} to use SSH with username/password
 * authentication.
 * The username is expected to be part of the Git SSH URL, while the password is provided separately and injected to
 * this class.
 *
 * @author avasquez
 */
public class SshPasswordAuthConfigurator extends AbstractSshAuthConfigurator {

    protected final String password;

    public SshPasswordAuthConfigurator(File sshConfig, String password) {
        super(sshConfig);
        this.password = password;
    }

    @Override
    public void configureAuthentication(TransportCommand<?, ?> command) {
        command.setCredentialsProvider(new SshUsernamePasswordCredentialsProvider(EMPTY, password));

        super.configureAuthentication(command);
    }

    @Override
    protected SshSessionFactory createSessionFactory() {
        return new SshSessionFactory(sshConfig);
    }

}
