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

import java.util.List;

public class SqlSortValidatorTest implements ValidatorTest {
    private Validator validator;

    @Before
    public void setUp() {
        validator = new SqlSortValidator(List.of("id", "DATE", "name"));
    }

    @Test
    public void testSingleColumn() {
        assertValid("date");
    }

    @Test
    public void testSingleColumnAndSort() {
        assertValid("date desc");
    }

    @Test
    public void testDifferentCase() {
        assertValid("DATE desc");
    }

    @Test
    public void testDifferentCase2() {
        assertValid("id ASc");
    }

    @Test
    public void testTwoColumns() {
        assertValid("date, name");
    }

    @Test
    public void testMultipleColumnAndSorts() {
        assertValid("date asc, name desc,id");
    }

    @Test
    public void testInvalidColumn() {
        assertRejected("date, parent");
    }

    @Test
    public void testInvalidOrder() {
        assertRejected("date asce");
    }

    @Test
    public void testMultipleOrder() {
        assertRejected("date asc desc");
    }

    @Override
    public Validator getValidator() {
        return validator;
    }
}
