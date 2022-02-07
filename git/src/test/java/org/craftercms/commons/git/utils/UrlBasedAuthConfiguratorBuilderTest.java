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

import org.craftercms.commons.git.auth.BasicUsernamePasswordAuthConfigurator;
import org.craftercms.commons.git.auth.NoopAuthConfigurator;
import org.craftercms.commons.git.auth.SshPasswordAuthConfigurator;
import org.craftercms.commons.git.auth.SshPrivateKeyAuthConfigurator;
import org.junit.Test;

import java.io.File;

/**
 * @author joseross
 */
public class UrlBasedAuthConfiguratorBuilderTest extends AbstractAuthConfiguratorBuilderTest {

    protected UrlBasedAuthConfiguratorBuilder createBuilder(String url) {
        // The config path is not important for this test
        return new UrlBasedAuthConfiguratorBuilder(new File("."), url);
    }

    @Test
    public void testPublicHttpRepo() {
        verify(createBuilder("https://github.com/craftercms/test.git"), NoopAuthConfigurator.class);
    }

    @Test
    public void testUserPassHttpRepo() {
        verify(createBuilder("https://github.com/craftercms/test.git")
                .withUsername("joe")
                .withPassword("secret"), BasicUsernamePasswordAuthConfigurator.class);
    }

    @Test
    public void testPublicSshRepo() {
        verify(createBuilder("ssh://git@github.com/craftercms/test.git"), NoopAuthConfigurator.class);
    }

    @Test
    public void testUserPassSshRepo() {
        verify(createBuilder("ssh://git@github.com/craftercms/test.git")
                .withUsername("joe")
                .withPassword("secret"), SshPasswordAuthConfigurator.class);
    }

    @Test
    public void testUnencryptedSshKeyRepo() {
        verify(createBuilder("ssh://git@github.com/craftercms/test.git")
                .withPrivateKeyPath("/some/key"), SshPrivateKeyAuthConfigurator.class);
    }

    @Test
    public void testEncryptedSshKeyRepo() {
        verify(createBuilder("ssh://git@github.com/craftercms/test.git")
                .withPrivateKeyPath("/some/key")
                .withPrivateKeyPassphrase("secret"), SshPrivateKeyAuthConfigurator.class);
    }

}
