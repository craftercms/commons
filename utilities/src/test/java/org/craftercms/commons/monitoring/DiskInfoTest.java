/*
 * Copyright (C) 2007-2025 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.monitoring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class DiskInfoTest {

	@Mock
	protected File theRootFile;

	@Test(expected = IllegalArgumentException.class)
	public void testPathDoesNotExist() {
		doReturn(false).when(theRootFile).exists();
		new DiskInfo(theRootFile);
	}

	@Test
	public void testLargeNumbers() {
		doReturn(true).when(theRootFile).exists();
		doReturn(234087234234234234L).when(theRootFile).getTotalSpace();
		doReturn(234087234234234234L / 100L).when(theRootFile).getFreeSpace();

		DiskInfo diskInfo = spy(new DiskInfo(theRootFile));

		assertEquals("Calculated disk usage percentage is not correct", 99, diskInfo.getDiskUsage());
	}

	@Test
	public void testLargeNumbers2() {
		doReturn(true).when(theRootFile).exists();
		long total = 5664887988745469946L;
		doReturn(total).when(theRootFile).getTotalSpace();
		doReturn(total / 100L * 2).when(theRootFile).getFreeSpace();

		DiskInfo diskInfo = spy(new DiskInfo(theRootFile));

		assertEquals("Calculated disk usage percentage is not correct", 98, diskInfo.getDiskUsage());
	}
}
