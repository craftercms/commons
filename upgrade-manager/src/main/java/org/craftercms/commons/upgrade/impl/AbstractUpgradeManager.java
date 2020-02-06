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

package org.craftercms.commons.upgrade.impl;

import java.util.List;

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.upgrade.UpgradeManager;
import org.craftercms.commons.upgrade.UpgradePipeline;
import org.craftercms.commons.upgrade.UpgradePipelineFactory;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Base class for all {@link UpgradeManager} implementations
 *
 * @author joseross
 * @since 3.1.5
 */
public abstract class AbstractUpgradeManager implements UpgradeManager, ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ApplicationContext applicationContext;

    /**
     * Indicates if errors during a target upgrade should stop the process
     */
    protected boolean continueOnFailure = true;

    public void setContinueOnFailure(final boolean continueOnFailure) {
        this.continueOnFailure = continueOnFailure;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void upgrade(final Object target) throws UpgradeException {
        logger.info("Starting upgrade for target '{}'", target);
        try {
            doUpgrade(target);
        } catch (Exception e) {
            UpgradeException ex = new UpgradeException("Error during upgrade for target " + target, e);
            if (!continueOnFailure) {
                throw ex;
            } else {
                logger.error("Error during upgrade for target " + target, ex);
            }
        } finally {
            logger.debug("Upgrade completed for target '{}'", target);
        }
    }

    @Override
    public void upgrade() throws UpgradeException {
        logger.info("Starting system upgrade");
        List<Object> targets = getTargets();
        for (Object target : targets) {
            upgrade(target);
        }
    }

    @Override
    public List<Object> getTargets() throws UpgradeException {
        logger.debug("Collecting target names");
        try {
            return doGetTargets();
        } catch (Exception e) {
            throw new UpgradeException("Error collecting target names", e);
        }
    }

    protected abstract List<Object> doGetTargets() throws Exception;

    protected abstract void doUpgrade(Object target) throws Exception;

    protected void executePipeline(Object target, UpgradePipelineFactory pipelineFactory) throws ConfigurationException, UpgradeException {
        UpgradePipeline pipeline = pipelineFactory.getPipeline(target);
        pipeline.execute(target);
    }

}
