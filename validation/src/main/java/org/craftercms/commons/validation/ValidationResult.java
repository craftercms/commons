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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections4.MapUtils;

public class ValidationResult {

    protected String message;
    protected ResourceBundle errorMessageBundle;
    protected Map<String, String> errors;

    public ValidationResult() {
        this("");
    }

    public ValidationResult(String message) {
        this(message, ValidationUtils.getDefaultErrorMessageBundle());
    }

    public ValidationResult(ResourceBundle errorMessageBundle) {
        this("", errorMessageBundle);
    }

    public ValidationResult(String message, ResourceBundle errorMessageBundle) {
        this.message = message;
        this.errorMessageBundle = errorMessageBundle;
        this.errors = new HashMap<>();
    }


    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public boolean hasErrors() {
        return MapUtils.isNotEmpty(errors);
    }

    @JsonProperty("errors")
    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String key, String errorCode, Object... args) {
        errors.put(key, ValidationUtils.getErrorMessage(errorMessageBundle, errorCode, args));
    }

}
