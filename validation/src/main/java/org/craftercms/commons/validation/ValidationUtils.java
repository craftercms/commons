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

import java.util.ResourceBundle;

import org.craftercms.commons.i10n.I10nUtils;

public class ValidationUtils {

    public static final String DEFAULT_ERROR_MESSAGE_BUNDLE_NAME = "crafter.commons.validation.errors";

    private ValidationUtils() {
    }

    public static ResourceBundle getDefaultErrorMessageBundle() {
        return ResourceBundle.getBundle(DEFAULT_ERROR_MESSAGE_BUNDLE_NAME);
    }

    public static String getErrorMessage(ResourceBundle messageBundle, String errorCode, Object... args) {
        if (messageBundle == null) {
            messageBundle = getDefaultErrorMessageBundle();
        }

        return I10nUtils.getLocalizedMessage(messageBundle, errorCode, args);
    }

}
