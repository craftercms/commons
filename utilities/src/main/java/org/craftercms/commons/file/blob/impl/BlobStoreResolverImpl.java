/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
import org.craftercms.commons.config.EncryptionAwareConfigurationReader;
import org.craftercms.commons.file.blob.BlobStore;
import org.craftercms.commons.file.blob.BlobStoreResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.craftercms.commons.file.blob.BlobStore.CONFIG_KEY_ID;

/**
 * Default implementation of {@link BlobStoreResolver}
 *
 * @author joseross
 * @since 3.1.6
 */
@SuppressWarnings("rawtypes, unchecked")
public class BlobStoreResolverImpl implements BlobStoreResolver, ApplicationContextAware {

    String CONFIG_KEY_STORE = "blobStore";

    String CONFIG_KEY_TYPE = "type";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The path of the configuration file
     */
    protected String configurationPath;

    protected EncryptionAwareConfigurationReader configurationReader;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }

    public void setConfigurationReader(EncryptionAwareConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    protected BlobStore findStore(HierarchicalConfiguration config, Predicate<HierarchicalConfiguration> predicate)
            throws ConfigurationException {
        Optional<HierarchicalConfiguration> storeConfig =
                config.configurationsAt(CONFIG_KEY_STORE).stream().filter(predicate).findFirst();
        if (storeConfig.isPresent()) {
            HierarchicalConfiguration store = storeConfig.get();
            String type = store.getString(CONFIG_KEY_TYPE);
            BlobStore instance = applicationContext.getBean(type, BlobStore.class);
            instance.init(store);
            return instance;
        }
        return null;
    }

    protected HierarchicalConfiguration getConfiguration(Function<String, InputStream> configGetter) {
        logger.debug("Reading blob store configuration");
        try (InputStream is = configGetter.apply(configurationPath)) {
            if (is != null) {
                return configurationReader.readXmlConfiguration(is, "utf-8");
            }
        } catch (Exception e) {
            logger.error("Error reading blob store configuration", e);
        }
        return null;
    }

    @Override
    public BlobStore getById(Function<String, InputStream> configGetter, String storeId) throws ConfigurationException {
        logger.debug("Looking blob store with id {}", storeId);
        HierarchicalConfiguration config = getConfiguration(configGetter);
        if (config != null) {
            return findStore(config, store ->
                    StringUtils.equals(storeId, store.getString(CONFIG_KEY_ID)));
        }
        return null;
    }

}
