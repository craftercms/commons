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

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.upgrade.exception.UpgradeException;

/**
 * Defines the basic operations for a single upgrade
 *
 * @author joseross
 * @since 3.1.5
 */
public interface UpgradeOperation {

    /**
     * Initializes the instance with the given configuration
     *
     * @param currentVersion the current version
     * @param nextVersion    the next version
     * @param config         the operation configuration
     */
    void init(String currentVersion, String nextVersion, HierarchicalConfiguration<?> config) throws ConfigurationException;

    /**
     * Performs a single upgrade operation.
     *
     * @param target the target
     * @throws UpgradeException if there is any error performing the upgrade
     */
    void execute(Object target) throws UpgradeException;

}
