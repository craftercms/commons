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

package org.craftercms.commons.upgrade.impl.providers;

import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Base class for all {@link VersionProvider} implementations
 *
 * @author joseross
 * @since 3.1.5
 */
public abstract class AbstractVersionProvider implements VersionProvider {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The default value to use when no version is found, defaults to {@link VersionProvider#SKIP}
     */
    protected String defaultValue = SKIP;

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getVersion(final Object target) throws UpgradeException {
        logger.debug("Getting current version for {}", target);
        try {
            String currentValue = doGetVersion(target);
            logger.debug("Found version {} for {}", currentValue, target);
            if (isEmpty(currentValue)) {
                currentValue = defaultValue;
            }
            return currentValue;
        } catch (Exception e) {
            throw new UpgradeException("Error getting current version for " + target, e);
        }
    }

    protected abstract String doGetVersion(Object target) throws Exception;

    @Override
    public void setVersion(final Object target, final String version) throws UpgradeException {
        logger.debug("Updating current version for {} to {}", target, version);
        try {
            doSetVersion(target, version);
            logger.debug("Version successfully updated for {}", target);
        } catch (Exception e) {
            throw new UpgradeException("Error updating version for " + target, e);
        }
    }

    protected abstract void doSetVersion(Object target, String version) throws Exception;

}
