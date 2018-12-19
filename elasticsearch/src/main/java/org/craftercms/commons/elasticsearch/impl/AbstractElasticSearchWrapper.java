/*
 * Copyright (C) 2007-2018 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.elasticsearch.impl;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpHost;
import org.craftercms.commons.elasticsearch.ElasticSearchWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Base implementation of {@link ElasticSearchWrapper}
 * @author joseross
 */
public abstract class AbstractElasticSearchWrapper implements ElasticSearchWrapper, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractElasticSearchWrapper.class);

    /**
     * The ElasticSearch client
     */
    protected RestHighLevelClient client;

    /**
     * The server urls for ElasticSearch
     */
    protected String[] serverUrls;

    /**
     * The filter queries to apply to all searches
     */
    protected String[] filterQueries;

    @Required
    public void setServerUrls(final String[] serverUrls) {
        this.serverUrls = serverUrls;
    }

    public void setFilterQueries(final String[] filterQueries) {
        this.filterQueries = filterQueries;
    }

    @Override
    public void afterPropertiesSet() {
        client = new RestHighLevelClient(RestClient.builder(
            Stream.of(serverUrls).map(HttpHost::create).toArray(HttpHost[]::new)));
    }

    /**
     * Updates the value of the index for the given request
     * @param request the request to update
     */
    protected abstract void updateIndex(SearchRequest request);

    /**
     * Updates the filter quieries for the given request
     * @param request the request to update
     */
    protected void updateFilters(SearchRequest request) {
        if(ArrayUtils.isEmpty(filterQueries)) {
            logger.debug("No additional filter queries configured");
            return;
        }

        BoolQueryBuilder boolQueryBuilder;
        if(request.source().query() instanceof BoolQueryBuilder) {
            boolQueryBuilder = (BoolQueryBuilder) request.source().query();
        } else {
            boolQueryBuilder = new BoolQueryBuilder().must(request.source().query());
        }

        for(String filterQuery : filterQueries) {
            logger.debug("Adding filter query: {}", filterQuery);
            boolQueryBuilder.filter(new QueryStringQueryBuilder(filterQuery));
        }

        request.source().query(boolQueryBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResponse search(final SearchRequest request, final RequestOptions options) throws IOException {
        logger.debug("Performing search for request: {}", request);
        updateIndex(request);
        updateFilters(request);
        return client.search(request, options);
    }

}
