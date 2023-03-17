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
package org.craftercms.commons.validation.util;

import org.craftercms.commons.validation.ValidationResult;
import org.craftercms.commons.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Utility for validation tests
 */
public class ValidationTestUtils {

    public static boolean isValid(Validator validator, String value) {
        ValidationResult errors = ValidationUtils.validateValue(validator, value, "key");
        return !errors.hasErrors();
    }

    public static void assertValid(Validator validator, String value){
        assertTrue(isValid(validator, value));
    }

    public static void assertInvalid(Validator validator, String value){
        assertFalse(isValid(validator, value));
    }

}
