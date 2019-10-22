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

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;

/**
 * Holds all data about assets for a plugin
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media {

    protected List<Asset> screenshots;
    protected List<Asset> videos;

    public List<Asset> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<Asset> screenshots) {
        this.screenshots = screenshots;
    }

    public List<Asset> getVideos() {
        return videos;
    }

    public void setVideos(List<Asset> videos) {
        this.videos = videos;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        final Media media = (Media)o;
        return isEqualCollection(emptyIfNull(screenshots), emptyIfNull(media.screenshots)) &&
            isEqualCollection(emptyIfNull(videos), emptyIfNull(media.videos));
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenshots, videos);
    }

    @Override
    public String toString() {
        return "Media{" + "screenshots=" + screenshots + ", videos=" + videos + '}';
    }

}