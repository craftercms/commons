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

package org.craftercms.commons.upgrade.impl.pipeline;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.YamlConfiguration;
import org.craftercms.commons.upgrade.UpgradeOperation;
import org.craftercms.commons.upgrade.UpgradePipeline;
import org.craftercms.commons.upgrade.UpgradePipelineFactory;
import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.impl.operations.UpdateVersionUpgradeOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * Default implementation of {@link UpgradePipelineFactory}
 *
 * @author joseross
 * @since 3.1.5
 */
public class DefaultUpgradePipelineFactoryImpl implements UpgradePipelineFactory, ApplicationContextAware {

    public static final String DEFAULT_PIPELINE_PREFIX = "pipelines.";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The version provider
     */
    protected VersionProvider versionProvider;

    /**
     * Path of the configuration file
     */
    protected Resource configurationFile;

    /**
     * The prefix for the pipelines in the configuration file, defaults to {@link this#DEFAULT_PIPELINE_PREFIX}
     */
    protected String pipelinePrefix = DEFAULT_PIPELINE_PREFIX;

    /**
     * Name used in the configuration file
     */
    protected String pipelineName;

    /**
     * Indicates if the version of the target should be updated after the pipeline is executed, default to {@code true}
     */
    protected boolean updateVersion = true;

    /**
     * The application context
     */
    protected ApplicationContext applicationContext;

    public DefaultUpgradePipelineFactoryImpl(final String pipelineName, final Resource configurationFile,
                                             final VersionProvider versionProvider) {
        this.pipelineName = pipelineName;
        this.configurationFile = configurationFile;
        this.versionProvider = versionProvider;
    }

    public void setPipelinePrefix(final String pipelinePrefix) {
        this.pipelinePrefix = pipelinePrefix;
    }

    public void setUpdateVersion(final boolean updateVersion) {
        this.updateVersion = updateVersion;
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("rawtypes")
    protected HierarchicalConfiguration loadUpgradeConfiguration() throws UpgradeException {
        YamlConfiguration configuration = new YamlConfiguration();
        try (InputStream is = configurationFile.getInputStream()) {
            configuration.read(is);
        } catch (Exception e) {
            throw new UpgradeException("Error reading configuration file", e);
        }
        return configuration;
    }

    protected UpgradePipeline createPipeline(String name, List<UpgradeOperation> operations) {
        logger.debug("Creating pipeline instance for '{}'", name);
        return new DefaultUpgradePipelineImpl(name, operations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked, rawtypes")
    public UpgradePipeline getPipeline(Object target) throws UpgradeException, ConfigurationException {
        logger.debug("Building pipeline for target '{}'", target);
        String currentVersion = versionProvider.getVersion(target);
        if (VersionProvider.SKIP.equals(currentVersion)) {
            // Return an empty pipeline to avoid errors & warnings in the log
            return new DefaultUpgradePipelineImpl(pipelineName, Collections.emptyList());
        }
        List<UpgradeOperation> operations = new LinkedList<>();
        HierarchicalConfiguration config = loadUpgradeConfiguration();
        List<HierarchicalConfiguration> pipeline = config.configurationsAt(pipelinePrefix + pipelineName);

        String nextVersion = currentVersion;
        for (HierarchicalConfiguration release : pipeline) {
            String sourceVersion = release.getString(CONFIG_KEY_CURRENT_VERSION);
            String targetVersion = release.getString(CONFIG_KEY_NEXT_VERSION);
            if (sourceVersion.equals(nextVersion)) {
                logger.debug("Adding version '{}' to pipeline '{}'", sourceVersion, pipelineName);
                List<HierarchicalConfiguration> operationsConfig = release.configurationsAt(CONFIG_KEY_OPERATIONS);
                for (HierarchicalConfiguration operationConfig : operationsConfig) {
                    UpgradeOperation operation =
                        applicationContext.getBean(operationConfig.getString(CONFIG_KEY_TYPE), UpgradeOperation.class);
                    operation.init(sourceVersion, targetVersion, operationConfig);
                    operations.add(operation);
                }
                nextVersion = targetVersion;
            } else {
                logger.debug("Skipping version '{}' already applied", sourceVersion);
            }
        }

        if (!isEmpty(operations) && updateVersion) {
            logger.debug("Adding upgrade version operation to pipeline '{}'", pipelineName);
            UpdateVersionUpgradeOperation updateOp = new UpdateVersionUpgradeOperation(versionProvider);
            updateOp.init(currentVersion, nextVersion, config);
            operations.add(updateOp);
        } else {
            logger.debug("Skipping upgrade version operation for pipeline '{}'", pipelineName);
        }

        return createPipeline(pipelineName, operations);
    }

}
