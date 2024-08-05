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

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.CONTENT_PATH_WRITE;

public class ContentNewPathValidatorTest implements ValidatorTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(CONTENT_PATH_WRITE);
    }

    @Test
    public void testSpace() {
        assertValid("site/website/folder 1/index.xml");
    }

    @Test
    public void testSpace2() {
        assertValid("static-assets/images/my picture.png");
    }

    @Test
    public void testSpace3() {
        assertValid("johnny mnemonic");
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
    public void testMultipleDotsInFilename() {
        assertValid("/site/components/path.to.content");
    }

    @Test
    public void testMultipleDotsInPath() {
        assertValid("/site/compo.ne.nts/path-to.content.xml");
    }

    @Test
    public void testIndexXml() {
        assertValid("/site/components/path/to/content/index.xml");
    }

    @Test
    public void testMultiSlash() {
        assertRejected("/site//components/path//to/content/index.xml");
    }

    @Test
    public void testMixedCase() {
        assertValid("/site/website/DOCS/to/content/index.xml");
    }

    @Test
    public void testHash() {
        assertRejected("/site/website/folder#hash/to/content/index.xml");
    }

    @Test
    public void testInvalidPathTraversal() {
        assertRejected("../../site/website");
        assertRejected("./site/website");
        assertRejected("/site/website/../../sample");
    }

    @Test
    public void testValidCurlyBraces() {
        assertValid("/scripts/rest/{version}");
        assertValid("/site/website/{version}/index.xml");
        assertValid("/site/{a}/{b}/{c}/sample.xml");
        assertValid("/site/component/{abc}-sample.xml");
        assertValid("/site/component/{version}");
        assertValid("/site/component/{version.xml}/abc/xyz");
        assertValid("/site/component/{version}");
        assertValid("/site/component/{a.sample.version}");
        assertValid("/site/a.sample.component/{version}");
        assertValid("/site/component/{index.xml}");
        assertValid("{version}");
        assertValid("{version}/{version}/component/sample/{new.version}/index.xml");
        assertValid("{version}/component");
    }

    @Test
    public void testInvalidCurlyBraces() {
        assertRejected("/site/website/{version");
        assertRejected("/site/website/version}");
        assertRejected("/site/website/{{version}}");
        assertRejected("/site/website/{version}/sample/{newVersion");
        assertRejected("{{version}}");
        assertRejected("{version/sample/path}");
        assertRejected("/sample/path/{with{wrong{curl{brances}}}");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
