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
package org.craftercms.commons.validation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class ValidationResult {

    protected String message;
    protected Map<String, String> errors;

    public ValidationResult() {
        this.errors = new HashMap<>();
    }

    public ValidationResult(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasErrors() {
        return MapUtils.isNotEmpty(errors);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String key, String message) {
        errors.put(key, message);
    }

}
