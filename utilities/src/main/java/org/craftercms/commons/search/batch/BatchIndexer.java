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
package org.craftercms.commons.search.batch;

import org.craftercms.commons.search.batch.exception.BatchIndexingException;
import org.craftercms.core.service.ContentStoreService;
import org.craftercms.core.service.Context;

/**
 * Classes that implement this interface update or delete batches of files from a specified search index.
 *
 * @author avasquez
 */
public interface BatchIndexer {

    /**
     * Updates the specified search index with the given batch of files.
     *
     * @param indexId               the ID of the index, or null to use a default index
     * @param siteName              the name of the site the files belong to
     * @param contentStoreService   the content store service used to retrieve the files and content to index
     * @param context               the context of the file store being used
     * @param updateSet             the set of files to update/delete
     * @param updateStatus          status object used to track index updates and deletes
     */
    void updateIndex(String indexId, String siteName, ContentStoreService contentStoreService,
                     Context context, UpdateSet updateSet, UpdateStatus updateStatus) throws BatchIndexingException;

}
