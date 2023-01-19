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
package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.ValidationUtils;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.craftercms.commons.validation.ErrorCodes.STRING_REGEX_VALIDATION_FAILED_ERROR_CODE;

/**
 * Base {@link Validator} implementation for String validation.
 * It supports a list of whitelist regexes and a list of blacklist
 * regexes to validate the input value.
 */
public abstract class AbstractStringValidator implements Validator {
    protected List<String> whitelistRegexes;
    protected List<String> blacklistRegexes;
    protected boolean matchFullInput;

    public AbstractStringValidator() {
        whitelistRegexes = emptyList();
        blacklistRegexes = emptyList();
        matchFullInput = true;
    }

    public AbstractStringValidator(final List<String> whitelistRegexes, final List<String> blacklistRegexes, final boolean matchFullInput) {
        this.whitelistRegexes = whitelistRegexes;
        this.blacklistRegexes = blacklistRegexes;
        this.matchFullInput = matchFullInput;
    }

    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value == null || ValidationUtils.validateString(value, blacklistRegexes, whitelistRegexes, matchFullInput);
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object value, @NonNull Errors errors) {
        if (!ValidationUtils.validateString((String) value, blacklistRegexes, whitelistRegexes, matchFullInput)) {
            errors.reject(STRING_REGEX_VALIDATION_FAILED_ERROR_CODE);
        }
    }
}
