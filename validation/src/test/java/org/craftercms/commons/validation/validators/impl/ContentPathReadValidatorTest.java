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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.CONTENT_PATH_READ;

public class ContentPathReadValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(CONTENT_PATH_READ);
    }

    @Test
    public void testSpace() {
        assertValid("site/website/folder 1/index.xml");
    }

    @Test
    public void testStartWithDigit() {
        assertValid("1st_folder/path/");
    }

    @Test
    public void testStartWithDigit2() {
        assertValid("7s1t3s/and/more");
    }

    @Test
    public void testSpaces() {
        assertValid("johnny mnemonic");
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
        assertValid("/site/website/_articles/folder_1");
    }

    @Test
    public void testDot() {
        assertValid("/site/components/path.to.content");
    }

    @Test
    public void testParenthesis() {
        assertValid("/site/website/another(1)");
    }

    @Test
    public void testParenthesis2() {
        assertValid("/site/website/another(2)/file.html");
    }

    @Test
    public void testExclamationMark() {
        assertRejected("/site/website/another!file/");
    }

    @Test
    public void testQuestionMark() {
        assertRejected("/site/website/?another2/");
    }

    @Test
    public void testTrailingSpaces() {
        assertRejected("/site/website/path ");
    }

    @Test
    public void testLeadingSpaces() {
        assertRejected(" /site/website/path");
    }

    @Test
    public void testBlank() {
        assertRejected(" ");
    }

    @Test
    public void testRoot() {
        assertValid("/");
    }

    @Test
    public void testShortFolderNames() {
        assertValid("/site/website/articles/2021/3/top-clubs-in-virginia/index.xml");
    }

    @Test
    public void testMultiSlash() {
        assertValid("/site//components/path//to/content/index.xml");
    }

    @Test
    public void testHash() {
        assertRejected("/site/website/folder#hash/to/content/index.xml");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
