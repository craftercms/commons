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
import java.util.HashMap;
import java.util.Map;

import org.craftercms.commons.validation.annotations.param.ValidateDoubleParam;
import org.craftercms.commons.validation.annotations.param.ValidateIntegerParam;
import org.craftercms.commons.validation.annotations.param.ValidateLongParam;
import org.craftercms.commons.validation.annotations.param.ValidateNoTagsParam;
import org.craftercms.commons.validation.annotations.param.ValidateSecurePathParam;
import org.craftercms.commons.validation.annotations.param.ValidateStringParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class AnnotationBasedValidatorFactoryRegistry implements AnnotationBasedValidatorFactory<Annotation, Object> {
    
    protected Map<Class<?>, AnnotationBasedValidatorFactory> registry;

    public AnnotationBasedValidatorFactoryRegistry() {
        registry = new HashMap<>(2);
        registry.put(ValidateStringParam.class, new StringParamValidatorFactory());
        registry.put(ValidateIntegerParam.class, new IntegerParamValidatorFactory());
        registry.put(ValidateLongParam.class, new LongParamValidatorFactory());
        registry.put(ValidateDoubleParam.class, new DoubleParamValidatorFactory());
        registry.put(ValidateSecurePathParam.class, new SecurePathParamValidatorFactory());
        registry.put(ValidateNoTagsParam.class, new NoTagsParamValidatorFactory());
    }

    public void setRegistry(Map<Class<?>, AnnotationBasedValidatorFactory> registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Validator<Object> getValidator(Annotation annotation) {
        AnnotationBasedValidatorFactory validatorFactory = registry.get(annotation.annotationType());
        if (validatorFactory != null) {
            return validatorFactory.getValidator(annotation);
        } else {
            return null;
        }
    }
    
}
