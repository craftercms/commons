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

import org.apache.commons.lang3.ArrayUtils;
import org.craftercms.commons.i10n.I10nUtils;
import org.craftercms.commons.lang.RegexUtils;
import org.springframework.lang.NonNull;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.ResourceBundle;

public class ValidationUtils {

    private static final String VALUE_FIELD_NAME = "value";

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
     * Invokes a {@link Validator} for a given value and return the list or errors
     */
    public static Errors validateValue(@NonNull Validator validator, Object value) {
        Errors errors = new BeanPropertyBindingResult(value, "value");


        org.springframework.validation.ValidationUtils.invokeValidator(validator, value, errors);
        return errors;
    }

    public static boolean validateString(final String value, String[] blacklistRegexes, String[] whitelistRegexes, final boolean matchFullInput) {
        return (ArrayUtils.isEmpty(whitelistRegexes) || RegexUtils.matchesAny(value, Arrays.asList(whitelistRegexes), matchFullInput)) &&
                (ArrayUtils.isEmpty(blacklistRegexes) || !RegexUtils.matchesAny(value, Arrays.asList(blacklistRegexes), matchFullInput));
    }

}
