package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.ValidateDoubleParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class DoubleParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateDoubleParam, Double> {

    @Override
    public Validator<Double> getValidator(ValidateDoubleParam annotation) {
        DoubleValidator validator = new DoubleValidator(annotation.name());
        validator.setNotNull(annotation.notNull());
        validator.setMinValue(annotation.minValue());
        validator.setMaxValue(annotation.maxValue());

        return validator;
    }

}
