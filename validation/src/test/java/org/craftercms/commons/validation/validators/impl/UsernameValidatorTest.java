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

import org.craftercms.commons.validation.validators.ValidatorTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Validator;

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.USERNAME;

public class UsernameValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(USERNAME);
    }

    @Test
    public void testDash() {
        assertValid("john-wick");
    }

    @Test
    public void testStartWithDigit() {
        assertRejected("1st_john");
    }

    @Test
    public void testStartWithDigit2() {
        assertRejected("7s1t3s");
    }

    @Test
    public void testSpaces() {
        assertRejected("johnny mnemonic");
    }

    @Test
    public void testSpecialCharsTags() {
        assertRejected("<malicious>");
    }

    @Test
    public void testSpecialChars() {
        assertRejected("valid_but;");
    }

    @Test
    public void testOnlyLetters() {
        assertValid("admin");
    }

    @Test
    public void testLettersThenDigits() {
        assertValid("bob123");
    }

    @Test
    public void testUnderscore() {
        assertValid("john_wick");
    }

    @Test
    public void testDot() {
        assertValid("john.wick");
    }

    @Test
    public void testEmail() {
        assertValid("john@wick.com");
    }

    @Test
    public void testDoubleAtEmail() {
        assertValid("john@wi@ck.com");
    }

    @Test
    public void testEmailWithSpaces() {
        assertRejected("john@wic k.com");
    }

    @Test
    public void testHash() {
        assertRejected("john#wi@ck.com");
    }

    @Test
    public void testHash2() {
        assertRejected("john@wi#ck.com");
    }

    @Test
    public void testAtSign() {
        assertValid("john@");
    }

    @Test
    public void testPlusSign() {
        assertValid("john+wick");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
