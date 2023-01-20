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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.GROUP_NAME;
import static org.craftercms.commons.validation.util.ValidationTestUtils.isValid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupNameValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(GROUP_NAME);
    }

    @Test
    public void testStartWithDigits() {
        assertFalse(isValid(validator, "1st_publisher"));
    }

    @Test
    public void testSpaces() {
        assertFalse(isValid(validator, "SITE reviewer"));
    }

    @Test
    public void testStartWithDigits2() {
        assertFalse(isValid(validator, "7s1t3s"));
    }

    @Test
    public void testSpecialChars() {
        assertFalse(isValid(validator, "administrators;"));
    }

    @Test
    public void testSpecialCharsTags() {
        assertFalse(isValid(validator, "<malicious>"));
    }

    @Test
    public void testUnderscore() {
        assertTrue(isValid(validator, "regional_reviewer"));
    }

    @Test
    public void testUnderscoreAndDigits() {
        assertTrue(isValid(validator, "site_admin3"));
    }

    @Test
    public void testOnlyLetters() {
        assertTrue(isValid(validator, "reviewer"));
    }

    @Test
    public void testDigits() {
        assertTrue(isValid(validator, "s1t3s"));
    }

    @Test
    public void testDigits2() {
        assertTrue(isValid(validator, "a1d"));
    }

    @Test
    public void testDash() {
        assertTrue(isValid(validator, "division-owner"));
    }
}
