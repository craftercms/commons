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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.craftercms.commons.aop.AopUtils;
import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationRuntimeException;
import org.craftercms.commons.validation.ValidationUtils;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;
import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ResourceBundle;

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
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        Method method = AopUtils.getActualMethod(joinPoint);
        ValidationResult result = new ValidationResult(errorMessageBundle);

        Parameter[] methodParameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            Object param = args[i];
            Parameter methodParameter = methodParameters[i];
            Annotation[] paramAnnotations = methodParameter.getAnnotations();
            for (Annotation annotation : paramAnnotations) {
                Validator<Object> validator = validatorFactory.getValidator(annotation, paramNames[i]);
                if (validator != null) {
                    ValidationUtils.validateValue(validator, param, result, methodParameter.getType());
                }
            }
        }

        if (result.hasErrors()) {
            String methodStr = method.toGenericString();

            result.setMessage(ValidationUtils.getErrorMessage(errorMessageBundle, INVALID_METHOD_PARAMS_ERROR_CODE, methodStr));

            throw new ValidationRuntimeException(result);
        }
    }

}
