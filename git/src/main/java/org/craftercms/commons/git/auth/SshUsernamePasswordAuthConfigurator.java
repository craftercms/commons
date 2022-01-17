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

import com.jcraft.jsch.Session;

import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;

/**
 * {@link GitAuthenticationConfigurator} that configures the {@code TransportCommand} to use SSH with username/password authentication.
 * The user name is expected to be part of the Git SSH URL, while the password is provided separately and injected to this class.
 *
 * @author avasquez
 */
public class SshUsernamePasswordAuthConfigurator extends AbstractSshAuthConfigurator {

    protected String password;

    public SshUsernamePasswordAuthConfigurator(String password) {
        this.password = password;
    }

    @Override
    protected SshSessionFactory createSessionFactory() {
        return new JschConfigSessionFactory() {

            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setPassword(password);

                setHostKeyType(hc, session);
            }


        };
    }

}
