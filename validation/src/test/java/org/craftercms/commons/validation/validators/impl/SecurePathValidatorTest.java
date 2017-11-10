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
