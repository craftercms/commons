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
package org.craftercms.commons.file.blob.impl.s3;

import org.craftercms.commons.file.blob.impl.AbstractBlobStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author joseross
 */
public class AwsS3BlobStoreTest {

    private static final String MAPPING_TARGET = "my-target";

    private static final String MAPPING_PREFIX = "sandbox";

    private static final String URL = "/static-assets/s3/test.png";

    @InjectMocks
    private AwsS3BlobStore store;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getKeyTest() {
        AbstractBlobStore.Mapping mapping = new AbstractBlobStore.Mapping();
        mapping.target = MAPPING_TARGET;

        // test with null prefix
        String key = store.getKey(mapping, URL);
        checkS3Key(key);
        assertEquals("key should be equal to the url", removeStart(URL, "/"), key);

        // test with empty prefix
        mapping.prefix = "";
        key = store.getKey(mapping, URL);
        checkS3Key(key);
        assertEquals("key should be equal to the url", removeStart(URL, "/"), key);

        // test with prefix
        mapping.prefix = MAPPING_PREFIX;
        key = store.getKey(mapping, URL);
        checkS3Key(key);
        assertEquals("key should use the prefix", MAPPING_PREFIX + URL, key);

        mapping.prefix = "/sandbox";
        key = store.getKey(mapping, URL);
        checkS3Key(key);
        assertEquals("key should use the prefix", MAPPING_PREFIX + URL, key);

        mapping.prefix = "sandbox/";
        key = store.getKey(mapping, URL);
        checkS3Key(key);
        assertEquals("key should use the prefix", MAPPING_PREFIX + URL, key);

        key = store.getKey(mapping, removeStart(URL, "/"));
        checkS3Key(key);
        assertEquals("key should use the prefix", MAPPING_PREFIX + URL, key);

    }

    private void checkS3Key(String key) {
        assertFalse("key should not be empty", isEmpty(key));
        assertFalse("key should not start with /", startsWith(key, "/"));
        assertFalse("key should be a normalized path", contains(key, "//"));
    }

}
