/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static java.lang.String.format;
import static org.craftercms.commons.validation.ErrorCodes.OBJECT_FIELD_READ_ERROR_CODE;
import static org.craftercms.commons.validation.ValidationUtils.validateValue;

/**
 * {@link org.craftercms.commons.validation.validators.Validator} implementation that
 * scans an object declared fields and trigger their validation if annotated with
 * validation annotations.
 */
public class ObjectParamValidator extends BasicValidator<Object> {

    private final AnnotationBasedValidatorFactory<Annotation, Object> validatorFactory;

    public ObjectParamValidator(String targetKey, final AnnotationBasedValidatorFactory<Annotation, Object> validatorFactory) {
        super(targetKey);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public boolean validate(final Object target, final ValidationResult result) {
        if (!super.validate(target, result)) {
            return false;
        }
        if (target == null) {
            return true;
        }
        boolean isValid = true;
        Field[] declaredFields = target.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] annotations = declaredField.getAnnotations();
            String declaredName = format("%s.%s", targetKey, declaredField.getName());
            for (Annotation annotation : annotations) {
                Validator<Object> validator = validatorFactory.getValidator(annotation, declaredName);
                if (validator != null) {
                    try {
                        declaredField.setAccessible(true);
                        Object fieldValue = declaredField.get(target);
                        isValid &= validateValue(validator, fieldValue, result, declaredField.getType());
                    } catch (IllegalAccessException e) {
                        result.addError(targetKey, OBJECT_FIELD_READ_ERROR_CODE);
                        isValid = false;
                    }
                }
            }
        }

        return isValid;
    }

}
