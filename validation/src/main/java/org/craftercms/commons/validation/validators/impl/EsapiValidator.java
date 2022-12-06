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
import org.craftercms.commons.validation.annotations.param.EsapiValidationType;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.IntrusionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.craftercms.commons.validation.ErrorCodes.getEsapiErrorMessageKey;

/**
 * {@link StringValidator} extension that validates the parameter value
 * against {@link ESAPI} validator(), using the annotation 'type' property.
 * 'type' property corresponds to the pattern indicated by the property
 * <code>Validator.&lt;type&gt;</code> in ESAPI.properties file.
 *
 */
public class EsapiValidator extends StringValidator {

    private static final Logger logger = LoggerFactory.getLogger(EsapiValidator.class);

    private final Validator validator;

    private EsapiValidationType type;

    public EsapiValidator(final String targetKey) {
        super(targetKey);
        validator = ESAPI.validator();
    }

    @Override
    public boolean validate(final String target, final ValidationResult result) {
        return super.validate(target, result) && validateEsapi(target, result);
    }

    private boolean validateEsapi(final String target, final ValidationResult result) {
        boolean isValid = false;
        String esapiType = type.typeKey;
        try {
            isValid = validator.isValidInput(esapiType, target, esapiType, maxLength, !notNull);
        } catch (IntrusionException e) {
            logger.warn("Potential attack attempt detected while validating input for param '{}'", targetKey, e);
        }
        if (!isValid) {
            result.addError(targetKey, getEsapiErrorMessageKey(esapiType));
        }
        return isValid;
    }

    public void setEsapiType(final EsapiValidationType type) {
        this.type = type;
    }
}
