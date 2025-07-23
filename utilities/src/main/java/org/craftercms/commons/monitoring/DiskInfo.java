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

import java.io.File;

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
	 * @param rootPath the root path to check disk information for
	 */
	public DiskInfo(String rootPath) {
		File f = new File(rootPath);
		if (!f.exists()) {
			throw new IllegalArgumentException("The provided path does not exist: " + rootPath);
		}
		this.rootPath = rootPath;
		totalSpace = f.getTotalSpace();
		freeSpace = f.getFreeSpace();
		usedSpace = totalSpace - freeSpace;
		if (totalSpace == 0) {
			throw new IllegalArgumentException("The provided path does not have a valid total space: " + rootPath);
		}
		diskUsage = (int) ((usedSpace * 100) / totalSpace);
	}

	public int getDiskUsage() {
		return diskUsage;
	}

	public long getFreeSpace() {
		return freeSpace;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public long getUsedSpace() {
		return usedSpace;
	}

	@JsonIgnore
	public String getRootPath() {
		return rootPath;
	}
}
