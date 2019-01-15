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

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.craftercms.commons.elasticsearch.MetadataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

/**
 * Implementation of {@link MetadataExtractor} that uses Apache Tika to parse binary files.
 * @author joseross
 */
public class TikaMetadataExtractor implements MetadataExtractor<Metadata> {

    private static final Logger logger = LoggerFactory.getLogger(TikaMetadataExtractor.class);

    /**
     * The list of mime types that can be handled by this extractor
     */
    protected String[] supportedMimeTypes;

    /**
     * The mapping of Apache Tika properties to extract
     */
    protected Map<String, Object> mapping;

    public void setSupportedMimeTypes(final String[] supportedMimeTypes) {
        this.supportedMimeTypes = supportedMimeTypes;
    }

    @Required
    public void setMapping(final Map<String, Object> mapping) {
        this.mapping = mapping;
    }

    /**
     * Indicates if the given metadata can be handled by this extractor
     * @param metadata the metadata to check
     * @return true if the metadata is supported
     */
    protected boolean isSupported(final Metadata metadata) {
        String contentType = metadata.get(HttpHeaders.CONTENT_TYPE);
        if(StringUtils.isEmpty(contentType) || ArrayUtils.isEmpty(supportedMimeTypes)) {
            return true;
        }
        MimeType mimeType = MimeType.valueOf(contentType);
        for(String supportedMimeType : supportedMimeTypes) {
            if(mimeType.isCompatibleWith(MimeType.valueOf(supportedMimeType))) {
                return true;
            }
        }
        logger.info("Type {} is not compatible with any type of {}", contentType, supportedMimeTypes);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extract(final Resource resource, final Metadata metadata, final Map<String, Object> properties) {
        if(isSupported(metadata)) {
            logger.info("Extracting metadata");
            mapping.forEach((property, key) -> {
                if(key instanceof String) {
                    properties.put(property, metadata.get((String) key));
                } else if(key instanceof Property) {
                    properties.put(property, metadata.get((Property) key));
                } else {
                    throw new IllegalArgumentException("Invalid metadata key " + key);
                }
            });
        }
    }

}
