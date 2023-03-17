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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.GROUP_NAME;

public class GroupNameValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(GROUP_NAME);
    }

    @Test
    public void testStartWithDigits() {
        assertRejected("1st_publisher");
    }

    @Test
    public void testSpaces() {
        assertRejected("SITE reviewer");
    }

    @Test
    public void testStartWithDigits2() {
        assertRejected("7s1t3s");
    }

    @Test
    public void testSpecialChars() {
        assertRejected("administrators;");
    }

    @Test
    public void testSpecialCharsTags() {
        assertRejected("<malicious>");
    }

    @Test
    public void testUnderscore() {
        assertValid("regional_reviewer");
    }

    @Test
    public void testUnderscoreAndDigits() {
        assertValid("site_admin3");
    }

    @Test
    public void testOnlyLetters() {
        assertValid("reviewer");
    }

    @Test
    public void testDigits() {
        assertValid("s1t3s");
    }

    @Test
    public void testDigits2() {
        assertValid("a1d");
    }

    @Test
    public void testDash() {
        assertValid("division-owner");
    }

    @Test
    public void testDot() {
        assertValid("division.owner");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
