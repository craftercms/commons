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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.SITE_ID;

public class SiteIdValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(SITE_ID);
    }

    @Test
    public void testSpace() {
        assertRejected("my site");
    }

    @Test
    public void testStartWithDigit() {
        assertValid("1mysite");
    }

    @Test
    public void testStartWithDigit2() {
        assertValid("7s1t3s");
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
        assertRejected("invalid;");
    }

    @Test
    public void testUnderscore() {
        assertValid("my_site");
    }

    @Test
    public void testDot() {
        assertRejected("my.site");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
