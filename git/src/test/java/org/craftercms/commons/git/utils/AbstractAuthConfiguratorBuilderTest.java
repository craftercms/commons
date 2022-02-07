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
package org.craftercms.commons.git.utils;

import org.craftercms.commons.git.auth.GitAuthenticationConfigurator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author joseross
 */
public class AbstractAuthConfiguratorBuilderTest {

    protected void verify(AuthConfiguratorBuilder builder,
                          Class<? extends GitAuthenticationConfigurator> expectedType) {
        assertNotNull("the builder should not be null", builder);
        assertNotNull("the expected type should not be null", expectedType);

        GitAuthenticationConfigurator configurator = builder.build();
        assertNotNull("the configurator should not be null", configurator);
        assertTrue("the configurator should be an instance of the expected type",
                expectedType.isInstance(configurator));
    }

}
