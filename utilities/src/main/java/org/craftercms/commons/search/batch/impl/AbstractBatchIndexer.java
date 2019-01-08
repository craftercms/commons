/*
 * Copyright (C) 2007-2017 Crafter Software Corporation.
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
package org.craftercms.commons.search.batch.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.craftercms.commons.lang.RegexUtils;
import org.craftercms.core.service.ContentStoreService;
import org.craftercms.core.service.Context;
import org.craftercms.commons.search.batch.BatchIndexer;
import org.craftercms.commons.search.batch.UpdateSet;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.commons.search.batch.exception.BatchIndexingException;

import static org.craftercms.commons.search.batch.utils.IndexingUtils.getSiteBasedPath;

/**
 * Base class for {@link BatchIndexer}s. Basically sub-classes only need to provide the processing of each of the files to be indexed.
 *
 * @author avasquez
 */
public abstract class AbstractBatchIndexer implements BatchIndexer {

    private static final Log logger = LogFactory.getLog(AbstractBatchIndexer.class);

    protected List<String> includePathPatterns;
    protected List<String> excludePathPatterns;

    public void setIncludePathPatterns(List<String> includePathPatterns) {
        this.includePathPatterns = includePathPatterns;
    }

    public void setExcludePathPatterns(List<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    @Override
    public void updateIndex(String indexId, String siteName, ContentStoreService contentStoreService,
                            Context context, UpdateSet updateSet, UpdateStatus updateStatus) throws BatchIndexingException {
        for (String path : updateSet.getUpdatePaths()) {
            if (include(path)) {
                try {
                    doSingleFileUpdate(indexId, siteName, contentStoreService, context, path, false, updateStatus);
                } catch (Exception e) {
                    logger.error("Error while trying to perform update of file " + getSiteBasedPath(siteName, path), e);

                    updateStatus.addFailedUpdate(path);
                }
            }
        }

        for (String path : updateSet.getDeletePaths()) {
            if (include(path)) {
                try {
                    doSingleFileUpdate(indexId, siteName, contentStoreService, context, path, true, updateStatus);
                } catch (Exception e) {
                    logger.error("Error while trying to perform delete of file " + getSiteBasedPath(siteName, path), e);

                    updateStatus.addFailedDelete(path);
                }
            }
        }
    }

    protected boolean include(String path) {
        return (CollectionUtils.isEmpty(includePathPatterns) || RegexUtils.matchesAny(path, includePathPatterns)) &&
               (CollectionUtils.isEmpty(excludePathPatterns) || !RegexUtils.matchesAny(path, excludePathPatterns));
    }

    protected abstract void doSingleFileUpdate(String indexId, String siteName,
                                               ContentStoreService contentStoreService, Context context,
                                               String path, boolean delete, UpdateStatus updateStatus) throws Exception;

}
