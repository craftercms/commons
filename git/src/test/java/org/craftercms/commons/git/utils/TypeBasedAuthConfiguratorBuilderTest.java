/*
 * Copyright (C) 2007-2026 Crafter Software Corporation. All Rights Reserved.
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
import org.craftercms.commons.git.auth.SshPrivateKeyAuthConfigurator;
import org.junit.Test;

import java.io.File;

/**
 * @author joseross
 */
public class TypeBasedAuthConfiguratorBuilderTest extends AbstractAuthConfiguratorBuilderTest {

	protected TypeBasedAuthConfiguratorBuilder createBuilder(AuthenticationType type) {
		// The config path is not important for this test
		return new TypeBasedAuthConfiguratorBuilder(new File("."), type);
	}

	@Test
	public void testNoAuth() {
		verify(createBuilder(AuthenticationType.none), NoopAuthConfigurator.class);
	}

	@Test
	public void testBasicAuth() {
		verify(createBuilder(AuthenticationType.basic)
			.withUsername("joe")
			.withPassword("secret"), BasicUsernamePasswordAuthConfigurator.class);
	}

	@Test
	public void testTokenAuth() {
		verify(createBuilder(AuthenticationType.token)
			.withUsername("token"), BasicUsernamePasswordAuthConfigurator.class);
	}

	@Test
	public void testPrivateKeyAuth() {
		verify(createBuilder(AuthenticationType.private_key)
			.withPrivateKeyPath("/some/key"), SshPrivateKeyAuthConfigurator.class);
	}

	@Test
	public void testPrivateKeyAuthWithPass() {
		verify(createBuilder(AuthenticationType.private_key)
			.withPrivateKeyPath("/some/key")
			.withPrivateKeyPassphrase("secret"), SshPrivateKeyAuthConfigurator.class);
	}

}
