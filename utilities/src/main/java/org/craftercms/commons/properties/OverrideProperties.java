/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 *
 */
public class OverrideProperties {
    /**
     * List of all Query files to be load.
     */
    protected List<Resource> resources;
    /**
     * Backend properties file.
     */
    protected Properties properties;
    /**
     * Logger.
     */
    private Logger log = LoggerFactory.getLogger(OverrideProperties.class);

    /**
     * Create a JongoQueries instance.
     */
    public OverrideProperties() {
        properties = new Properties();
    }

    /**
     * Checks and starts the reading of the given Resources.
     */
    public void init() {
        for (Resource resource : resources) {
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    readPropertyFile(in);
                } catch (IOException ex) {
                    log.debug("Unable to load queries from " + resource.getDescription(), ex);
                }
            } else {
                log.info("Query file at {} not found. Ignoring it...", resource.getDescription());
            }
        }
    }

    /**
     * Does the actual Reading of the properties file (of the given inputstream).
     *
     * @param input Input to be read.
     * @throws IOException If unable to read the given inputstream
     */
    protected void readPropertyFile(final InputStream input) throws IOException {
        properties.load(input);
    }

    /**
     * Gets the Query with the given name. Null if query is not found.
     *
     * @param name Name of the query.
     * @return Query with the given name. Null if nothing with that name if found.
     */
    public String get(String name) {
        return properties.getProperty(name);
    }

    /**
     * Reload the Query map.
     */
    public void reload() {
        destroy();
        init();
    }

    /**
     * Delete all the queries from the map.
     */
    private void destroy() {
        properties.clear();
    }

    public void setResources(final List<Resource> resources) {
        this.resources = resources;
    }
}
