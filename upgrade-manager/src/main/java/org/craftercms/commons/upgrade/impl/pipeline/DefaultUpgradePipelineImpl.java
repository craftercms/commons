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

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.craftercms.commons.upgrade.UpgradeOperation;
import org.craftercms.commons.upgrade.UpgradePipeline;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Default implementation for {@link UpgradePipeline}.
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public class DefaultUpgradePipelineImpl<T> implements UpgradePipeline<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUpgradePipelineImpl.class);

    public DefaultUpgradePipelineImpl(final String name, final List<UpgradeOperation<T>> operations) {
        this.name = name;
        this.operations = operations;
    }

    /**
     * Name of the pipeline.
     */
    protected final String name;

    /**
     * Indicates if the pipeline should continue executing after an operation fails
     */
    protected boolean continueOnFailure = false;

    /**
     * List of all upgrade operations to be executed.
     */
    protected final List<UpgradeOperation<T>> operations;

    public void setContinueOnFailure(final boolean continueOnFailure) {
        this.continueOnFailure = continueOnFailure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final UpgradeContext<T> context) throws UpgradeException {
        if (isEmpty()) {
            logger.debug("Pipeline '{}' is empty, skipping execution", name);
            return;
        }
        StopWatch watch = new StopWatch("pipeline " + name);
        logger.info("============================================================");
        logger.info("Starting execution of upgrade pipeline: {}", name);
        for (UpgradeOperation<T> operation : operations) {
            String operationName = operation.getClass().getSimpleName();
            logger.info("------- Starting execution of operation {} -------", operationName);
            watch.start(operationName);
            try {
                operation.execute(context);
            } catch (UpgradeException e) {
                if (continueOnFailure) {
                    logger.error("Execution of operation {} failed", operationName, e);
                } else {
                    throw e;
                }
            } finally {
                watch.stop();
                logger.info("------- Execution of operation {} completed -------", operationName);
            }
        }
        logger.info("Execution of pipeline {} completed", name);
        logger.info("============================================================");

        if (logger.isTraceEnabled()) {
            logger.trace("Pipeline Duration:\n" + watch.prettyPrint());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(operations);
    }

}
