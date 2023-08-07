/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.upgrade.impl.configuration;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.config.YamlConfiguration;
import org.craftercms.commons.upgrade.UpgradeConfigurationProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * {@link UpgradeConfigurationProvider} implementation that loads a YAML configuration file.
 * This implementation also caches the configuration, so it the configuration file is loaded only once.
 * @since 4.1.2
 */
public class YamlConfigurationProvider implements UpgradeConfigurationProvider<HierarchicalConfiguration> {

    private final Resource configurationFile;
    private volatile HierarchicalConfiguration configuration;

    public YamlConfigurationProvider(final Resource configurationFile) {
        this.configurationFile = configurationFile;
    }

    @Override
    public HierarchicalConfiguration getConfiguration() throws UpgradeException {
        if (configuration != null) {
            return configuration;
        }
        synchronized (this) {
            if (configuration == null) {
                configuration = loadConfiguration();
            }
        }
        return configuration;
    }

    private HierarchicalConfiguration loadConfiguration() throws UpgradeException {
        YamlConfiguration configuration = new YamlConfiguration();
        try (InputStream is = configurationFile.getInputStream()) {
            configuration.read(is);
        } catch (Exception e) {
            throw new UpgradeException("Failed to read configuration file", e);
        }
        return configuration;
    }
}
