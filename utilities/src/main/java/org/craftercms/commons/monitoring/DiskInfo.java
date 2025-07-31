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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

/**
 * Holds basic disk information for a given path.
 */
@SuppressWarnings("unused")
public class DiskInfo {

	private final int diskUsage;
	private final long totalSpace;
	private final long freeSpace;
	private final long usedSpace;

	private final String rootPath;

	/**
	 * Creates a DiskInfo object for the specified root path.
	 *
	 * @param rootFile the root to check disk information for
	 */
	public DiskInfo(File rootFile) {
		if (rootFile == null || !rootFile.exists()) {
			throw new IllegalArgumentException("The provided path does not exist: " + rootFile);
		}
		this.rootPath = rootFile.getPath();
		totalSpace = rootFile.getTotalSpace();
		freeSpace = rootFile.getFreeSpace();
		usedSpace = totalSpace - freeSpace;
		if (totalSpace == 0) {
			throw new IllegalArgumentException("The provided path does not have a valid total space: " + rootPath);
		}
		diskUsage = (int) ((double) usedSpace / totalSpace * 100);
	}

	public int getDiskUsage() {
		return diskUsage;
	}

	public long getFreeSpace() {
		return freeSpace;
	}

	@JsonProperty("freeSpaceDisplaySize")
	public String getFreeSpaceDisplaySize() {
		return byteCountToDisplaySize(freeSpace);
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	@JsonProperty("totalSpaceDisplaySize")
	public String getTotalSpaceDisplaySize() {
		return byteCountToDisplaySize(totalSpace);
	}

	public long getUsedSpace() {
		return usedSpace;
	}

	@JsonProperty("usedSpaceDisplaySize")
	public String getUsedSpaceDisplaySize() {
		return byteCountToDisplaySize(usedSpace);
	}

	@JsonIgnore
	public String getRootPath() {
		return rootPath;
	}
}
