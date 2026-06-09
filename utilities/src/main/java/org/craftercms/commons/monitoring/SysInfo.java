/*
 * Copyright (C) 2007-2026 Crafter Software Corporation. All Rights Reserved.
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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.jar.Manifest;

/**
 * Get current version information about:
 * 1. Current VM version runs current code.
 * 2. Build Information from a given manifest file.
 * 3. Name and version from a given manifest file.
 */
public final class SysInfo extends VersionInfo {

	private String osName;
	private String osVersion;
	private String osArch;

	private String javaVersion;
	private String javaVendor;
	private String javaVm;

	/**
	 * Create a {@link SysInfo} pojo instance based on a given Manifest File.
	 * Empty values are assign if the keys in the manifest are missing.
	 *
	 * @param manifest Manifest File that contains the VersionInfo Information.
	 */
	private SysInfo(Manifest manifest) {
		super(manifest);
		initRuntime();
		initOS();
	}

	private void initOS() {
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		osName = os.getName();
		osVersion = os.getVersion();
		osArch = os.getArch();
	}

	private void initRuntime() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		javaVersion = runtime.getSpecVersion();
		javaVendor = runtime.getSpecVendor();
		javaVm = runtime.getVmName();
	}

	/**
	 * Gets the current SysInfo based on Manifest and current JVM information.
	 *
	 * @param manifest Manifest were to get the information.
	 * @return A {@link SysInfo} pojo with information.
	 */
	public static SysInfo getInfo(Manifest manifest) {
		return new SysInfo(manifest);
	}

	/**
	 * Gets the current {@link SysInfo} base on a Class that will load it's manifest file.
	 *
	 * @param clazz Class that will load the manifest MF file
	 * @return A {@link SysInfo} pojo with the information.
	 * @throws IOException If Unable to read the Manifest file.
	 */
	public static SysInfo getInfo(Class<?> clazz) throws IOException {
		Manifest manifest = findManifest(clazz);
		if (manifest == null) {
			return null;
		}
		return new SysInfo(manifest);
	}

	@Override
	public String toString() {
		return "SysInfo{" +
				"packageName='" + getPackageName() + '\'' +
				", packageVersion='" + getPackageVersion() + '\'' +
				", packageBuild='" + getPackageBuild() + '\'' +
				", packageBuildDate='" + getPackageBuildDate() + '\'' +
				", javaVersion='" + javaVersion + '\'' +
				", javaVendor='" + javaVendor + '\'' +
				", javaVm='" + javaVm + '\'' +
				", osName='" + osName + '\'' +
				", osVersion='" + osVersion + '\'' +
				", osArch='" + osArch + '\'' +
				'}';
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public String getJavaVendor() {
		return javaVendor;
	}

	public String getJavaVm() {
		return javaVm;
	}

	public String getOsName() {
		return osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

}

