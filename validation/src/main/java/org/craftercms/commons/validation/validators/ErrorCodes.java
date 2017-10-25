/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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
package org.craftercms.commons.validation.validators;

public class ErrorCodes {

    public static final String INVALID_METHOD_PARAMS_ERROR_CODE = "validation.error.invalidMethodParams";
    public static final String INVALID_REQUEST_BODY_ERROR_CODE = "validation.error.invalidRequestBody";

    private ErrorCodes() {
    }

    public static class FieldErrors {

        public static final String UNRECOGNIZED_FIELD_ERROR_CODE = "validation.error.rest.field.unrecognized";
        public static final String MISSING_FIELD_ERROR_CODE = "validation.error.rest.field.missing";

        private FieldErrors() {
        }

    }

    public static class StringErrors {

        public static String NOT_NULL_ERROR_CODE = "validation.error.string.notNull";
        public static String NOT_EMPTY_ERROR_CODE = "validation.error.string.notEmpty";
        public static String NOT_BLANK_ERROR_CODE = "validation.error.string.notBlank";
        public static String MIN_LENGTH_ERROR_CODE = "validation.error.string.minLength";
        public static String MAX_LENGTH_ERROR_CODE = "validation.error.string.maxLength";
        public static String REGEX_VALIDATION_FAILED_ERROR_CODE = "validation.error.string.regexMatchingFailed";

        private StringErrors() {
        }

    }

}
