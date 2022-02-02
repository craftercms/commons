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

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Extension of {@link SshSessionFactory} to use a given private key for SSH authentication
 *
 * @author joseross
 * @since 4.0.0
 */
public class SshPrivateKeySessionFactory extends SshSessionFactory {

    protected Path privateKey;

    public SshPrivateKeySessionFactory(File sshConfig, Path privateKey) {
        super(sshConfig);
        this.privateKey = privateKey;
    }

    // Prevents other authentication methods from being used
    @Override
    protected String getDefaultPreferredAuthentications() {
        return "publickey";
    }

    // Returns the configured private key additionally to the defaults
    @Override
    protected List<Path> getDefaultIdentities(File sshDir) {
        List<Path> identities = new LinkedList<>();
        if (privateKey != null) {
            identities.add(privateKey);
        }
        identities.addAll(super.getDefaultIdentities(sshDir));
        return identities;
    }

}
