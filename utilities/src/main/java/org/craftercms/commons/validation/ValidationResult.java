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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * DTO returned by classes that perform validation to indicate a result, generally when there are field errors.
 *
 * @author avasquez
 */
public class ValidationResult {

    protected ResourceBundle bundle;
    protected String message;
    protected List<FieldError> fieldErrors;

    public ValidationResult() {
        this(ResourceBundle.getBundle(I10nUtils.DEFAULT_ERROR_BUNDLE_NAME));
    }

    public ValidationResult(ResourceBundle bundle) {
        this.bundle = bundle;
        this.fieldErrors = new ArrayList<>();
    }

    @JsonProperty("message")
    public String getMessage() {
        if (StringUtils.isEmpty(message) && CollectionUtils.isNotEmpty(fieldErrors)) {
            message = I10nUtils.getLocalizedMessage(bundle, ValidationConstants.VALIDATION_FAILED_MSG_KEY);
        }

        return message;
    }

    public void setMessage(String key) {
        this.message = I10nUtils.getLocalizedMessage(bundle, key);
    }

    public void setMessage(String key, Object... args) {
        this.message = I10nUtils.getLocalizedMessage(bundle, key, args);
    }

    @JsonProperty("field_errors")
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String key, Object... args) {
        fieldErrors.add(new FieldError(field, I10nUtils.getLocalizedMessage(bundle, key, args)));
    }

    public void addMissingFieldError(String field) {
        fieldErrors.add(new FieldError(field, I10nUtils.getLocalizedMessage(bundle, ValidationConstants.MISSING_FIELD_MSG_KEY)));
    }

}
