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
package org.craftercms.commons.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class that provides the basic {@link ExceptionHandler}s for Crafter REST services.
 *
 * @author avasquez
 */
@ControllerAdvice
public class BaseRestExceptionHandlers extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(BaseRestExceptionHandlers.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, String message, HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        return handleExceptionInternal(ex, new Result(message), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        logger.error("Request '{}' failed with status '{}'", ((ServletWebRequest) request).getRequest().getRequestURI(), statusCode, ex);

        if (body == null) {
            body = new Result(ex.getMessage());
        }

        return new ResponseEntity<>(body, headers, statusCode);
    }

}
