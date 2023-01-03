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

import org.craftercms.commons.validation.annotations.param.ValidateObjectParam;
import org.craftercms.commons.validation.validators.AnnotationBasedValidatorFactory;
import org.craftercms.commons.validation.validators.Validator;

import java.lang.annotation.Annotation;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

/**
 * Simple AnnotationBasedValidatorFactory implementation to build a {@link ObjectParamValidator}
 * for an element annotated with {@link ValidateObjectParam}
 */
public class ObjectParamValidatorFactory implements AnnotationBasedValidatorFactory<ValidateObjectParam, Object> {
    private final AnnotationBasedValidatorFactory<Annotation, Object> validatorFactory;

    public ObjectParamValidatorFactory(AnnotationBasedValidatorFactory<Annotation, Object> validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Validator<Object> getValidator(final ValidateObjectParam annotation, final String paramName) {
        return new ObjectParamValidator(defaultIfEmpty(annotation.name(), paramName), validatorFactory);
    }
}
