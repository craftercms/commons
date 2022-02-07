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
package org.craftercms.commons.git.jgit;

import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.agent.ConnectorFactory;

import java.io.File;

/**
 * Extension of {@link SshdSessionFactory} that overrides the default SSH configuration
 *
 * @author joseross
 * @since 4.0.0
 */
public class SshSessionFactory extends SshdSessionFactory {

    /**
     * The folder for the SSH configuration
     */
    protected File sshConfig;

    public SshSessionFactory(File sshConfig) {
        this.sshConfig = sshConfig;
    }

    // Prevents the current user's home from being used
    @Override
    public File getHomeDirectory() {
        return sshConfig.getParentFile();
    }

    @Override
    public File getSshDirectory() {
        return sshConfig;
    }

    // Prevents other authentication methods from being used
    @Override
    protected String getDefaultPreferredAuthentications() {
        return "password";
    }

    // Prevents using the native ssh agent
    @Override
    protected ConnectorFactory getConnectorFactory() {
        return null;
    }

}
