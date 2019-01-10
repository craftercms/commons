/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.entitlements.exception;

import org.craftercms.commons.entitlements.model.EntitlementType;
import org.craftercms.commons.entitlements.model.Module;

/**
 * Exception thrown when an invalid module or entitlement is requested during a validation.
 *
 * @author joseross
 */
public class UnsupportedEntitlementException extends EntitlementException {

    /**
     * The requested module.
     */
    protected Module module;

    /**
     * The requested entitlement.
     */
    protected EntitlementType entitlementType;

    public UnsupportedEntitlementException(final Module module, final EntitlementType entitlementType) {
        super(String.format("Unsupported entitlement '%s' for module '%s'", entitlementType, module));
        this.module = module;
        this.entitlementType = entitlementType;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public EntitlementType getEntitlementType() {
        return entitlementType;
    }

    public void setEntitlementType(final EntitlementType entitlementType) {
        this.entitlementType = entitlementType;
    }

}
