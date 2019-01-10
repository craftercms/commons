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

package org.craftercms.commons.elasticsearch;

import org.craftercms.commons.elasticsearch.exception.ElasticSearchException;

/**
 * Provides operations to manage indices in ElasticSearch
 * @author joseross
 */
public interface ElasticSearchAdminService {

    /**
     * Check if an index already exists
     * @param indexName the name of the index
     * @return true if the index already exists
     * @throws ElasticSearchException if there is any error during the operation
     */
    boolean exists(String indexName) throws ElasticSearchException;

    /**
     * Creates an index
     * @param indexName the name of the index
     * @param isAuthoring indicates if the index if for authoring
     * @throws ElasticSearchException if there is any error during the operation
     */
    void createIndex(String indexName, boolean isAuthoring) throws ElasticSearchException;

    /**
     * Deletes an index
     * @param indexName the name of the index
     * @param isAuthoring indicates if the index if for authoring
     * @throws ElasticSearchException if there is any error during the operation
     */
    void deleteIndex(String indexName, boolean isAuthoring) throws ElasticSearchException;

}
