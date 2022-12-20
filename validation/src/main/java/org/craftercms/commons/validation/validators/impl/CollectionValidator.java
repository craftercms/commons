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
package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.ValidationResult;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static org.craftercms.commons.validation.ErrorCodes.LIST_NOT_EMPTY_ERROR_CODE;

/**
 * Validator implementation that validates a parameter of type (or subtype of) {@link Collection}.
 */
public class CollectionValidator extends BasicValidator<Object> {
    private boolean notEmpty;

    public CollectionValidator(String targetKey) {
        super(targetKey);
    }

    public void setNotEmpty(final boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    @Override
    public boolean validateCollection(Collection<Object> target, ValidationResult result) {
        if (!super.validate(target, result)) {
            return false;
        }
        if (notEmpty && CollectionUtils.isEmpty(target)) {
            result.addError(targetKey, LIST_NOT_EMPTY_ERROR_CODE);
            return false;
        }

        return true;
    }
}
