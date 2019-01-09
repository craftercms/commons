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

package org.craftercms.commons.mongo;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cortiz on 1/10/14.
 */
public class JongoRepositoryTest {

    private static final String COLLECTION_NAME = "testPojo";
    public static final String TESTPOJO = "testpojo";

    @Test
    public void testAnnotatedPojoRepository() throws Exception {
        TestAnnotated repository = new TestAnnotated();
        repository.init();
        Assert.assertEquals(COLLECTION_NAME, repository.collectionName);
    }

    @Test
    public void testNonAnnotatedPojoRepository() throws Exception {
        TestSimple repository = new TestSimple();
        repository.init();
        Assert.assertEquals(TESTPOJO, repository.collectionName);

    }

    private class TestAnnotated extends AbstractJongoRepository<TestAnnotatedPojo>{

        /**
         * Creates A instance of a Jongo Repository.
         */
        public TestAnnotated() throws Exception {
        }
    }

    private class TestSimple extends AbstractJongoRepository<TestPojo>{

        /**
         * Creates A instance of a Jongo Repository.
         */
        public TestSimple() throws Exception {
        }
    }

}
