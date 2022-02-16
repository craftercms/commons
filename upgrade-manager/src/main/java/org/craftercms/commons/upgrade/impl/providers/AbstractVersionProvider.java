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

package org.craftercms.commons.upgrade.impl.providers;

import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Base class for all {@link VersionProvider} implementations
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public abstract class AbstractVersionProvider<T> implements VersionProvider<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The default value to use when no version is found, defaults to {@link VersionProvider#SKIP}
     */
    protected String defaultValue = SKIP;

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getVersion(final UpgradeContext<T> context) throws UpgradeException {
        logger.debug("Getting current version for {}", context);
        try {
            String currentValue = doGetVersion(context);
            logger.debug("Found version {} for {}", currentValue, context);
            if (isEmpty(currentValue)) {
                currentValue = defaultValue;
            }
            return currentValue;
        } catch (Exception e) {
            throw new UpgradeException("Error getting current version for " + context, e);
        }
    }

    protected abstract String doGetVersion(UpgradeContext<T> context) throws Exception;

    @Override
    public void setVersion(final UpgradeContext<T> context, final String version) throws UpgradeException {
        logger.debug("Updating current version for {} to {}", context, version);
        try {
            doSetVersion(context, version);
            logger.debug("Version successfully updated for {}", context);
        } catch (Exception e) {
            throw new UpgradeException("Error updating version for " + context, e);
        }
    }

    protected abstract void doSetVersion(UpgradeContext<T> context, String version) throws Exception;

}
