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
package org.craftercms.commons.validation;

public class ErrorCodes {

    /*
     * Common errors
     */
    public static final String NOT_NULL_ERROR_CODE = "validation.error.notNull";
    public static final String INVALID_METHOD_PARAMS_ERROR_CODE = "validation.error.invalidMethodParams";
    public static final String INVALID_REQUEST_BODY_ERROR_CODE = "validation.error.invalidRequestBody";

    /*
     * Field errors
     */
    public static final String FIELD_UNRECOGNIZED_ERROR_CODE = "validation.error.rest.field.unrecognized";
    public static final String FIELD_MISSING_ERROR_CODE = "validation.error.rest.field.missing";

    /*
     * String errors
     */
    public static final String STRING_NOT_EMPTY_ERROR_CODE = "validation.error.string.notEmpty";
    public static final String STRING_NOT_BLANK_ERROR_CODE = "validation.error.string.notBlank";
    public static final String STRING_MIN_LENGTH_ERROR_CODE = "validation.error.string.minLength";
    public static final String STRING_MAX_LENGTH_ERROR_CODE = "validation.error.string.maxLength";
    public static final String STRING_REGEX_VALIDATION_FAILED_ERROR_CODE = "validation.error.string.regexMatchingFailed";

    /*
     * Number errors
     */
    public static final String NUMBER_MIN_VALUE_ERROR = "validation.error.number.minValue";
    public static final String NUMBER_MAX_VALUE_ERROR = "validation.error.number.maxValue";

    private ErrorCodes() {
    }

}
