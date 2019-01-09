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

import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.validators.Validator;

import static org.craftercms.commons.validation.ErrorCodes.NOT_NULL_ERROR_CODE;

public class BasicValidator<T> implements Validator<T> {

    protected String targetKey;
    protected boolean notNull;

    public BasicValidator(String targetKey) {
        this.targetKey = targetKey;
        this.notNull = false;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    @Override
    public boolean validate(T target, ValidationResult result) {
        if (notNull && target == null) {
            result.addError(targetKey, NOT_NULL_ERROR_CODE);
            return false;
        } else {
            return true;
        }
    }

}
