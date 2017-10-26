package org.craftercms.commons.validation.validators.impl;

public class LongValidator extends AbstractNumberValidator<Long> {

    public LongValidator(String targetKey) {
        super(targetKey);

        this.minValue = Long.MIN_VALUE;
        this.maxValue = Long.MAX_VALUE;
    }

    @Override
    protected boolean isLessThanMinValue(Long target) {
        return target < minValue;
    }

    @Override
    protected boolean isGreaterThanMaxValue(Long target) {
        return target > maxValue;
    }

}
