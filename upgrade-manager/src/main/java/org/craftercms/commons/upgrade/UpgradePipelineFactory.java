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

package org.craftercms.commons.upgrade;

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.upgrade.exception.UpgradeException;

/**
 * Builds an {@link UpgradePipeline} with all the required operations
 *
 * @author joseross
 * @since 3.1.5
 */
public interface UpgradePipelineFactory {

    String CONFIG_KEY_CURRENT_VERSION = "currentVersion";
    String CONFIG_KEY_NEXT_VERSION = "nextVersion";
    String CONFIG_KEY_OPERATIONS = "operations";
    String CONFIG_KEY_TYPE = "type";

    /**
     * Retrieves the needed upgrade operations for the given target
     *
     * @param target the target
     * @return the upgrade pipeline
     * @throws UpgradeException if there is any error retrieving the operations
     */
    UpgradePipeline getPipeline(Object target) throws UpgradeException, ConfigurationException;

}
