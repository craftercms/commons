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
 * Holds the metadata for a plugin
 *
 * @author joseross
 * @since 3.1.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plugin {

    /**
     * The type of plugin
     */
    protected String type;

    /**
     * The unique id of the plugin
     */
    protected String id;

    /**
     * The name of the plugin
     */
    protected String name;

    /**
     * The tags of the plugin
     */
    protected List<String> tags;

    /**
     * The version of the plugin
     */
    protected Version version;

    /**
     * The description of the plugin
     */
    protected String description;

    /**
     * The website information of the plugin
     */
    protected Link website;

    /**
     * The media assets of the plugin
     */
    protected Media media;

    /**
     * The developer information of the plugin
     */
    protected Developer developer;

    /**
     * The build information of the plugin
     */
    protected Build build;

    /**
     * The license of the plugin
     */
    protected Link license;

    /**
     * The supported Crafter CMS editions of the plugin
     */
    protected List<String> crafterCmsEditions;

    /**
     * The supported Crafter CMS versions of the plugin
     */
    protected List<Version> crafterCmsVersions;

    /**
     * The search engine required by the plugin
     */
    protected String searchEngine = SearchEngines.ELASTICSEARCH;

    /**
     * The parameters supported by the plugin
     */
    protected List<Parameter> parameters;

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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plugin)) {
            return false;
        }
        final Plugin plugin = (Plugin)o;
        return Objects.equals(type, plugin.type) && Objects.equals(id, plugin.id) && Objects.equals(name,
            plugin.name) && isEqualCollection(emptyIfNull(tags), emptyIfNull(plugin.tags)) &&
            Objects.equals(version, plugin.version) &&
            Objects.equals(description, plugin.description) && Objects.equals(website, plugin.website) &&
            Objects.equals(media, plugin.media) && Objects.equals(developer, plugin.developer) &&
            Objects.equals(build, plugin.build) && Objects.equals(license, plugin.license) &&
            isEqualCollection(emptyIfNull(crafterCmsEditions), emptyIfNull(plugin.crafterCmsEditions)) &&
            isEqualCollection(emptyIfNull(crafterCmsVersions), emptyIfNull(plugin.crafterCmsVersions)) &&
            Objects.equals(searchEngine, plugin.searchEngine) &&
            isEqualCollection(emptyIfNull(parameters), emptyIfNull(plugin.parameters));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, name, tags, version, description, website, media, developer, build, license,
            crafterCmsEditions, crafterCmsVersions, searchEngine, parameters);
    }

    @Override
    public String toString() {
        return "Plugin{" + "type='" + type + '\'' + ", id='" + id + '\'' + ", name='" + name + '\'' + ", tags=" +
            tags + ", version=" + version + ", description='" + description + '\'' + ", website=" + website +
            ", media=" + media + ", developer=" + developer + ", build=" + build + ", license=" + license +
            ", crafterCmsEditions=" + crafterCmsEditions + ", crafterCmsVersions=" + crafterCmsVersions +
            ", searchEngine='" + searchEngine + '\'' + ", parameters=" + parameters + '}';
    }

}