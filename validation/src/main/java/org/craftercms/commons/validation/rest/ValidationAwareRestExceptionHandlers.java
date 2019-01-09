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
package org.craftercms.commons.validation.rest;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.ResourceBundle;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.craftercms.commons.rest.BaseRestExceptionHandlers;
import org.craftercms.commons.validation.ValidationException;
import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.craftercms.commons.validation.ErrorCodes.FIELD_MISSING_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.FIELD_UNRECOGNIZED_ERROR_CODE;
import static org.craftercms.commons.validation.ErrorCodes.INVALID_REQUEST_BODY_ERROR_CODE;

@ControllerAdvice
public class ValidationAwareRestExceptionHandlers extends BaseRestExceptionHandlers {

    protected ResourceBundle errorMessageBundle;

    public void setErrorMessageBundle(ResourceBundle errorMessageBundle) {
        this.errorMessageBundle = errorMessageBundle;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest webRequest) {
        Throwable cause = ExceptionUtils.getRootCause(ex);
        if (cause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException upe = (UnrecognizedPropertyException)cause;
            String field = upe.getPropertyName();

            ValidationResult result = new ValidationResult();
            result.addError(field, ValidationUtils.getErrorMessage(errorMessageBundle, FIELD_UNRECOGNIZED_ERROR_CODE));

            return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        } else {
            String message = ValidationUtils.getErrorMessage(errorMessageBundle, INVALID_REQUEST_BODY_ERROR_CODE);

            return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ValidationResult result = new ValidationResult();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            result.addError(fieldError.getField(), ValidationUtils.getErrorMessage(errorMessageBundle, FIELD_MISSING_ERROR_CODE));
        }

        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest webRequest) {
        Throwable cause = ex.getCause();
        if (cause instanceof ValidationException) {
            return handleValidationException((ValidationException)cause, webRequest);
        } else {
            return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
        }
    }

}
