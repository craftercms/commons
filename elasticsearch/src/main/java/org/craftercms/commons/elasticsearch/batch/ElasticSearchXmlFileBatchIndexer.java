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

import org.craftercms.commons.elasticsearch.ElasticSearchService;
import org.craftercms.commons.search.batch.UpdateDetail;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.commons.search.batch.impl.AbstractXmlFileBatchIndexer;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link AbstractXmlFileBatchIndexer} for ElasticSearch
 * @author joseross
 */
public class ElasticSearchXmlFileBatchIndexer extends AbstractXmlFileBatchIndexer {

    /**
     * ElasticSearch service
     */
    protected ElasticSearchService elasticSearchService;

    @Required
    public void setElasticSearchService(final ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    protected void doDelete(final String indexId, final String siteName, final String path, final UpdateStatus updateStatus) {
        ElasticSearchIndexingUtils.doDelete(elasticSearchService, indexId, siteName, path, updateStatus);
    }

    @Override
    protected void doUpdate(final String indexId, final String siteName, final String path, final String xml,
                            final UpdateDetail updateDetail, final UpdateStatus updateStatus) {
        ElasticSearchIndexingUtils.doUpdate(elasticSearchService, indexId, siteName, path, xml, updateDetail,
            updateStatus);
    }

}
