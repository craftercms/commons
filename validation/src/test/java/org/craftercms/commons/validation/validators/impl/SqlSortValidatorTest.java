package org.craftercms.commons.validation.validators.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Validator;

import java.util.List;

import static org.craftercms.commons.validation.util.ValidationTestUtils.isValid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SqlSortValidatorTest {
    private Validator validator;

    @Before
    public void setUp() {
        validator = new SqlSortValidator(List.of("id", "DATE", "name"));
    }

    @Test
    public void testSingleColumn() {
        assertTrue(isValid(validator, "date"));
    }

    @Test
    public void testSingleColumnAndSort() {
        assertTrue(isValid(validator, "date desc"));
    }

    @Test
    public void testDifferentCase() {
        assertTrue(isValid(validator, "DATE desc"));
    }

    @Test
    public void testDifferentCase2() {
        assertTrue(isValid(validator, "id ASc"));
    }

    @Test
    public void testTwoColumns() {
        assertTrue(isValid(validator, "date, name"));
    }

    @Test
    public void testMultipleColumnAndSorts() {
        assertTrue(isValid(validator, "date asc, name desc,id"));
    }

    @Test
    public void testInvalidColumn() {
        assertFalse(isValid(validator, "date, parent"));
    }

    @Test
    public void testInvalidOrder() {
        assertFalse(isValid(validator, "date asce"));
    }

    @Test
    public void testMultipleOrder() {
        assertFalse(isValid(validator, "date asc desc"));
    }
}
