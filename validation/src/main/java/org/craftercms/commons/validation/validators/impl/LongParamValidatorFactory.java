package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.ValidateLongParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class LongParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateLongParam, Long> {

    @Override
    public Validator<Long> getValidator(ValidateLongParam annotation) {
        LongValidator validator = new LongValidator(annotation.name());
        validator.setNotNull(annotation.notNull());
        validator.setMinValue(annotation.minValue());
        validator.setMaxValue(annotation.maxValue());

        return validator;
    }

}
