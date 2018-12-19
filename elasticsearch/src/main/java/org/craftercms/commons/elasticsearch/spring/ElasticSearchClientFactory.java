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

package org.craftercms.commons.elasticsearch.spring;

import java.util.stream.Stream;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Factory class for the ElasticSearch rest client
 * @author joseross
 */
public class ElasticSearchClientFactory implements FactoryBean<RestHighLevelClient> {

    /**
     * List of ElasticSearch urls
     */
    protected String[] serverUrls;

    @Required
    public void setServerUrls(final String[] serverUrls) {
        this.serverUrls = serverUrls;
    }

    @Override
    public RestHighLevelClient getObject() {
        return new RestHighLevelClient(RestClient.builder(
            Stream.of(serverUrls).map(HttpHost::create).toArray(HttpHost[]::new)));
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}