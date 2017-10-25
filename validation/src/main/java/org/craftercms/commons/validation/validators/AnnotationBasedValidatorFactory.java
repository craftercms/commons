package org.craftercms.commons.validation.validators;

import java.lang.annotation.Annotation;

public interface AnnotationBasedValidatorFactory<A extends Annotation, T> {

    Validator<T> getValidator(A annotation);

}
