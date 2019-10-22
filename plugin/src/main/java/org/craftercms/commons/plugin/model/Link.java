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
 * Holds the data about a single link
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {

    /**
     * The URL of the link
     */
    protected String url;

    /**
     * The name of the link
     */
    protected String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Link)) {
            return false;
        }
        final Link link = (Link)o;
        return Objects.equals(url, link.url) && Objects.equals(name, link.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name);
    }

    @Override
    public String toString() {
        return "Link{" + "url='" + url + '\'' + ", name='" + name + '\'' + '}';
    }

}