package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.annotations.param.SqlSort;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static org.craftercms.commons.validation.ErrorCodes.SQL_SORT_VALIDATION_FAILED_ERROR_CODE;

/**
 * Validates that a String is a list of "column order(optional)" pairs, where column
 * is one of the allowed values in columns parameter.
 */
public class SqlSortValidator implements ConstraintValidator<SqlSort, String>, Validator {

    private static final String ORDER_REGEX = "(?i)(asc|desc)?";

    private final Pattern orderPattern;
    private List<String> columns;


    public SqlSortValidator() {
        orderPattern = Pattern.compile(ORDER_REGEX);
    }

    public SqlSortValidator(final List<String> columns) {
        this();
        setColumns(columns);
    }

    @Override
    public void initialize(SqlSort sqlSort) {
        setColumns(List.of(sqlSort.columns().split("\\s+")));
    }

    private void setColumns(final List<String> columns) {
        this.columns = columns.stream().map(String::toLowerCase).collect(toList());
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value == null || areColumnsValid(value);
    }

    private boolean areColumnsValid(final @NonNull String value) {
        String[] columnOrderPairs = value.toLowerCase().split(",");
        return Arrays.stream(columnOrderPairs)
                .map(String::trim)
                .allMatch(this::isValidPair);
    }

    private boolean isValidPair(String columnSort) {
        String[] columnOrderPair = columnSort.split("\\s+");
        if (columnOrderPair.length == 0 || columnOrderPair.length > 2) {
            return false;
        }
        String columnName = columnOrderPair[0];
        String order = "";
        if (columnOrderPair.length > 1) {
            order = columnOrderPair[1];
        }
        if (!columns.contains(columnName)) {
            return false;
        }
        if (columnOrderPair.length > 1) {
            return orderPattern.matcher(order).matches();
        }
        return true;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object value, @NonNull Errors errors) {
        if (!areColumnsValid((String) value)) {
            errors.reject(SQL_SORT_VALIDATION_FAILED_ERROR_CODE);
        }

    }
}
