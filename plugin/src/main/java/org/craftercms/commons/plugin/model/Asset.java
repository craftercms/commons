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
 * Holds the metadata of a single asset
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {

    /**
     * The title of the asset
     */
    protected String title;

    /**
     * The description of the asset
     */
    protected String description;

    /**
     * The URL of the asset
     */
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        final Asset asset = (Asset)o;
        return Objects.equals(title, asset.title) && Objects.equals(description, asset.description) &&
            Objects.equals(url, asset.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, url);
    }

    @Override
    public String toString() {
        return "Asset{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", url='" + url + '\'' +
            '}';
    }

}
