package org.craftercms.commons.validation.validators.impl;

public class DoubleValidator extends AbstractNumberValidator<Double> {

    public DoubleValidator(String targetKey) {
        super(targetKey);

        this.minValue = Double.MIN_VALUE;
        this.maxValue = Double.MAX_VALUE;
    }

    @Override
    protected boolean isLessThanMinValue(Double target) {
        return target < minValue;
    }

    @Override
    protected boolean isGreaterThanMaxValue(Double target) {
        return target > maxValue;
    }

}
