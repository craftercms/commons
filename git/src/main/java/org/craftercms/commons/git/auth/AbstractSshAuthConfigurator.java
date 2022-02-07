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
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;

import java.io.File;

/**
 * {@link GitAuthenticationConfigurator} that configures the {@code TransportCommand} to use SSH, but without providing
 * any authentication functionality. Actual authentication functionality is provided by subclasses.
 *
 * @author avasquez
 */
public abstract class AbstractSshAuthConfigurator implements GitAuthenticationConfigurator {

    protected final File sshConfig;

    public AbstractSshAuthConfigurator(File sshConfig) {
        this.sshConfig = sshConfig;
    }

    @Override
    public void configureAuthentication(TransportCommand<?,?> command) {
        SshSessionFactory sessionFactory = createSessionFactory();

        command.setTransportConfigCallback(transport -> ((SshTransport) transport).setSshSessionFactory(sessionFactory));
    }

    protected abstract SshSessionFactory createSessionFactory();

}
