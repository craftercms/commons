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

package org.craftercms.commons.elasticsearch.impl.tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.craftercms.commons.elasticsearch.MetadataExtractor;
import org.craftercms.commons.elasticsearch.impl.AbstractDocumentParser;
import org.craftercms.search.exception.SearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link org.craftercms.commons.elasticsearch.DocumentParser} that uses Apache Tika
 * @author joseross
 */
public class TikaDocumentParser extends AbstractDocumentParser {

    private static final Logger logger = LoggerFactory.getLogger(TikaDocumentParser.class);

    /**
     * The maximum number of characters to parse from the document.
     * Defaults to 0 to parse only metadata.
     */
    protected int charLimit = 0;

    /**
     * Jackson {@link ObjectMapper} instance
     */
    protected ObjectMapper objectMapper;

    /**
     * List of metadata extractors to apply after parsing documents
     */
    protected List<MetadataExtractor<Metadata>> metadataExtractors;

    /**
     * Apache {@link Tika} instance
     */
    protected Tika tika = new Tika();

    protected FileTypeMap fileTypeMap = new MimetypesFileTypeMap();

    public void setCharLimit(final int charLimit) {
        this.charLimit = charLimit;
    }

    @Required
    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Required
    public void setMetadataExtractors(final List<MetadataExtractor<Metadata>> metadataExtractors) {
        this.metadataExtractors = metadataExtractors;
    }

    public void setTika(final Tika tika) {
        this.tika = tika;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String parseToXml(final String filename, final Resource resource,
                             final MultiValueMap<String, String> additionalFields) {
        Metadata metadata = new Metadata();
        try (InputStream in = resource.getInputStream()) {
            String parsedContent = tika.parseToString(in, metadata, charLimit);
            return extractMetadata(filename, resource, parsedContent, metadata, additionalFields);
        } catch (IOException | TikaException e) {
            logger.error("Error parsing file", e);
            throw new SearchException("Error parsing file", e);
        }
    }

    /**
     * Prepares the document to be indexed
     * @param resource the content of the parsed file
     * @param metadata the metadata of the parsed file
     * @param additionalFields additional fields to be added
     * @return the XML ready to be indexed
     */
    protected String extractMetadata(String filename, Resource resource, String parsedContent, Metadata metadata,
                                     MultiValueMap<String, String> additionalFields) {
        Map<String, Object> map = new HashMap<>();

        map.put(fieldNameContent, parsedContent);

        String type = fileTypeMap.getContentType(filename);
        if(!"pplication/octet-stream".equals(type)) {
            map.put("contentType", type);
        }

        try {
            map.put("contentLength", resource.contentLength());
        } catch (IOException e) {
            logger.warn("Could not find file size for {}", resource);
        }
        metadataExtractors.forEach(extractor -> extractor.extract(resource, metadata, map));

        if(MapUtils.isNotEmpty(additionalFields)) {
            map.putAll(additionalFields);
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error("Error writing parsed document as XML");
            throw new SearchException("Error writing parsed document as XML", e);
        }
    }

}