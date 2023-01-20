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

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Validator;

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.USERNAME;
import static org.craftercms.commons.validation.util.ValidationTestUtils.isValid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UsernameValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(USERNAME);
    }

    @Test
    public void testDash() {
        assertFalse(isValid(validator, "john-wick"));
    }

    @Test
    public void testStartWithDigit() {
        assertFalse(isValid(validator, "1st_john"));
    }

    @Test
    public void testStartWithDigit2() {
        assertFalse(isValid(validator, "7s1t3s"));
    }

    @Test
    public void testSpaces() {
        assertFalse(isValid(validator, "johnny mnemonic"));
    }

    @Test
    public void testSpecialCharsTags() {
        assertFalse(isValid(validator, "<malicious>"));
    }

    @Test
    public void testSpecialChars() {
        assertFalse(isValid(validator, "valid_but;"));
    }

    @Test
    public void testOnlyLetters() {
        assertTrue(isValid(validator, "admin"));
    }

    @Test
    public void testLettersThenDigits() {
        assertTrue(isValid(validator, "bob123"));
    }

    @Test
    public void testUnderscore() {
        assertTrue(isValid(validator, "john_wick"));
    }

    @Test
    public void testDot() {
        assertTrue(isValid(validator, "john.wick"));
    }

}
