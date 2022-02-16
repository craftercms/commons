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

package org.craftercms.commons.upgrade.impl.pipeline;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.vdurmont.semver4j.Semver;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.config.YamlConfiguration;
import org.craftercms.commons.upgrade.UpgradeOperation;
import org.craftercms.commons.upgrade.UpgradePipeline;
import org.craftercms.commons.upgrade.UpgradePipelineFactory;
import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.exception.UpgradeNotSupportedException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;
import org.craftercms.commons.upgrade.impl.operations.UpdateVersionUpgradeOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Default implementation of {@link UpgradePipelineFactory}
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public class DefaultUpgradePipelineFactoryImpl<T> implements UpgradePipelineFactory<T>, ApplicationContextAware {

    public static final String DEFAULT_PIPELINE_PREFIX = "pipelines.";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The version provider
     */
    protected final VersionProvider<T> versionProvider;

    /**
     * Path of the configuration file
     */
    protected final Resource configurationFile;

    /**
     * The prefix for the pipelines in the configuration file, defaults to {@code DEFAULT_PIPELINE_PREFIX}
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
                                             final VersionProvider<T> versionProvider) {
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

    protected UpgradePipeline<T> createPipeline(String name, List<UpgradeOperation<T>> operations) {
        logger.debug("Creating pipeline instance for '{}'", name);
        return new DefaultUpgradePipelineImpl<>(name, operations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes, unchecked")
    public UpgradePipeline<T> getPipeline(UpgradeContext<T> context) throws UpgradeException, ConfigurationException {
        logger.debug("Building pipeline for target '{}'", context);
        String currentVersion = versionProvider.getVersion(context);
        if (VersionProvider.SKIP.equals(currentVersion)) {
            // Return an empty pipeline to avoid errors & warnings in the log
            return new DefaultUpgradePipelineImpl<>(pipelineName, Collections.emptyList());
        }
        List<UpgradeOperation<T>> operations = new LinkedList<>();
        HierarchicalConfiguration config = loadUpgradeConfiguration();

        var pipelineRoot = pipelinePrefix + pipelineName;
        var requiredVersion = config.getString(pipelineRoot + CONFIG_KEY_REQUIRES);
        if (isNotEmpty(requiredVersion)) {
            logger.debug("Pipeline '{}' requires version '{}'", pipelineName, requiredVersion);
            // NPM mode to support ranges
            // withClearedSuffixAndBuild() because only major.minor.patch can be properly compared
            var version = new Semver(currentVersion, Semver.SemverType.NPM).withClearedSuffixAndBuild();
            if (!version.satisfies(requiredVersion)) {
                throw new UpgradeNotSupportedException(format("Current version '%s' for '%s' cannot be upgraded " +
                        "automatically, requires '%s'", currentVersion, context, requiredVersion));
            }
            pipelineRoot += CONFIG_KEY_VERSIONS;
        }

        List<HierarchicalConfiguration> pipeline = config.configurationsAt(pipelineRoot);

        String nextVersion = currentVersion;
        for (HierarchicalConfiguration release : pipeline) {
            String sourceVersion = release.getString(CONFIG_KEY_CURRENT_VERSION);
            String targetVersion = release.getString(CONFIG_KEY_NEXT_VERSION);
            if (sourceVersion.equals(nextVersion)) {
                logger.debug("Adding version '{}' to pipeline '{}'", sourceVersion, pipelineName);
                List<HierarchicalConfiguration> operationsConfig = release.configurationsAt(CONFIG_KEY_OPERATIONS);
                for (HierarchicalConfiguration operationConfig : operationsConfig) {
                    UpgradeOperation<T> operation =
                        applicationContext.getBean(operationConfig.getString(CONFIG_KEY_TYPE), UpgradeOperation.class);
                    operation.init(sourceVersion, targetVersion, operationConfig);
                    operations.add(operation);
                }

                if (updateVersion) {
                    logger.debug("Adding upgrade version operation for '{}' to pipeline '{}'", targetVersion,
                            pipelineName);
                    UpdateVersionUpgradeOperation<T> updateOp = new UpdateVersionUpgradeOperation<>(versionProvider);
                    updateOp.init(sourceVersion, targetVersion, config);
                    operations.add(updateOp);
                } else {
                    logger.debug("Skipping upgrade version operation for pipeline '{}'", pipelineName);
                }

                nextVersion = targetVersion;
            } else {
                logger.debug("Skipping version '{}' already applied", sourceVersion);
            }
        }

        return createPipeline(pipelineName, operations);
    }

}
