/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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

import org.craftercms.commons.validation.annotations.param.EsapiValidatedParam;
import org.craftercms.commons.validation.annotations.param.EsapiValidationType;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.craftercms.commons.validation.ErrorCodes.getEsapiErrorMessageKey;
import static org.craftercms.commons.validation.ValidationUtils.getDefaultErrorMessageBundle;
import static org.craftercms.commons.validation.ValidationUtils.getErrorMessage;
import static org.owasp.esapi.StringUtilities.isEmpty;

/**
 * {@link StringValidator} extension that validates the parameter value
 * against {@link ESAPI} validator(), using the annotation 'type' property.
 * 'type' property corresponds to the pattern indicated by the property
 * <code>Validator.&lt;type&gt;</code> in ESAPI.properties file.
 */
public class EsapiValidator implements ConstraintValidator<EsapiValidatedParam, String>, org.springframework.validation.Validator {

    private static final Logger logger = LoggerFactory.getLogger(EsapiValidator.class);

    private final Validator validator;
    private EsapiValidationType type;

    public EsapiValidator() {
        validator = ESAPI.validator();
    }

    public EsapiValidator(final EsapiValidationType type) {
        this();
        this.type = type;
    }

    @Override
    public void initialize(EsapiValidatedParam annotation) {
        this.type = annotation.type();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        boolean isValid = false;
        String esapiType = type.typeKey;
        try {
            isValid = validator.isValidInput(esapiType, value, esapiType, Integer.MAX_VALUE, true);
        } catch (IntrusionException e) {
            // TODO: JM: Revisit how to get param name
            logger.warn("Potential attack attempt detected while validating input", e);
        }
        String messageTemplate = context.getDefaultConstraintMessageTemplate();
        if (!isValid && isEmpty(messageTemplate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(getErrorMessage(getDefaultErrorMessageBundle(), getEsapiErrorMessageKey(esapiType)))
                    .addConstraintViolation();
        }
        return isValid;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object value, @NonNull Errors errors) {
        String esapiType = type.typeKey;
        if (!validator.isValidInput(esapiType, (String) value, esapiType, Integer.MAX_VALUE, true)) {
            errors.reject(getEsapiErrorMessageKey(esapiType));
        }
    }
}
