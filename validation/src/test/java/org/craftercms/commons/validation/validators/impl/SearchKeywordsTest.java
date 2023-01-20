package org.craftercms.commons.validation.validators.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Validator;

import static org.craftercms.commons.validation.annotations.param.EsapiValidationType.USERNAME;
import static org.craftercms.commons.validation.util.ValidationTestUtils.isValid;
import static org.junit.Assert.assertFalse;

public class SearchKeywordsTest {
    private Validator validator;

    @Before
    public void setUp() {
        validator = new EsapiValidator(USERNAME);
    }

    @Test
    public void testSpecialChars() {
        assertFalse(isValid(validator, "<invalid>"));
    }

    @Test
    public void testUnderscore() {
        assertFalse(isValid(validator, "_invalid_"));
    }

    @Test
    public void testDash() {
        assertFalse(isValid(validator, "this-is-valid"));
    }

    @Test
    public void testSpaces() {
        assertFalse(isValid(validator, "also this is valid"));
    }


}
