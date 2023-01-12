/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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

import org.craftercms.commons.validation.ValidationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SecurePathValidatorTest {

    private static final String[] VALID_PATHS = {"/site/website", "/site/website/index.xml", "/site/website/.folder/index.xml",
            "/site/website/..folder/index.xml", "/site/website/~folder/index.xml", "/site/website/folder:/index.xml"};
    private static final String[] INVALID_PATHS = {".", "..", "...", "./folder", "folder/.", "../folder", "folder/..",
            "folder/./folder", "folder/../folder", "~/folder", "folder/~", "folder/~/folder", "C:/Program Files"};

    private Validator validator;

    @Before
    public void setUp() {
        validator = new SecurePathValidator();
    }

    @Test
    public void testValidPaths() {
        for (String path : VALID_PATHS) {
            Errors errors = ValidationUtils.validateValue(validator, path);
            assertFalse("Validation of " + path, errors.hasErrors());
        }
    }

    @Test
    public void testInvalidPaths() {
        for (String path : INVALID_PATHS) {
            Errors errors = ValidationUtils.validateValue(validator, path);
            assertTrue("Validation of " + path, errors.hasErrors());
        }
    }

}
