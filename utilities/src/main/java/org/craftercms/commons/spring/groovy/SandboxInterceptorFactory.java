/*
 * Copyright (C) 2007-2025 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.spring.groovy;

import org.jenkinsci.plugins.scriptsecurity.sandbox.Whitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.blacklists.Blacklist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SandboxInterceptor;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.CompositeWhitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.PermitAllWhitelist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.StaticWhitelist;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

/**
 * Implementation of {@link org.springframework.beans.factory.FactoryBean} for {@link SandboxInterceptor}
 *
 * @author joseross
 * @since 3.1.12
 */
public class SandboxInterceptorFactory extends AbstractFactoryBean<SandboxInterceptor> {

	/**
	 * Indicates if the sandbox should be enabled
	 */
	protected final boolean sandboxEnabled;

	/**
	 * Indicates if the blacklist should be enabled
	 */
	protected final boolean blacklistEnabled;

	/**
	 * Resource containing the restrictions
	 */
	protected final Resource blacklist;


	/**
	 * Indicates if the whitelist should be enabled
	 */
	protected final boolean whitelistEnabled;

	/**
	 * Resource containing the allowed definitions
	 */
	protected final Resource whitelist;

	/**
	 * List of regex to allow System.getenv()
	 */
	protected final List<String> whitelistGetEnvRegex;

	protected final List<Class<?>> extensions;

	@ConstructorProperties({"sandboxEnabled", "blacklistEnabled", "blacklist",
			"whitelistEnabled", "whitelist", "whitelistGetEnvRegex",
			"extensions"})
	public SandboxInterceptorFactory(final boolean sandboxEnabled, final boolean blacklistEnabled, final Resource blacklist,
									 final boolean whitelistEnabled, final Resource whitelist, final String[] whitelistGetEnvRegex,
									 final List<Class<?>> extensions) {
		this.sandboxEnabled = sandboxEnabled;
		this.blacklistEnabled = blacklistEnabled;
		this.blacklist = blacklist;
		this.whitelistEnabled = whitelistEnabled;
		this.whitelist = whitelist;
		if (blacklistEnabled && blacklist == null) {
			throw new IllegalArgumentException("blacklistEnabled=true but 'blacklist' resource is null");
		}
		if (whitelistEnabled && whitelist == null) {
			throw new IllegalArgumentException("whitelistEnabled=true but 'whitelist' resource is null");
		}
		this.whitelistGetEnvRegex = (whitelistGetEnvRegex == null) ? List.of() : Arrays.asList(whitelistGetEnvRegex);
		this.extensions = emptyIfNull(extensions);
	}

	@Override
	public Class<?> getObjectType() {
		return SandboxInterceptor.class;
	}

	@Override
	protected SandboxInterceptor createInstance() throws Exception {
		if (!sandboxEnabled) {
			return null;
		}
		return new SandboxInterceptor(getWhitelist(), extensions);
	}

	/**
	 * Get a sandbox whitelist based on the current configuration:
	 * <ul>
	 * <li>If the sandbox is disabled, returns <code>null</code></li>
	 * <li>If both the whitelist and blacklist are disabled, returns a <code>PermitAllWhitelist</code></li>
	 * <li>If either (or both) the whitelist or blacklist are enabled, returns a <code>CompositeWhitelist</code> containing the enabled lists.
	 * The resulting whitelist will allow a call if and only if it is allowed by both the whitelist and the blacklist</li>
	 * </ul>
	 *
	 * @throws IOException if there is any error loading the whitelist/blacklist resources
	 */
	protected Whitelist getWhitelist() throws IOException {
		if (!sandboxEnabled) {
			// Nothing to do here
			return null;
		}
		if (!blacklistEnabled && !whitelistEnabled) {
			Whitelist whitelist = new PermitAllWhitelist();
			whitelist.setGetEnvWhitelistRegex(whitelistGetEnvRegex);
			return whitelist;
		}

		Collection<Whitelist> delegateLists = new ArrayList<>(2);
		if (whitelistEnabled) {
			try (InputStream is = whitelist.getInputStream()) {
				Whitelist whitelist = new StaticWhitelist(new InputStreamReader(is));
				whitelist.setGetEnvWhitelistRegex(whitelistGetEnvRegex);
				delegateLists.add(whitelist);
			}
		}
		if (blacklistEnabled) {
			try (InputStream is = blacklist.getInputStream()) {
				Blacklist blacklist = new Blacklist(new InputStreamReader(is));
				blacklist.setGetEnvWhitelistRegex(whitelistGetEnvRegex);
				delegateLists.add(blacklist);
			}
		}

		return new CompositeWhitelist(delegateLists);
	}


}
