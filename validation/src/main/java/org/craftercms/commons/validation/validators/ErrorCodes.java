package org.craftercms.commons.validation.validators;

public class ErrorCodes {

    private ErrorCodes() {
    }

    public static final String INVALID_METHOD_PARAMS_ERROR_CODE = "validation.error.invalidMethodParams";
    public static final String INVALID_REQUEST_BODY_ERROR_CODE = "validation.error.invalidRequestBody";

    public static class FieldErrors {

        private FieldErrors() {
        }

        public static final String UNRECOGNIZED_FIELD_ERROR_CODE = "validation.error.rest.field.unrecognized";
        public static final String MISSING_FIELD_ERROR_CODE = "validation.error.rest.field.missing";

    }

    public static class StringErrors {

        private StringErrors() {
        }

        public static String NOT_NULL_ERROR_CODE = "validation.error.string.notNull";
        public static String NOT_EMPTY_ERROR_CODE = "validation.error.string.notEmpty";
        public static String NOT_BLANK_ERROR_CODE = "validation.error.string.notBlank";
        public static String MIN_LENGTH_ERROR_CODE = "validation.error.string.minLength";
        public static String MAX_LENGTH_ERROR_CODE = "validation.error.string.maxLength";
        public static String REGEX_VALIDATION_FAILED_ERROR_CODE = "validation.error.string.regexMatchingFailed";

    }

}
