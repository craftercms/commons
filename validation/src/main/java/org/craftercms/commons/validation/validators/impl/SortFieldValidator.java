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

import org.craftercms.commons.rest.parameters.SortField;
import org.craftercms.commons.validation.annotations.param.SqlSort;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.craftercms.commons.validation.ErrorCodes.SQL_SORT_VALIDATION_FAILED_ERROR_CODE;

/**
 * Validates a {@link SortField} object.
 * Validates that the field is one of the allowed values in columns parameter
 */
public class SortFieldValidator implements ConstraintValidator<SqlSort, SortField>, Validator {

    private List<String> columns;

    @Override
    public void initialize(SqlSort sqlSort) {
        setColumns(List.of(sqlSort.columns().split("\\s+")));
    }

    private void setColumns(final List<String> columns) {
        this.columns = columns.stream().map(String::toLowerCase).collect(toList());
    }

    @Override
    public boolean isValid(SortField value, ConstraintValidatorContext context) {
        return value == null || doValidate(value);
    }

    private boolean doValidate(final @NonNull SortField value) {
        String columnName = value.getField().toLowerCase();

        return columns.contains(columnName);
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return SortField.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (!doValidate((SortField) target)) {
            errors.reject(SQL_SORT_VALIDATION_FAILED_ERROR_CODE);
        }
    }
}
