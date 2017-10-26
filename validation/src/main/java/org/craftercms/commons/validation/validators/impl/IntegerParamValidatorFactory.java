package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.ValidateIntegerParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class IntegerParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateIntegerParam, Integer> {

    @Override
    public Validator<Integer> getValidator(ValidateIntegerParam annotation) {
        IntegerValidator validator = new IntegerValidator(annotation.name());
        validator.setNotNull(annotation.notNull());
        validator.setMinValue(annotation.minValue());
        validator.setMaxValue(annotation.maxValue());

        return validator;
    }

}
