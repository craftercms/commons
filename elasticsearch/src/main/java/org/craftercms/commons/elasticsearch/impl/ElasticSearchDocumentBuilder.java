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

package org.craftercms.commons.elasticsearch.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.craftercms.commons.elasticsearch.jackson.MixedMultivaluedMap;
import org.craftercms.search.exception.SearchException;
import org.springframework.beans.factory.annotation.Required;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link org.craftercms.commons.elasticsearch.DocumentBuilder} for ElasticSearch
 * @author joseross
 */
public class ElasticSearchDocumentBuilder extends AbstractDocumentBuilder<Map<String, Object>> {

    protected ObjectMapper objectMapper;

    @Required
    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> build(final String site, final String id, final String xml) {
        try {
            Map<String, Object> map = objectMapper.readValue(xml, MixedMultivaluedMap.class);
            addFields(map, site, id, null);
            return map;
        } catch (Exception e) {
            throw new SearchException("Error building json for document " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFields(Map<String, Object> map, String site, String id, Map<String, List<String>> additionalFields) {
        String now = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now(ZoneId.of("UTC")));

        map.put(siteFieldName, site);
        map.put(localIdFieldName, id);
        map.put(publishingDateFieldName, now);
        map.put(publishingDateAltFieldName, now);

        if(MapUtils.isNotEmpty(additionalFields)) {
            additionalFields.forEach((key, values) -> map.put(key, values.size() == 1? values.get(0) : values));
        }
    }

}