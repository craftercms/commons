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
package org.craftercms.commons.validation;

import org.craftercms.commons.i10n.I10nUtils;
import org.craftercms.commons.validation.validators.Validator;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.ResourceBundle;

public class ValidationUtils {

    public static final String DEFAULT_ERROR_MESSAGE_BUNDLE_NAME = "crafter.commons.validation.errors";

    private ValidationUtils() {
    }

    public static ResourceBundle getDefaultErrorMessageBundle() {
        return ResourceBundle.getBundle(DEFAULT_ERROR_MESSAGE_BUNDLE_NAME);
    }

    public static String getErrorMessage(ResourceBundle messageBundle, String errorCode, Object... args) {
        if (messageBundle == null) {
            messageBundle = getDefaultErrorMessageBundle();
        }

        return I10nUtils.getLocalizedMessage(messageBundle, errorCode, args);
    }

    /**
     * Validates a target value according to the validation annotation
     *
     * @param validator {@link Validator} instance to delegate the validation to
     * @param value field/param value to be validated
     * @param result result of the validation
     * @param declaredType declared type of the annotated element
     */
    public static boolean validateValue(@NonNull Validator<Object> validator, Object value, ValidationResult result,
                                        Class<?> declaredType) {
        boolean collectionParam = Collection.class.isAssignableFrom(declaredType);
        if (!collectionParam) {
            return validator.validate(value, result);
        }
        Collection<Object> paramValueList = (Collection<Object>) value;
        return validator.validateCollection(paramValueList, result);
    }

}
