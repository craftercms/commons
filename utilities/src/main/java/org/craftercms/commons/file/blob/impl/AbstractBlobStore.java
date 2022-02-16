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
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.ConfigurationMapper;
import org.craftercms.commons.config.profiles.ConfigurationProfile;
import org.craftercms.commons.file.blob.Blob;
import org.craftercms.commons.file.blob.BlobStore;
import org.craftercms.commons.config.PublishingTargetResolver;
import org.springframework.core.io.Resource;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.craftercms.commons.config.ConfigUtils.getRequiredStringProperty;

/**
 * Base class for all implementations of {@link BlobStore}
 *
 * @author joseross
 * @since 3.1.6
 */
public abstract class AbstractBlobStore<T extends ConfigurationProfile> implements BlobStore {

    /**
     * The id of the store
     */
    protected String id;

    /**
     * The regex to check for compatible paths
     */
    protected String pattern;

    /**
     * The mappings for the different environments
     */
    protected Map<String, Mapping> mappings;

    /**
     * The profile to connect to the remote store
     */
    protected T profile;

    /**
     * The mapper to load the profile configuration
     */
    protected ConfigurationMapper<T> profileMapper;

    /**
     * The publishing target resolver
     */
    protected PublishingTargetResolver publishingTargetResolver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setProfileMapper(ConfigurationMapper<T> profileMapper) {
        this.profileMapper = profileMapper;
    }

    public void setPublishingTargetResolver(PublishingTargetResolver publishingTargetResolver) {
        this.publishingTargetResolver = publishingTargetResolver;
    }

    @Override
    public boolean isCompatible(String path) {
        return path != null && path.matches(pattern);
    }

    @Override
    public void init(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException {
        id = getRequiredStringProperty(config, CONFIG_KEY_ID);
        pattern = getRequiredStringProperty(config, CONFIG_KEY_PATTERN);

        mappings = new LinkedHashMap<>();
        config.configurationsAt(CONFIG_KEY_MAPPING).forEach(bucketConfig -> {
            Mapping mapping = new Mapping();
            mapping.target = bucketConfig.getString(CONFIG_KEY_MAPPING_STORE_TARGET);
            mapping.prefix = bucketConfig.getString(CONFIG_KEY_MAPPING_PREFIX);
            mappings.put(bucketConfig.getString(CONFIG_KEY_MAPPING_PUBLISHING_TARGET), mapping);
        });

        profile = profileMapper.processConfig(config.configurationAt(CONFIG_KEY_CONFIGURATION));
        profile.setProfileId(config.getString(CONFIG_KEY_ID));

        doInit(config);
    }

    protected abstract void doInit(HierarchicalConfiguration<ImmutableNode> config) throws ConfigurationException;

    @Override
    public Resource getResource(String path, Blob blob) {
        return doGetContent(getMapping(publishingTargetResolver.getPublishingTarget()), path);
    }

    protected abstract Resource doGetContent(Mapping mapping, String path);

    protected Mapping getMapping(String publishingTarget) {
        return mappings.get(publishingTarget);
    }

    /**
     * Internal class used when loading the configuration
     */
    public static class Mapping {

        /**
         * The target in the store
         */
        public String target;

        /**
         * The prefix to use in the store
         */
        public String prefix;

    }

}
