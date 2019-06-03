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
 * Holds the metadata for a plugin
 *
 * @author joseross
 * @since 3.1.1
 */
public class Plugin {

    /**
     * The type of plugin
     */
    private String type;

    /**
     * The unique id of the plugin
     */
    private String id;

    /**
     * The name of the plugin
     */
    private String name;

    /**
     * The tags of the plugin
     */
    private List<String> tags;

    /**
     * The version of the plugin
     */
    private Version version;

    /**
     * The description of the plugin
     */
    private String description;

    /**
     * The website information of the plugin
     */
    private Link website;

    /**
     * The media assets of the plugin
     */
    private Media media;

    /**
     * The developer information of the plugin
     */
    private Developer developer;

    /**
     * The build information of the plugin
     */
    private Build build;

    /**
     * The license of the plugin
     */
    private Link license;

    /**
     * The supported Crafter CMS editions of the plugin
     */
    private List<String> crafterCmsEditions;

    /**
     * The supported Crafter CMS versions of the plugin
     */
    private List<Version> crafterCmsVersions;

    /**
     * The search engine required by the plugin
     */
    private String searchEngine = SearchEngines.ELASTICSEARCH;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Link getWebsite() {
        return website;
    }

    public void setWebsite(Link website) {
        this.website = website;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(final Developer developer) {
        this.developer = developer;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public Link getLicense() {
        return license;
    }

    public void setLicense(Link link) {
        this.license = link;
    }

    public List<String> getCrafterCmsEditions() {
        return crafterCmsEditions;
    }

    public void setCrafterCmsEditions(final List<String> crafterCmsEditions) {
        this.crafterCmsEditions = crafterCmsEditions;
    }

    public List<Version> getCrafterCmsVersions() {
        return crafterCmsVersions;
    }

    public void setCrafterCmsVersions(final List<Version> crafterCmsVersions) {
        this.crafterCmsVersions = crafterCmsVersions;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(final String searchEngine) {
        this.searchEngine = searchEngine;
    }

}