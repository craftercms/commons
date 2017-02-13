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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.craftercms.commons.i10n.I10nUtils;

/**
 * DTO returned by classes that perform validation to indicate a result, generally when there are field errors.
 *
 * @author avasquez
 */
public class ValidationFailure {

    protected ResourceBundle bundle;
    protected String message;
    protected List<FieldError> fieldErrors;

    public ValidationFailure() {
        this(ValidationConstants.MSG_KEY_VALIDATION_FAILED);
    }

    public ValidationFailure(String key, Object... args) {
        this(ResourceBundle.getBundle(I10nUtils.DEFAULT_ERROR_BUNDLE_NAME), key, args);
    }

    public ValidationFailure(ResourceBundle bundle, String key, Object... args) {
        this.bundle = bundle;
        this.message = I10nUtils.getLocalizedMessage(bundle, key, args);
        this.fieldErrors = new ArrayList<>();
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("field_errors")
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String key, Object... args) {
        fieldErrors.add(new FieldError(field, I10nUtils.getLocalizedMessage(bundle, key, args)));
    }

}
