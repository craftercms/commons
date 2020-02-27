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

import java.util.List;

import org.craftercms.commons.upgrade.exception.UpgradeException;

/**
 * Manages the current version and applies the required upgrades
 *
 * @author joseross
 * @since 3.1.5
 */
public interface UpgradeManager {

    /**
     * Executes all required upgrades for the system
     *
     * @throws UpgradeException if any of the upgrades fails
     */
    void upgrade() throws UpgradeException;

    /**
     * Executes all required upgrades for the given target
     *
     * @param target the target
     * @throws UpgradeException if any of the upgrades fails
     */
    void upgrade(Object target) throws UpgradeException;

    /**
     * Returns all targets to be upgraded
     *
     * @return the list of targets
     * @throws UpgradeException if there is any error finding the targets
     */
    List<Object> getTargets() throws UpgradeException;

}
