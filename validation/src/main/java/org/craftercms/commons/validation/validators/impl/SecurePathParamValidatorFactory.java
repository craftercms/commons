package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.ValidateSecurePathParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class SecurePathParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateSecurePathParam, String> {

    @Override
    public Validator<String> getValidator(ValidateSecurePathParam annotation) {
        SecurePathValidator validator = new SecurePathValidator(annotation.name());
        validator.setNotNull(annotation.notNull());
        validator.setNotEmpty(annotation.notEmpty());
        validator.setNotBlank(annotation.notBlank());
        validator.setMinLength(annotation.minLength());
        validator.setMaxLength(annotation.maxLength());

        return validator;
    }

}
