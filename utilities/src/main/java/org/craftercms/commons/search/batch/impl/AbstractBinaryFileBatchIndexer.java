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
package org.craftercms.commons.search.batch.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.craftercms.commons.search.batch.UpdateDetail;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.core.service.Content;
import org.craftercms.core.service.ContentStoreService;
import org.craftercms.core.service.Context;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;

import javax.activation.FileTypeMap;
import java.util.List;

import static org.craftercms.commons.search.batch.utils.IndexingUtils.isMimeTypeSupported;


/**
 * {@link org.craftercms.commons.search.batch.BatchIndexer} that updates/deletes binary or structured document files
 * (PDF,
 * Word, etc.) from a search index, only if their mime types match the supported mime types or if the supported mime
 * types map is empty.
 *
 * @author avasquez
 */
public abstract class AbstractBinaryFileBatchIndexer extends AbstractBatchIndexer {

    private static final Log logger = LogFactory.getLog(AbstractBinaryFileBatchIndexer.class);

    protected List<String> supportedMimeTypes;
    protected FileTypeMap mimeTypesMap;
    protected long maxFileSize;

    public AbstractBinaryFileBatchIndexer() {
        mimeTypesMap = new ConfigurableMimeFileTypeMap();
    }

    public void setSupportedMimeTypes(List<String> supportedMimeTypes) {
        this.supportedMimeTypes = supportedMimeTypes;
    }

    @Required
    public void setMaxFileSize(final long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    @Override
    protected void doSingleFileUpdate(String indexId, String siteName, ContentStoreService contentStoreService,
                                      Context context, String path, boolean delete, UpdateDetail updateDetail,
                                      UpdateStatus updateStatus) {
        if (delete) {
            doDelete(indexId, siteName, path, updateStatus);
        } else {
            Content binaryContent = contentStoreService.getContent(context, path);
            if(binaryContent.getLength() > maxFileSize) {
                logger.info("Skipping large binary file @ " + path);
            } else {
                doUpdateContent(indexId, siteName, path, binaryContent, updateDetail, updateStatus);
            }
        }
    }

    protected abstract void doDelete(String indexId, String siteName, String path, UpdateStatus updateStatus);

    protected abstract void doUpdateContent(String indexId, String siteName, String path, Content binaryContent,
                                            UpdateDetail updateDetail, UpdateStatus updateStatus);

    @Override
    protected boolean include(String path) {
        if (super.include(path)) {
            return isMimeTypeSupported(mimeTypesMap, supportedMimeTypes, path);
        }

        return false;
    }

}
