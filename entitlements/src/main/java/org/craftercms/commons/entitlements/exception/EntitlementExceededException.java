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
 * Exception thrown when the validation of an entitlement fails.
 *
 * @author joseross
 */
public class EntitlementExceededException extends EntitlementException {

    /**
     * The module that requested the validation.
     */
    protected Module module;

    /**
     * The entitlement that was being validated.
     */
    protected EntitlementType entitlementType;

    /**
     * The entitlement value found in the license file.
     */
    protected Number entitlementValue;

    /**
     * The actual value provided by the module.
     */
    protected Number currentValue;

    public EntitlementExceededException(final Module module, final EntitlementType entitlementType,
                                        final Number entitlementValue, final Number currentValue) {
        super(String.format("Exceeded entitlement '%s' for module '%s': using %s but allowed is %s", entitlementType,
            module, currentValue, entitlementValue));
        this.module = module;
        this.entitlementType = entitlementType;
        this.entitlementValue = entitlementValue;
        this.currentValue = currentValue;
    }

    public Module getModule() {
        return module;
    }

    public EntitlementType getEntitlementType() {
        return entitlementType;
    }

    public Number getEntitlementValue() {
        return entitlementValue;
    }

    public Number getCurrentValue() {
        return currentValue;
    }

}
