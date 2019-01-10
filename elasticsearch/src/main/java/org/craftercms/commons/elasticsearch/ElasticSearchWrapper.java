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

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;

/**
 * Exposes the search related operations from the ElasticSearch client
 * @author joseross
 */
public interface ElasticSearchWrapper {

    /**
     * Performs a search operation
     * @param request the search request
     * @return the search response
     * @throws IOException if there is a connection issue
     */
    default SearchResponse search(SearchRequest request) throws IOException {
        return search(request, RequestOptions.DEFAULT);
    }

    /**
     * Performs a search operation
     * @param request the search request
     * @param options the request options
     * @return the search response
     * @throws IOException if there is a connection issue
     */
    SearchResponse search(SearchRequest request, RequestOptions options) throws IOException;

}
