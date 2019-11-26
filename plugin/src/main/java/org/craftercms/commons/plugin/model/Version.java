/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Link as published by
 * the Free Software Foundation, either version 3 of the Link, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public Link for more details.
 *
 * You should have received a copy of the GNU General Public Link
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.plugin.model;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static org.craftercms.commons.plugin.model.CrafterCmsEditions.COMMUNITY;
import static org.craftercms.commons.plugin.model.CrafterCmsEditions.ENTERPRISE;

/**
 * Holds the data about a version
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

    private static Pattern PATTERN = Pattern.compile("(\\d\\.\\d\\.\\d)(\\w)?.*");

    public static Version of(String v) {
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        String[] values = v.split("\\.");
        if (values.length != 3) {
            throw new IllegalArgumentException("String '" + v + "' does not contain a valid version");
        }
        return of(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
    }

    public static Version of(int major, int minor, int patch) {
        Version version = new Version();
        version.major = major;
        version.minor = minor;
        version.patch = patch;
        return version;
    }

    public static String getVersion(String v) {
        Matcher matcher = PATTERN.matcher(v);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("String '" + v + "' does not contain a valid version");
    }

    public static String getEdition(String v) {
        Matcher matcher = PATTERN.matcher(v);
        if (matcher.matches()) {
            String edition = matcher.group(2);
            if (StringUtils.isNotEmpty(edition) && StringUtils.equalsIgnoreCase(edition, "e")) {
                return ENTERPRISE;
            } else {
                return COMMUNITY;
            }
        }
        throw new IllegalArgumentException("String '" + v + "' does not contain a valid version");
    }

    /**
     * The major version
     */
    protected int major;

    /**
     * The minor version
     */
    protected int minor;

    /**
     * The patch version
     */
    protected int patch;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getPatch() {
        return patch;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Version)) {
            return false;
        }
        final Version version = (Version)o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public String toString() {
        return "Version{" + "major=" + major + ", minor=" + minor + ", patch=" + patch + '}';
    }

}