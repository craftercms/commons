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

import org.craftercms.commons.validation.annotations.param.*;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationBasedValidatorFactoryRegistry implements AnnotationBasedValidatorFactory<Annotation, Object> {

    protected Map<Class<?>, AnnotationBasedValidatorFactory> registry;

    public AnnotationBasedValidatorFactoryRegistry() {
        registry = new HashMap<>();
        registry.put(ValidateStringParam.class, new StringParamValidatorFactory());
        registry.put(ValidateIntegerParam.class, new IntegerParamValidatorFactory());
        registry.put(ValidateLongParam.class, new LongParamValidatorFactory());
        registry.put(ValidateDoubleParam.class, new DoubleParamValidatorFactory());
        registry.put(ValidateSecurePathParam.class, new SecurePathParamValidatorFactory());
        registry.put(ValidateNoTagsParam.class, new NoTagsParamValidatorFactory());
        registry.put(EsapiValidatedParam.class, new EsapiValidatorFactory());
        registry.put(ValidateCollectionParam.class, new CollectionParamValidatorFactory());
        registry.put(ValidateObjectParam.class, new ObjectParamValidatorFactory(this));
    }

    public void setRegistry(Map<Class<?>, AnnotationBasedValidatorFactory> registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Validator<Object> getValidator(Annotation annotation, String paramName) {
        AnnotationBasedValidatorFactory validatorFactory = registry.get(annotation.annotationType());
        if (validatorFactory != null) {
            return validatorFactory.getValidator(annotation, paramName);
        }

        return null;
    }

}
