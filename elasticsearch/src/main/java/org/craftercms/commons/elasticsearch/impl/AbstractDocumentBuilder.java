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

import java.util.List;
import java.util.Map;

import org.craftercms.commons.elasticsearch.DocumentBuilder;

/**
 * Basic operations for all {@link DocumentBuilder} implementations
 * @param <T> type for the native documents
 * @author joseross
 */
public abstract class AbstractDocumentBuilder<T> implements DocumentBuilder<T> {

    public static final String DEFAULT_SITE_FIELD_NAME = "crafterSite";
    public static final String DEFAULT_LOCAL_ID_FIELD_NAME = "localId";
    public static final String DEFAULT_PUBLISHING_DATE_FIELD_NAME = "publishingDate";
    public static final String DEFAULT_PUBLISHING_DATE_ALT_FIELD_NAME = "publishingDate_dt";

    protected String siteFieldName;
    protected String localIdFieldName;
    protected String publishingDateFieldName;
    protected String publishingDateAltFieldName;

    public AbstractDocumentBuilder() {
        siteFieldName = DEFAULT_SITE_FIELD_NAME;
        localIdFieldName = DEFAULT_LOCAL_ID_FIELD_NAME;
        publishingDateFieldName = DEFAULT_PUBLISHING_DATE_FIELD_NAME;
        publishingDateAltFieldName = DEFAULT_PUBLISHING_DATE_ALT_FIELD_NAME;
    }

    public void setSiteFieldName(String siteFieldName) {
        this.siteFieldName = siteFieldName;
    }

    public void setLocalIdFieldName(String localIdFieldName) {
        this.localIdFieldName = localIdFieldName;
    }

    public void setPublishingDateFieldName(String publishingDateFieldName) {
        this.publishingDateFieldName = publishingDateFieldName;
    }

    public void setPublishingDateAltFieldName(String publishingDateAltFieldName) {
        this.publishingDateAltFieldName = publishingDateAltFieldName;
    }

    /**
     * Adds the given fields to a native document
     * @param document the native document
     * @param id the local id for the document
     * @param site the site name
     * @param additionalFields additional fields to add
     */
    protected abstract void addFields(T document, String id, String site, Map<String, List<String>> additionalFields);

}