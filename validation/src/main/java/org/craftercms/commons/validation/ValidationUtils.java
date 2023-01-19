/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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
import org.springframework.lang.NonNull;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.ResourceBundle;

import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.craftercms.commons.lang.RegexUtils.matchesAny;

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
     * Invokes a {@link Validator} for a given value and return the list or errors
     *
     * @param validator {@link Validator} to invoke
     * @param value     value to validate
     * @param objectKey the key/name of the value being validated
     */
    public static ValidationResult validateValue(@NonNull Validator validator, Object value, String objectKey) {
        Errors errors = new BeanPropertyBindingResult(value, objectKey);
        org.springframework.validation.ValidationUtils.invokeValidator(validator, value, errors);
        return getValidationResult(errors);
    }

    private static ValidationResult getValidationResult(final Errors errors) {
        ValidationResult result = new ValidationResult(format("Validation failed for '%s'", errors.getObjectName()), getDefaultErrorMessageBundle());
        errors.getAllErrors().forEach(error ->
                result.addError(errors.getObjectName(), error.getCode()));
        return result;
    }

    /**
     * Convenience method to validate a String against a list of blacklist regexes and a list of whitelist regexes
     *
     * @param value            String to validate
     * @param blacklistRegexes list of blacklist regexes
     * @param whitelistRegexes list of whitelist regexes
     * @param matchFullInput   if the entire string should be matched
     * @return true if the string matches any of the whitelist regexes and none of the blacklist regexes
     */
    public static boolean validateString(final String value, String[] blacklistRegexes, String[] whitelistRegexes, final boolean matchFullInput) {
        return (isEmpty(whitelistRegexes) || matchesAny(value, Arrays.asList(whitelistRegexes), matchFullInput)) &&
                (isEmpty(blacklistRegexes) || !matchesAny(value, Arrays.asList(blacklistRegexes), matchFullInput));
    }

}
