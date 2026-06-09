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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static org.apache.commons.lang3.Strings.CS;

/**
 * Get's current version information about:
 * 1. Build Information from a given manifest file.
 * 2. Name and version from a given manifest file.
 *
 * @author Carlos Ortiz
 * @since 3.0
 */
public class VersionInfo {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"));

	/**
	 * Manifest key for the date in ms when the package was build.
	 */
	public static final String KEY_BUILD_ON = "Build-On";

	/**
	 * Manifest key for name of the Lib/app in the jar (usually maven name tag value).
	 */
	public static final String KEY_IMPLEMENTATION_TITLE = "Implementation-Title";

	/**
	 * Manifest key for VersionInfo of the code (usually matches Mvn packageVersion)
	 */
	public static final String KEY_IMPLEMENTATION_VERSION = "Implementation-Version";

	/**
	 * Manifest key for codebase packageVersion (usually git hash or svn id).
	 */
	public static final String KEY_IMPLEMENTATION_BUILD = "Implementation-Build";

	/**
	 * Manifest Default Path
	 */
	public static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

	/**
	 * The path for classes loaded from a WAR file
	 */
	public static final String WAR_PATH = "/WEB-INF/classes/";

	/**
	 * The path for classes loaded from a Spring Boot JAR file
	 */
	public static final String SPRING_PATH = "!BOOT-INF/classes/!/";

	private String packageName;
	private String packageVersion;
	private String packageBuild;
	private String packageBuildDate;

	protected VersionInfo(Manifest manifest) {
		Attributes mainAttrs = manifest.getMainAttributes();
		initFromManifest(mainAttrs);
	}

	/**
	 * Gets the current VersionInfo based on Manifest &amp; current JVM information.
	 *
	 * @param manifest Manifest were to get the information.
	 * @return A VersionInfo pojo with information.
	 */
	public static VersionInfo getVersion(Manifest manifest) {
		return new VersionInfo(manifest);
	}

	/**
	 * Gets the current VersionInfo base on a Class that will load it's manifest file.
	 *
	 * @param clazz Class that will load the manifest MF file
	 * @return A {@link VersionInfo} pojo with the information.
	 * @throws IOException If Unable to read the Manifest file.
	 */
	public static VersionInfo getVersion(Class<?> clazz) throws IOException {
		Manifest manifest = findManifest(clazz);
		if (manifest == null) {
			return null;
		}
		return new VersionInfo(manifest);
	}

	protected static Manifest findManifest(Class<?> clazz) throws IOException {
		String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		if (CS.contains(path, WAR_PATH)) {
			path = CS.removeEnd(path, WAR_PATH);
			path = CS.appendIfMissing(path, MANIFEST_PATH);
			try (InputStream is = Files.newInputStream(Paths.get(path))) {
				return new Manifest(is);
			}
		}
		if (CS.contains(path, SPRING_PATH)) {
			path = CS.removeStart(path, "file:");        // filesystem case
			path = CS.removeStart(path, "nested:");    // Spring Boot nested jars case
			path = CS.removeEnd(path, SPRING_PATH);
			try (JarFile jarFile = new JarFile(path)) {
				return jarFile.getManifest();
			}
		}
		return null;
	}

	private void initFromManifest(Attributes mainAttrs) {

		if (mainAttrs.getValue(KEY_BUILD_ON) != null) {
			packageBuildDate = FORMATTER.format(
					Instant.ofEpochMilli(Long.parseLong(mainAttrs.getValue(KEY_BUILD_ON))));
		} else {
			packageBuildDate = StringUtils.EMPTY;
		}
		packageName = mainAttrs.getValue(KEY_IMPLEMENTATION_TITLE);
		packageVersion = mainAttrs.getValue(KEY_IMPLEMENTATION_VERSION);
		packageBuild = mainAttrs.getValue(KEY_IMPLEMENTATION_BUILD);
	}

	@Override
	public String toString() {
		return "VersionInfo{" +
				"packageName='" + packageName + '\'' +
				", packageVersion='" + packageVersion + '\'' +
				", packageBuild='" + packageBuild + '\'' +
				", packageBuildDate='" + packageBuildDate + '\'' +
				'}';
	}

	public String getPackageName() {
		return packageName;
	}

	public String getPackageVersion() {
		return packageVersion;
	}

	public String getPackageBuild() {
		return packageBuild;
	}

	public String getPackageBuildDate() {
		return packageBuildDate;
	}
}
