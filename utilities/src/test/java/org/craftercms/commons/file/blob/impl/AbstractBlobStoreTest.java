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
package org.craftercms.commons.file.blob.impl;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.ConfigUtils;
import org.craftercms.commons.config.profiles.aws.S3ProfileMapper;
import org.craftercms.commons.file.blob.impl.s3.AwsS3BlobStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.MapUtils.isEmpty;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author joseross
 */
public class AbstractBlobStoreTest {

    public static final Resource CONFIG_FILE = new ClassPathResource("config/stores.xml");

    @InjectMocks
    private AwsS3BlobStore store; // can't test abstract class so use the impl

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private S3ProfileMapper profileMapper;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void initTest() throws IOException, org.craftercms.commons.config.ConfigurationException {
        try (InputStream is = CONFIG_FILE.getInputStream()) {
            HierarchicalConfiguration<ImmutableNode> config = ConfigUtils.readXmlConfiguration(is, ',' ,emptyMap());

            store.init(config.configurationsAt("blobStore").get(0));

            assertNotNull("profile should have an id", store.profile.getProfileId());
            assertNull("profile should not have a bucket name", store.profile.getBucketName());
            assertNotNull("profile should have a region", store.profile.getRegion());

            assertFalse("mappings should not be empty", isEmpty(store.mappings));
            assertEquals("there should be 3 mappings", 3, store.mappings.size());
        }
    }

}
