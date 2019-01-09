/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.craftercms.commons.aop.AopUtils;
import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationRuntimeException;
import org.craftercms.commons.validation.ValidationUtils;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;
import org.springframework.beans.factory.annotation.Required;

import static org.craftercms.commons.validation.ErrorCodes.INVALID_METHOD_PARAMS_ERROR_CODE;

@Aspect
public class ValidateParamsAspect {

    protected ResourceBundle errorMessageBundle;

    protected AnnotationBasedValidatorFactory<Annotation, Object> validatorFactory;

    public void setErrorMessageBundle(ResourceBundle errorMessageBundle) {
        this.errorMessageBundle = errorMessageBundle;
    }

    @Required
    @SuppressWarnings("unchecked")
    public void setValidatorFactory(AnnotationBasedValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Before("@within(org.craftercms.commons.validation.annotations.param.ValidateParams) || " +
            "@annotation(org.craftercms.commons.validation.annotations.param.ValidateParams)")
    public void doValidation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = AopUtils.getActualMethod(joinPoint);
        Annotation[][] allParamAnnotations = method.getParameterAnnotations();
        ValidationResult result = new ValidationResult(errorMessageBundle);

        if (ArrayUtils.isNotEmpty(allParamAnnotations)) {
            for (int i = 0; i < args.length; i++) {
                Object param = args[i];
                Annotation[] paramAnnotations = allParamAnnotations[i];

                for (Annotation annotation : paramAnnotations) {
                    validateParam(annotation, param, result);
                }
            }
        }

        if (result.hasErrors()) {
            String methodStr = method.toGenericString();

            result.setMessage(ValidationUtils.getErrorMessage(errorMessageBundle, INVALID_METHOD_PARAMS_ERROR_CODE, methodStr));

            throw new ValidationRuntimeException(result);
        }
    }

    protected void validateParam(Annotation annotation, Object param, ValidationResult result) {
        Validator<Object> validator = validatorFactory.getValidator(annotation);
        if (validator != null) {
            validator.validate(param, result);
        }
    }

}
