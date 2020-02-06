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

package org.craftercms.commons.upgrade.impl.operations;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.upgrade.UpgradeOperation;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * Base class for all {@link UpgradeOperation} implementations that provides access to system resources
 * <p>Supported YAML properties:
 * <ul>
 *     <li><strong>currentVersion</strong>: (required) the version number that will be upgraded</li>
 *     <li><strong>nextVersion</strong> (required) the version number to use after the upgrade</li>
 * </ul>
 * </p>
 *
 * @author joseross
 * @since 3.1.5
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractUpgradeOperation implements UpgradeOperation, ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The current version.
     */
    protected String currentVersion;

    /**
     * The next version.
     */
    protected String nextVersion;


    /**
     * The application context
     */
    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void init(final String currentVersion, final String nextVersion, final HierarchicalConfiguration config) throws ConfigurationException {
        this.currentVersion = currentVersion;
        this.nextVersion = nextVersion;

        doInit(config);
    }

    protected void doInit(final HierarchicalConfiguration<?> config) throws ConfigurationException {
        // do nothing by default
    }

    @Override
    public void execute(final Object target) throws UpgradeException {
        logger.debug("Starting execution for target {}", target);
        try {
            doExecute(target);
        } catch (Exception e) {
            throw new UpgradeException("Error executing upgrade operation " + getClass(), e);
        } finally {
            logger.debug("Execution completed for target {}", target);
        }
    }

    protected abstract void doExecute(Object target) throws Exception;

    protected Resource loadResource(String path) {
        return applicationContext.getResource(path);
    }

}

