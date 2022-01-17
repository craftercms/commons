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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;

/**
 * {@link GitAuthenticationConfigurator} that configures the {@code TransportCommand} to use SSH with RSA key pair authentication.
 * The file path of the private key and it's passphrase can be provided, but are not necessary, specially when the private key has
 * already been loaded into the SSH agent.
 *
 * @author avasquez
 */
public class SshRsaKeyPairAuthConfigurator extends AbstractSshAuthConfigurator {

    protected String privateKeyPath;
    protected String passphrase;

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    protected SshSessionFactory createSessionFactory() {
        return new JschConfigSessionFactory() {

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);

                if (StringUtils.isNotEmpty(privateKeyPath)) {
                    if (StringUtils.isNotEmpty(passphrase)) {
                        defaultJSch.addIdentity(privateKeyPath, passphrase);
                    } else {
                        defaultJSch.addIdentity(privateKeyPath);
                    }
                }

                return defaultJSch;
            }

            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                setHostKeyType(host, session);
            }

        };
    }

}
