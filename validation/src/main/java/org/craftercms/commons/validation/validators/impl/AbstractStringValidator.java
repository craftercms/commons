package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.ValidationUtils;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidatorContext;

import static org.craftercms.commons.validation.ErrorCodes.STRING_REGEX_VALIDATION_FAILED_ERROR_CODE;

public class AbstractStringValidator implements Validator {
    protected String[] whitelistRegexes;
    protected String[] blacklistRegexes;
    protected boolean matchFullInput;

    public AbstractStringValidator() {
        whitelistRegexes = new String[0];
        blacklistRegexes = new String[0];
        matchFullInput = true;
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
