package org.craftercms.commons.validation.validators.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.craftercms.commons.validation.annotations.param.ValidateSecurePathParam;
import org.craftercms.commons.validation.annotations.param.ValidateStringParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

public class AnnotationBasedValidatorFactoryRegistry implements AnnotationBasedValidatorFactory<Annotation, Object> {
    
    protected Map<Class<?>, AnnotationBasedValidatorFactory> registry;

    public AnnotationBasedValidatorFactoryRegistry() {
        registry = new HashMap<>(2);
        registry.put(ValidateStringParam.class, new StringParamValidatorFactory());
        registry.put(ValidateSecurePathParam.class, new SecurePathParamValidatorFactory());
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
