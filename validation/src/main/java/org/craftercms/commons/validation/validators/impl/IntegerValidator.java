package org.craftercms.commons.validation.validators.impl;

public class IntegerValidator extends AbstractNumberValidator<Integer> {

    public IntegerValidator(String targetKey) {
        super(targetKey);

        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }

    @Override
    protected boolean isLessThanMinValue(Integer target) {
        return target < minValue;
    }

    @Override
    protected boolean isGreaterThanMaxValue(Integer target) {
        return target > maxValue;
    }

}
