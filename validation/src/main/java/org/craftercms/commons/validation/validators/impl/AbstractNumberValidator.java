package org.craftercms.commons.validation.validators.impl;

import java.util.ResourceBundle;

import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;

import static org.craftercms.commons.validation.validators.ErrorCodes.NUMBER_MAX_VALUE_ERROR;
import static org.craftercms.commons.validation.validators.ErrorCodes.NUMBER_MIN_VALUE_ERROR;

public abstract class AbstractNumberValidator<T extends Number> extends BasicValidator<T> {

    protected T minValue;
    protected T maxValue;

    public AbstractNumberValidator(String targetKey) {
        super(targetKey);
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean validate(T target, ValidationResult result, ResourceBundle errorMessageBundle) {
        if (!super.validate(target, result, errorMessageBundle)) {
            return false;
        } else if (target != null && isLessThanMinValue(target)) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, NUMBER_MIN_VALUE_ERROR));
            return false;
        } else if (target != null && isGreaterThanMaxValue(target)) {
            result.addError(targetKey, ValidationUtils.getErrorMessage(errorMessageBundle, NUMBER_MAX_VALUE_ERROR));
            return false;
        } else {
            return true;
        }
    }

    protected abstract boolean isLessThanMinValue(T target);

    protected abstract boolean isGreaterThanMaxValue(T target);

}
