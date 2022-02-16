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
package org.craftercms.commons.spring.groovy;

import org.jenkinsci.plugins.scriptsecurity.sandbox.blacklists.Blacklist;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SandboxInterceptor;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.PermitAllWhitelist;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

import java.beans.ConstructorProperties;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    protected boolean sandboxEnabled;

    /**
     * Indicates if the blacklist should be enabled
     */
    protected final boolean blacklistEnabled;

    /**
     * Resource containing the restrictions
     */
    protected Resource blacklist;

    @ConstructorProperties({"sandboxEnabled", "blacklistEnabled", "blacklist"})
    public SandboxInterceptorFactory(boolean sandboxEnabled, boolean blacklistEnabled, Resource blacklist) {
        this.sandboxEnabled = sandboxEnabled;
        this.blacklistEnabled = blacklistEnabled;
        this.blacklist = blacklist;
    }

    @Override
    public Class<?> getObjectType() {
        return SandboxInterceptor.class;
    }

    @Override
    protected SandboxInterceptor createInstance() throws Exception {
        if (sandboxEnabled && blacklistEnabled) {
            try (InputStream is = blacklist.getInputStream()) {
                return new SandboxInterceptor(new Blacklist(new InputStreamReader(is)));
            }
        } else if(sandboxEnabled) {
            return new SandboxInterceptor(new PermitAllWhitelist());
        } else {
            return null;
        }
    }

}
