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
package org.craftercms.commons.validation.validators;

import org.craftercms.commons.validation.ValidationResult;

import java.util.Collection;

import static java.util.Objects.isNull;

public interface Validator<T> {

    boolean validate(T target, ValidationResult result);

    /**
     * Validates each item in the target parameter against this {@link Validator}
     * Null or empty {@link Collection} is consider valid.
     *
     * @param target collection of values to validate
     * @param result result object where validation errors are recorded
     * @return true if the collection is valid, false otherwise
     */
    default boolean validateCollection(Collection<T> target, ValidationResult result) {
        if (isNull(target)) {
            return true;
        }
        boolean isValid = true;
        for (T t : target) {
            if (!validate(t, result))
                isValid = false;
        }
        return isValid;
    }

}
