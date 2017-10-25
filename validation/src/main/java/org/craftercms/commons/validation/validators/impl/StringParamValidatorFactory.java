package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.ValidateStringParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class StringParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateStringParam, String> {

    @Override
    public Validator<String> getValidator(ValidateStringParam annotation) {
        StringValidator validator = new StringValidator(annotation.name());
        validator.setNotNull(annotation.notNull());
        validator.setNotEmpty(annotation.notEmpty());
        validator.setNotBlank(annotation.notBlank());
        validator.setMinLength(annotation.minLength());
        validator.setMaxLength(annotation.maxLength());
        validator.setWhitelistRegexes(annotation.whitelistedPatterns());
        validator.setBlacklistRegexes(annotation.blacklistedPatterns());
        validator.setMatchFullInput(annotation.matchFullInput());

        return validator;
    }

}
