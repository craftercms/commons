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

import java.io.InputStream;

import org.craftercms.commons.elasticsearch.ElasticSearchService;
import org.craftercms.commons.elasticsearch.exception.ElasticSearchException;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.commons.search.batch.utils.IndexingUtils;
import org.craftercms.search.exception.SearchException;
import org.springframework.util.MultiValueMap;

/**
 * Utility class to perform ElasticSearch operations
 * @author joseross
 */
public abstract class ElasticSearchIndexingUtils extends IndexingUtils {

    public static void doDelete(final ElasticSearchService elasticSearch, final String indexId,
                                final String siteName, final String path, final UpdateStatus updateStatus) {
        try {
            elasticSearch.delete(indexId, siteName, path);
            updateStatus.addSuccessfulDelete(path);
        } catch (ElasticSearchException e) {
            throw new SearchException(indexId, "Error deleting document " + path, e);
        }
    }

    public static void doUpdate(final ElasticSearchService elasticSearch, final String indexId,
                                final String siteName, final String path, final String xml,
                                final UpdateStatus updateStatus) {
        try {
            elasticSearch.index(indexId, siteName, path, xml);
            updateStatus.addSuccessfulUpdate(path);
        } catch (ElasticSearchException e) {
            throw new SearchException(indexId, "Error indexing document " + path, e);
        }
    }

    public static void doUpdateBinary(final ElasticSearchService elasticSearch, final String indexName,
                                      final String siteName, final String path,
                                      final MultiValueMap<String, String> additionalFields,
                                      final InputStream content, final UpdateStatus updateStatus) {
        try {
            elasticSearch.indexBinary(indexName, siteName, path, additionalFields, content);
            updateStatus.addSuccessfulUpdate(path);
        } catch (ElasticSearchException e) {
            throw new SearchException(indexName, "Error indexing binary document " + path, e);
        }

    }
}
