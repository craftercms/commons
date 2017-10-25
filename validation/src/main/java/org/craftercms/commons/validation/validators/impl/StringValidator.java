/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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

import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.i10n.I10nUtils;
import org.craftercms.commons.lang.RegexUtils;
import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;
import org.craftercms.commons.validation.validators.Validator;
import org.springframework.beans.factory.annotation.Required;

import static org.craftercms.commons.validation.validators.ErrorCodes.StringErrors.*;

public class StringValidator implements Validator<String> {

    protected String targetKey;
    protected boolean notNull;
    protected boolean notEmpty;
    protected boolean notBlank;
    protected Integer minLength;
    protected Integer maxLength;
    protected String[] whitelistRegexes;
    protected String[] blacklistRegexes;
    protected boolean matchFullInput;

    public StringValidator(String targetKey) {
        this.targetKey = targetKey;
        this.notNull = false;
        this.notEmpty = false;
        this.notBlank = false;
        this.minLength = 0;
        this.maxLength = Integer.MAX_VALUE;
        this.whitelistRegexes = new String[0];
        this.blacklistRegexes = new String[0];
        this.matchFullInput = true;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public void setNotBlank(boolean notBlank) {
        this.notBlank = notBlank;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setWhitelistRegexes(String[] whitelistRegexes) {
        this.whitelistRegexes = whitelistRegexes;
    }

    public void setBlacklistRegexes(String[] blacklistRegexes) {
        this.blacklistRegexes = blacklistRegexes;
    }

    public void setMatchFullInput(boolean matchFullInput) {
        this.matchFullInput = matchFullInput;
    }

    @Override
    public void validate(String target, ValidationResult result) {
        validate(target, result, null);
    }

    @Override
    public void validate(String target, ValidationResult result, ResourceBundle errorMessageBundle) {
        if (notNull && target == null) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, NOT_NULL_ERROR_CODE));
        } else if (notEmpty && StringUtils.isEmpty(target)) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, NOT_EMPTY_ERROR_CODE));
        } else if (notBlank && StringUtils.isBlank(target)) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, NOT_BLANK_ERROR_CODE));
        } else if (minLength != null && target != null && target.length() < minLength) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, MIN_LENGTH_ERROR_CODE, minLength));
        } else if (maxLength != null && target != null && target.length() > maxLength) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, MAX_LENGTH_ERROR_CODE, maxLength));
        } else if (target != null && !isWhitelistedAndNotBlacklisted(target)) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, REGEX_VALIDATION_FAILED_ERROR_CODE));
        }
    }

    protected boolean isWhitelistedAndNotBlacklisted(String target) {
        return (ArrayUtils.isEmpty(whitelistRegexes) || RegexUtils.matchesAny(target, Arrays.asList(whitelistRegexes), matchFullInput)) &&
               (ArrayUtils.isEmpty(blacklistRegexes) || !RegexUtils.matchesAny(target, Arrays.asList(blacklistRegexes), matchFullInput));
    }


}
