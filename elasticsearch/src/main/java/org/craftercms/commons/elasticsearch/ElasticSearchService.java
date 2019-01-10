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

import java.io.InputStream;
import java.util.List;

import org.craftercms.commons.elasticsearch.exception.ElasticSearchException;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.util.MultiValueMap;

/**
 * Provides access to indexing operations in ElasticSearch
 * @author joseross
 */
public interface ElasticSearchService {

    /**
     * Performs a search for a specific field
     * @param indexName the name of the index
     * @param field the name of the field
     * @param queryBuilder the filters to apply
     * @return the list of values that match the search
     * @throws ElasticSearchException if there is any error during the operation
     */
    List<String> searchField(String indexName, String field, QueryBuilder queryBuilder) throws ElasticSearchException;

    /**
     * Performs an index for the given xml file
     * @param indexName the name of the index
     * @param siteId the name of the site
     * @param docId the id of the document
     * @param xml the content of the document
     * @throws ElasticSearchException if there is any error during the operation
     */
    void index(String indexName, String siteId, String docId, String xml) throws ElasticSearchException;

    /**
     * Performs an index for the given binary file
     * @param indexName the name of the index
     * @param siteName the name of the site
     * @param path the path of the document
     * @param additionalFields the additional fields to index
     * @param content the content of the document
     * @throws ElasticSearchException if there is any error during the operation
     */
    void indexBinary(String indexName, String siteName, String path, MultiValueMap<String, String> additionalFields,
                     InputStream content) throws ElasticSearchException;

    /**
     * Performs a delete for the given document
     * @param indexName the name of the index
     * @param siteId the id of the site
     * @param docId the id of the document
     * @throws ElasticSearchException if there is any error during the operation
     */
    void delete(String indexName, String siteId, String docId) throws ElasticSearchException;

    /**
     * Performs a flush for a given index
     * @param indexName the name of the index
     * @throws ElasticSearchException if there is any error during the operation
     */
    void flush(String indexName) throws ElasticSearchException;

}
