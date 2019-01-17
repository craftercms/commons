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

package org.craftercms.commons.elasticsearch.batch;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.craftercms.commons.elasticsearch.ElasticSearchService;
import org.craftercms.commons.elasticsearch.exception.ElasticSearchException;
import org.craftercms.commons.search.batch.UpdateDetail;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.commons.search.batch.impl.AbstractBinaryFileWithMetadataBatchIndexer;
import org.craftercms.core.service.Content;
import org.craftercms.search.exception.SearchException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.util.MultiValueMap;

/**
 * Implementation of {@link AbstractBinaryFileWithMetadataBatchIndexer} for ElasticSearch
 * @author joseross
 */
public class ElasticSearchBinaryFileWithMetadataBatchIndexer extends AbstractBinaryFileWithMetadataBatchIndexer {

    /**
     * ElasticSearch service
     */
    protected ElasticSearchService elasticSearchService;

    @Required
    public void setElasticSearchService(final ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    protected void doDelete(final String indexId, final String siteName, final String previousBinaryPath,
                            final UpdateStatus updateStatus) {
        ElasticSearchIndexingUtils.doDelete(elasticSearchService, indexId, siteName, previousBinaryPath, updateStatus);
    }

    @Override
    protected List<String> searchBinaryPathsFromMetadataPath(final String indexId, final String siteName,
                                                             final String metadataPath) {
        try {
            return elasticSearchService.searchField(indexId, "_id",
                new BoolQueryBuilder()
                    .filter(new TermQueryBuilder("crafterSite", siteName))
                    .filter(new TermQueryBuilder("metadataPath", metadataPath))
            );
        } catch (ElasticSearchException e) {
            throw new SearchException(indexId, "Error executing search for " + metadataPath, e);
        }
    }

    @Override
    protected String searchMetadataPathFromBinaryPath(final String indexId, final String siteName,
                                                      final String binaryPath) {
        try {
            List<String> paths = elasticSearchService.searchField(indexId, "metadataPath",
                new BoolQueryBuilder()
                    .filter(new TermQueryBuilder("crafterSite", siteName))
                    .filter(new TermQueryBuilder("_id", binaryPath))
            );
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.get(0);
            } else {
                return null;
            }
        } catch (ElasticSearchException e) {
           throw new SearchException(indexId, "Error executing search for " + binaryPath, e);
        }
    }

    @Override
    protected void doUpdateContent(final String indexId, final String siteName, final String binaryPath,
                                   final Resource resource, final MultiValueMap<String, String> metadata,
                                   final UpdateDetail updateDetail, final UpdateStatus updateStatus) {
        ElasticSearchIndexingUtils.doUpdateBinary(elasticSearchService, indexId, siteName, binaryPath, metadata,
                resource, updateDetail, updateStatus);
    }

    @Override
    protected void doUpdateContent(final String indexId, final String siteName, final String binaryPath,
                                   final Content content, final MultiValueMap<String, String> metadata,
                                   final UpdateDetail updateDetail, final UpdateStatus updateStatus) {
        ElasticSearchIndexingUtils.doUpdateBinary(elasticSearchService, indexId, siteName, binaryPath, metadata,
                content, updateDetail, updateStatus);
    }

    @Override
    protected void doUpdateContent(final String indexId, final String siteName, final String binaryPath,
                                   final Resource resource, final UpdateDetail updateDetail,
                                   final UpdateStatus updateStatus) {
        doUpdateContent(indexId, siteName, binaryPath, resource, updateDetail, updateStatus);
    }

    @Override
    protected void doUpdateContent(final String indexId, final String siteName, final String binaryPath,
                                   final Content content, final UpdateDetail updateDetail,
                                   final UpdateStatus updateStatus) {
        doUpdateContent(indexId, siteName, binaryPath, content, updateDetail, updateStatus);
    }

}
