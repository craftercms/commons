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

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.git.jgit.SshPrivateKeySessionFactory;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.nio.file.Paths;

/**
 * {@link GitAuthenticationConfigurator} that configures the {@code TransportCommand} to use SSH with private key
 * authentication.
 * The file path of the private key and it's passphrase can be provided, but are not necessary, specially when the
 * private key has already been loaded into the SSH agent.
 *
 * @author avasquez
 */
public class SshPrivateKeyAuthConfigurator extends AbstractSshAuthConfigurator {

    protected final String privateKeyPath;
    protected final String passphrase;

    public SshPrivateKeyAuthConfigurator(File sshConfig, String privateKeyPath, String passphrase) {
        super(sshConfig);
        this.privateKeyPath = privateKeyPath;
        this.passphrase = passphrase;
    }

    @Override
    public void configureAuthentication(TransportCommand<?, ?> command) {
        if (StringUtils.isNotEmpty(passphrase)) {
            command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(null, passphrase));
        }

        super.configureAuthentication(command);
    }

    @Override
    protected SshSessionFactory createSessionFactory() {
        return new SshPrivateKeySessionFactory(sshConfig, Paths.get(privateKeyPath));
    }

}
