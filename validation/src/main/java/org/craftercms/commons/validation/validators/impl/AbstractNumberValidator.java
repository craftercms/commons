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
package org.craftercms.commons.validation.validators.impl;

import java.util.ResourceBundle;

import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;

import static org.craftercms.commons.validation.ErrorCodes.NUMBER_MAX_VALUE_ERROR;
import static org.craftercms.commons.validation.ErrorCodes.NUMBER_MIN_VALUE_ERROR;

public abstract class AbstractNumberValidator<T extends Number> extends BasicValidator<T> {

    protected T minValue;
    protected T maxValue;

    public AbstractNumberValidator(String targetKey) {
        super(targetKey);
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean validate(T target, ValidationResult result) {
        if (!super.validate(target, result)) {
            return false;
        } else if (target != null && isLessThanMinValue(target)) {
            result.addError(targetKey, NUMBER_MIN_VALUE_ERROR);
            return false;
        } else if (target != null && isGreaterThanMaxValue(target)) {
            result.addError(targetKey, NUMBER_MAX_VALUE_ERROR);
            return false;
        } else {
            return true;
        }
    }

    protected abstract boolean isLessThanMinValue(T target);

    protected abstract boolean isGreaterThanMaxValue(T target);

}
