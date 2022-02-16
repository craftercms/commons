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
 * Provides the current version of a specific target
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public interface VersionProvider<T> {

    /**
     * Keyword used to retrieve the version
     */
    String VERSION = "version";

    /**
     * Value used when a file is missing from the repository
     */
    String SKIP = "SKIP";

    /**
     * Returns the current version
     *
     * @param context the upgrade context
     * @return version number
     * @throws UpgradeException if there is any error getting the current version
     */
    String getVersion(UpgradeContext<T> context) throws UpgradeException;

    /**
     * Updates the current version
     *
     * @param context the upgrade context
     * @param version the new version
     * @throws UpgradeException if there is any error setting the current version
     */
    void setVersion(UpgradeContext<T> context, String version) throws UpgradeException;

}
