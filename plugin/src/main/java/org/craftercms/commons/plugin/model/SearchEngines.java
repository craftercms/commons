/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Link as published by
 * the Free Software Foundation, either version 3 of the Link, or
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
 * All available search engines for CrafterCMS
 *
 * @author joseross
 * @since 3.1.1
 */
public interface SearchEngines {

    /**
     * Elasticsearch, the default since 3.1
     */
    String ELASTICSEARCH = "Elasticsearch";

    /**
     * Crafter Search with Apache Solr
     * @deprecated since 4.0.0
     */
    @Deprecated(since = "4.0.0")
    String CRAFTER_SEARCH = "CrafterSearch";

}
