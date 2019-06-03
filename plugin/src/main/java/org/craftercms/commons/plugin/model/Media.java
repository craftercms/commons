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

/**
 * Holds all data about assets for a plugin
 *
 * @author joseross
 * @since 3.1.1
 */
public class Media {

    private List<Asset> screenshots;
    private List<Asset> videos;

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

}