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
package org.craftercms.commons.file.blob.impl;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationProvider;
import org.craftercms.commons.config.ConfigurationResolver;
import org.craftercms.commons.file.blob.BlobStore;
import org.craftercms.commons.file.blob.exception.BlobStoreConfigurationMissingException;
import org.craftercms.commons.file.blob.BlobStoreResolver;
import org.craftercms.commons.file.blob.exception.BlobStoreMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.String.format;
import static org.craftercms.commons.file.blob.BlobStore.CONFIG_KEY_ID;

/**
 * Default implementation of {@link BlobStoreResolver}
 *
 * @author joseross
 * @since 3.1.6
 */
@SuppressWarnings("rawtypes, unchecked")
public class BlobStoreResolverImpl implements BlobStoreResolver, ApplicationContextAware {

    public final String CONFIG_KEY_STORE = "blobStore";

    public final String CONFIG_KEY_TYPE = "type";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The module of the configuration file
     */
    protected String configModule;

    /**
     * The path of the configuration file
     */
    protected String configPath;

    protected ConfigurationResolver configurationResolver;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setConfigModule(String configModule) {
        this.configModule = configModule;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public void setConfigurationResolver(ConfigurationResolver configurationResolver) {
        this.configurationResolver = configurationResolver;
    }

    protected String findStoreId(HierarchicalConfiguration config, Predicate<HierarchicalConfiguration> predicate) {
        if (config == null || config.isEmpty()) {
            throw new BlobStoreConfigurationMissingException("No blob store configuration found");
        }
        Optional<HierarchicalConfiguration> storeConfig =
                config.configurationsAt(CONFIG_KEY_STORE).stream().filter(predicate).findFirst();
        if (storeConfig.isPresent()) {
            HierarchicalConfiguration store = storeConfig.get();
            return store.getString(CONFIG_KEY_ID);
        }
        return null;
    }

    protected BlobStore findStore(HierarchicalConfiguration config, Predicate<HierarchicalConfiguration> predicate)
            throws ConfigurationException {
        if (config == null || config.isEmpty()) {
            throw new BlobStoreConfigurationMissingException("No blob store configuration found");
        }
        Optional<HierarchicalConfiguration> storeConfig =
                config.configurationsAt(CONFIG_KEY_STORE).stream().filter(predicate).findFirst();
        if (storeConfig.isPresent()) {
            HierarchicalConfiguration store = storeConfig.get();
            String type = store.getString(CONFIG_KEY_TYPE);
            try {
                BlobStore instance = applicationContext.getBean(type, BlobStore.class);
                instance.init(store);
                return instance;
            } catch (NoSuchBeanDefinitionException e) {
                throw new BlobStoreMissingException(
                        format("No blob store found with id '%s'", store.getString(CONFIG_KEY_ID)));
            }
        } else {
            throw new BlobStoreMissingException("Blob store not found in the configuration file");
        }
    }

    protected HierarchicalConfiguration getConfiguration(ConfigurationProvider provider) throws ConfigurationException {
        logger.debug("Reading blob store configuration");
        try {
            return configurationResolver.getXmlConfiguration(configModule, configPath, provider);
        } catch (ConfigurationException e) {
            logger.error("Error reading blob store configuration", e);
            throw e;
        }
    }

    @Override
    public BlobStore getById(ConfigurationProvider provider, String storeId) throws ConfigurationException {
        return getById(getConfiguration(provider), storeId);
    }

    protected BlobStore getById(HierarchicalConfiguration config, String storeId) throws ConfigurationException {
        logger.debug("Looking blob store with id {}", storeId);
        return findStore(config, store -> StringUtils.equals(storeId, store.getString(CONFIG_KEY_ID)));
    }

}
