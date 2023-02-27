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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.CONTENT_FILE_NAME_WRITE;

public class ContentNewFilenameValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(CONTENT_FILE_NAME_WRITE);
    }

    @Test
    public void testXml() {
        assertValid("index.xml");
    }

    @Test
    public void testSlash() {
        assertRejected("path/file.xml");
    }

    @Test
    public void testSpaces() {
        assertRejected("my file.xml");
    }

    @Test
    public void testDots() {
        assertRejected("my.file.xml");
    }

    @Test
    public void testNonXml() {
        assertRejected("my.txt");
    }

    @Test
    public void testLevelDescriptor() {
        assertValid("crafter-level-descriptor.level.xml");
    }

    @Test
    public void testLevelDescriptor2() {
        assertRejected("something_else_crafter-level-descriptor.level.xml");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

}
