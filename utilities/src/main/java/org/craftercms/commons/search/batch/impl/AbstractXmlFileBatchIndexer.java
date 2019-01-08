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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.craftercms.commons.search.batch.UpdateStatus;
import org.craftercms.core.exception.CrafterException;
import org.craftercms.core.exception.XmlException;
import org.craftercms.core.processors.ItemProcessor;
import org.craftercms.core.processors.impl.ItemProcessorPipeline;
import org.craftercms.core.service.Content;
import org.craftercms.core.service.ContentStoreService;
import org.craftercms.core.service.Context;
import org.craftercms.core.service.Item;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import static org.craftercms.commons.search.batch.utils.IndexingUtils.getSiteBasedPath;


/**
 * {@link org.craftercms.commons.search.batch.BatchIndexer} that updates/deletes XML files from a search index.
 *
 * @author avasquez
 */
public abstract class AbstractXmlFileBatchIndexer extends AbstractBatchIndexer {

    private static final Log logger = LogFactory.getLog(AbstractXmlFileBatchIndexer.class);

    public static final List<String> DEFAULT_INCLUDE_FILENAME_PATTERNS = Collections.singletonList("^.*\\.xml$");

    protected ItemProcessor itemProcessor;

    public AbstractXmlFileBatchIndexer() {
        includePathPatterns = DEFAULT_INCLUDE_FILENAME_PATTERNS;
    }

    public void setItemProcessor(ItemProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
    }

    public void setItemProcessors(List<ItemProcessor> itemProcessors) {
        this.itemProcessor = new ItemProcessorPipeline(itemProcessors);
    }

    @Override
    protected void doSingleFileUpdate(String indexId, String siteName, ContentStoreService contentStoreService,
                                      Context context, String path, boolean delete, UpdateStatus updateStatus) {
        if (delete) {
            doDelete(indexId, siteName, path, updateStatus);
        } else {
            String xml = processXml(siteName, contentStoreService, context, path);

            doUpdate(indexId, siteName, path, xml, updateStatus);
        }
    }

    protected abstract void doDelete(String indexId, String siteName, String path, UpdateStatus updateStatus);

    protected abstract void doUpdate(String indexId, String siteName, String path, String xml,
                                     UpdateStatus updateStatus);

    protected String processXml(String siteName, ContentStoreService contentStoreService, Context context,
                                String path) throws CrafterException {
        if (logger.isDebugEnabled()) {
            logger.debug("Processing XML @ " + getSiteBasedPath(siteName, path) + " before indexing");
        }

        Item item = contentStoreService.getItem(context, null, path, itemProcessor);
        Document doc = item.getDescriptorDom();

        if (doc != null) {
            String xml = documentToString(item.getDescriptorDom());

            if (logger.isDebugEnabled()) {
                logger.debug("XML @ " + getSiteBasedPath(siteName, path) + " processed successfully:\n" + xml);
            }

            return xml;
        } else {
            throw new XmlException("Item @ " + getSiteBasedPath(siteName, path) + " doesn't seem to be an XML file");
        }
    }

    protected String documentToString(Document document) {
        StringWriter stringWriter = new StringWriter();
        OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter xmlWriter = new XMLWriter(stringWriter, format);

        try {
            xmlWriter.write(document);
        } catch (IOException e) {
            // Ignore, shouldn't happen.
        }

        return stringWriter.toString();
    }

    public static class EmptyContent implements Content {

        @Override
        public long getLastModified() {
            return System.currentTimeMillis();
        }

        @Override
        public long getLength() {
            return 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(new byte[0]);
        }

    }

}
