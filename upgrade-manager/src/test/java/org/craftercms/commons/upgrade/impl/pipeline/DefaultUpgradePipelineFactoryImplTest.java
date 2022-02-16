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
package org.craftercms.commons.upgrade.impl.pipeline;

import org.craftercms.commons.config.ConfigurationException;
import org.craftercms.commons.upgrade.UpgradeOperation;
import org.craftercms.commons.upgrade.VersionProvider;
import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.exception.UpgradeNotSupportedException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author joseross
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultUpgradePipelineFactoryImplTest {

    private static final String CONFIG_PATH = "pipeline-factory-test.yaml";

    private static final String PIPELINE_NAME_VERSIONS = "test";

    private static final String OPERATION_TYPE = "testUpgrader";

    private static final String VERSION_1 = "1.0";

    private static final String VERSION_2 = "2.0";

    private static final String VERSION_3 = "3.0";

    private static final String UNSUPPORTED_VERSION = "0.5.1";

    private DefaultUpgradePipelineFactoryImpl<String> factory;

    private Resource config = new ClassPathResource(CONFIG_PATH);

    @Mock
    private VersionProvider<String> versionProvider;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private UpgradeContext<String> context;

    @Mock
    private UpgradeOperation<String> testOperation;

    @Before
    public void setUp() {
        factory = new DefaultUpgradePipelineFactoryImpl<>(PIPELINE_NAME_VERSIONS, config, versionProvider);
        factory.setApplicationContext(applicationContext);

        when(applicationContext.getBean(OPERATION_TYPE, UpgradeOperation.class)).thenReturn(testOperation);
    }

    @Test
    public void versionShouldNotBeUpdated() throws ConfigurationException, UpgradeException {
        when(versionProvider.getVersion(context)).thenReturn(VERSION_1);
        factory.setUpdateVersion(false);

        var pipeline = factory.getPipeline(context);
        assertFalse("pipeline should not be empty", pipeline.isEmpty());

        pipeline.execute(context);
        verify(versionProvider, never()).setVersion(any(), any());
    }

    @Test
    public void allVersionsShouldBeUpdated() throws UpgradeException, ConfigurationException {
        when(versionProvider.getVersion(context)).thenReturn(VERSION_1);

        var pipeline = factory.getPipeline(context);
        assertFalse("pipeline should not be empty", pipeline.isEmpty());

        pipeline.execute(context);
        verify(versionProvider).setVersion(context, VERSION_2);
        verify(versionProvider).setVersion(context, VERSION_3);
    }

    @Test(expected = UpgradeNotSupportedException.class)
    public void shouldRejectUnsupportedVersion() throws UpgradeException, ConfigurationException {
        when(versionProvider.getVersion(context)).thenReturn(UNSUPPORTED_VERSION);

        factory.getPipeline(context);
    }

}
