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

package org.craftercms.commons.entitlements.model;

/**
 * Holds the value for a given entitlement type.
 *
 * @author joseross
 */
public class Entitlement {

    protected EntitlementType type;
    protected int value;

    public EntitlementType getType() {
        return type;
    }

    public void setType(final EntitlementType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entitlement{" + "type=" + type + ", value=" + value + '}';
    }

}
