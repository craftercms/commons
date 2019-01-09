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
package org.craftercms.commons.validation.validators.impl;

import org.craftercms.commons.validation.ValidationResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecurePathValidatorTest {

    private static final String[] VALID_PATHS = {"/site/website", "/site/website/index.xml", "/site/website/.folder/index.xml",
        "/site/website/..folder/index.xml", "/site/website/~folder/index.xml", "/site/website/folder:/index.xml"};
    private static final String[] INVALID_PATHS = {".", "..", "...", "./folder", "folder/.", "../folder", "folder/..",
        "folder/./folder", "folder/../folder", "~/folder", "folder/~", "folder/~/folder", "C:/Program Files"};

    private SecurePathValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new SecurePathValidator("path");
    }

    @Test
    public void testValidPaths() throws Exception {
        for (String path : VALID_PATHS) {
            assertTrue("Validation of " + path, validator.validate(path, new ValidationResult()));
        }
    }

    @Test
    public void testInvalidPaths() throws Exception {
        for (String path : INVALID_PATHS) {
            assertFalse("Validation of " + path, validator.validate(path, new ValidationResult()));
        }
    }

}
