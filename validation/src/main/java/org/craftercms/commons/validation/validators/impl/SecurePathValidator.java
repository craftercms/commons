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

import org.craftercms.commons.validation.annotations.param.ValidateSecurePathParam;

import jakarta.validation.ConstraintValidator;
import java.util.List;

public class SecurePathValidator extends AbstractStringValidator implements ConstraintValidator<ValidateSecurePathParam, String> {

    public static final String[] DEFAULT_BLACKLISTED_REGEXES =
            {"^[^:\\/]+:", "^(\\.+|~)$", "^(\\.+|~)[\\/]", "[\\/](\\.+|~)$", "[\\/](\\.+|~)[\\/]"};

    public SecurePathValidator() {
        matchFullInput = false;
        blacklistRegexes = List.of(DEFAULT_BLACKLISTED_REGEXES);
    }
}
