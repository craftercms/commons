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

/**
 * Holds the data about a version
 *
 * @author joseross
 * @since 3.1.1
 */
public class Version {

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