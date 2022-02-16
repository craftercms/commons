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

package org.craftercms.commons.upgrade;

import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;

/**
 * Groups any number of {@link UpgradeOperation} instances
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public interface UpgradePipeline<T> {

    /**
     * Executes each {@link UpgradeOperation} for the given target
     *
     * @param context the upgrade context
     * @throws UpgradeException if any of the {@link UpgradeOperation}s fails
     */
    void execute(UpgradeContext<T> context) throws UpgradeException;

    /**
     * Indicates if the pipeline doesn't contain any operations
     *
     * @return true if there are no operations
     */
    boolean isEmpty();

}
