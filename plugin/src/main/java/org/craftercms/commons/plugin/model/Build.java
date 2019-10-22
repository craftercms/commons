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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Holds the build information
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {

    /**
     * The id of the build
     */
    protected String id;

    /**
     * The date of the build
     */
    protected String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Build)) {
            return false;
        }
        final Build build = (Build)o;
        return Objects.equals(id, build.id) && Objects.equals(date, build.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public String toString() {
        return "Build{" + "id='" + id + '\'' + ", date='" + date + '\'' + '}';
    }

}
