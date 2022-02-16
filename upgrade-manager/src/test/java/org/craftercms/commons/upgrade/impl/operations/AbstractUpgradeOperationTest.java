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

package org.craftercms.commons.upgrade.impl.operations;

import org.craftercms.commons.upgrade.exception.UpgradeException;
import org.craftercms.commons.upgrade.impl.UpgradeContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author joseross
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractUpgradeOperationTest {

    @Spy
    private DummyUpgradeOperation operation;

    @Test
    public void testEnabled() throws UpgradeException {
        operation.setEnabled(true);
        operation.execute(null);

        verify(operation).doExecute(null);
    }

    @Test
    public void testDisabled() throws UpgradeException {
        operation.setEnabled(false);
        operation.execute(null);

        verify(operation, never()).doExecute(null);
    }

    private static class DummyUpgradeOperation extends AbstractUpgradeOperation<Object> {

        @Override
        protected void doExecute(UpgradeContext<Object> target) {
            // do nothing
        }

    }

}
