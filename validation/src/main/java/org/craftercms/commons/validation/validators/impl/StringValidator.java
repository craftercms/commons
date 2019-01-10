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

import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.lang.RegexUtils;
import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;

import static org.craftercms.commons.validation.ErrorCodes.STRING_MAX_LENGTH_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.STRING_MIN_LENGTH_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.STRING_NOT_BLANK_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.STRING_NOT_EMPTY_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.STRING_REGEX_VALIDATION_FAILED_ERROR_CODE;

public class StringValidator extends BasicValidator<String> {

    protected boolean notEmpty;
    protected boolean notBlank;
    protected Integer minLength;
    protected Integer maxLength;
    protected String[] whitelistRegexes;
    protected String[] blacklistRegexes;
    protected boolean matchFullInput;

    public StringValidator(String targetKey) {
        super(targetKey);
        
        notEmpty = false;
        notBlank = false;
        minLength = 0;
        maxLength = Integer.MAX_VALUE;
        whitelistRegexes = new String[0];
        blacklistRegexes = new String[0];
        matchFullInput = true;
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
    public boolean validate(String target, ValidationResult result) {
        if (!super.validate(target, result)) {
            return false;
        } else if (notEmpty && StringUtils.isEmpty(target)) {
            result.addError(targetKey, STRING_NOT_EMPTY_ERROR_CODE);
            return false;
        } else if (notBlank && StringUtils.isBlank(target)) {
            result.addError(targetKey, STRING_NOT_BLANK_ERROR_CODE);
            return false;
        } else if (minLength != null && target != null && target.length() < minLength) {
            result.addError(targetKey, STRING_MIN_LENGTH_ERROR_CODE, minLength);
            return false;
        } else if (maxLength != null && target != null && target.length() > maxLength) {
            result.addError(targetKey, STRING_MAX_LENGTH_ERROR_CODE, maxLength);
            return false;
        } else if (target != null && !isWhitelistedAndNotBlacklisted(target)) {
            result.addError(targetKey, STRING_REGEX_VALIDATION_FAILED_ERROR_CODE);
            return false;
        } else {
            return true;
        }
    }

    protected boolean isWhitelistedAndNotBlacklisted(String target) {
        return (ArrayUtils.isEmpty(whitelistRegexes) || RegexUtils.matchesAny(target, Arrays.asList(whitelistRegexes), matchFullInput)) &&
               (ArrayUtils.isEmpty(blacklistRegexes) || !RegexUtils.matchesAny(target, Arrays.asList(blacklistRegexes), matchFullInput));
    }


}
