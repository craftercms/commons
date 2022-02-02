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

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Extension of {@link UsernamePasswordCredentialsProvider} to use in SSH authentication
 *
 * @author joseross
 * @since 4.0.0
 */
public class SshUsernamePasswordCredentialsProvider extends UsernamePasswordCredentialsProvider {

    public SshUsernamePasswordCredentialsProvider(String username, String password) {
        super(username, password);
    }

    // This allows it to be used for SSH sessions
    @Override
    public boolean isInteractive() {
        return true;
    }

}
