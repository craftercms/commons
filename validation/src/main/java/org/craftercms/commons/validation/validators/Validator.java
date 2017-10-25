package org.craftercms.commons.validation.validators;

import java.util.ResourceBundle;

import org.craftercms.commons.validation.ValidationResult;

public interface Validator<T> {

    void validate(T target, ValidationResult result);

    void validate(T target, ValidationResult result, ResourceBundle errorMessageBundle);

}
