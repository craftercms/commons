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

package org.craftercms.commons.upgrade.impl.operations;

import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.impl.UpgradeContext;

/**
 * Implementation of {@link org.craftercms.commons.upgrade.UpgradeOperation} that updates the current version for
 * the given target
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 3.1.5
 */
public class UpdateVersionUpgradeOperation<T> extends AbstractUpgradeOperation<T> {

    /**
     * The version provider
     */
    protected VersionProvider<T> versionProvider;

    public UpdateVersionUpgradeOperation(final VersionProvider<T> versionProvider) {
        this.versionProvider = versionProvider;
    }

    @Override
    protected void doExecute(final UpgradeContext<T> context) throws Exception {
        versionProvider.setVersion(context, nextVersion);
    }

}
