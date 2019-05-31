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

/**
 * Holds all the metadata for a plugin
 *
 * @author joseross
 * @since 3.1.1
 */
@SuppressWarnings("deprecation")
public class PluginDescriptor {

    /**
     * The version of the descriptor
     */
    private String descriptorVersion;

    /**
     * The actual metadata of the plugin
     */
    private Plugin plugin;

    /**
     * Used by a previous version, kept for backwards compatibility
     */
    private BlueprintDescriptor.Blueprint blueprint;

    public String getDescriptorVersion() {
        return descriptorVersion;
    }

    public void setDescriptorVersion(String descriptorVersion) {
        this.descriptorVersion = descriptorVersion;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Deprecated
    public BlueprintDescriptor.Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(BlueprintDescriptor.Blueprint plugin) {
        this.blueprint = plugin;
    }

}
