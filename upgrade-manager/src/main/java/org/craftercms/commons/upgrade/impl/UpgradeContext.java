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
package org.craftercms.commons.upgrade.impl;

/**
 * Holds all data related to a given upgrade process.
 *
 * @param <T> The target type supported
 * @author joseross
 * @since 4.0.0
 */
public abstract class UpgradeContext<T> {

    /**
     * The target of the upgrade.
     */
    protected T target;

    public UpgradeContext(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return target.toString();
    }

}
