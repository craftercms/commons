/*
 * Copyright (C) 2007-${year} Crafter Software Corporation.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Holds a Map of all the register MongoQueries.
 */
public class JongoQueries {

    /**
     * Logger.
     */
    private Logger log = LoggerFactory.getLogger(JongoQueries.class);

    /**
     * List of all Query files to be load.
     */
    private List<Resource> queryFiles;

    /**
     * Backend properties file.
     */
    protected Properties properties;

    /**
     * Create a JongoQueries instance.
     */
    public JongoQueries() {
        properties = new Properties();
    }

    public void init() {
        for (Resource queryFile : queryFiles) {
            if (queryFile.exists()) {
                try (InputStream in = queryFile.getInputStream()) {
                    properties.loadFromXML(in);
                } catch (IOException ex) {
                    log.debug("Unable to load queries from " + queryFile.getDescription(), ex);
                }
            } else {
                log.info("Query file at {} not found. Ignoring it...", queryFile.getDescription());
            }
        }
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

    public void setQueryFiles(final List<Resource> queryFiles) {
        this.queryFiles = queryFiles;
    }
}