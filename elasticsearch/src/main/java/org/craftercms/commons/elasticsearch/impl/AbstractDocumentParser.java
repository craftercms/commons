/*
 * Copyright (C) 2007-2018 Crafter Software Corporation. All Rights Reserved.
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

import org.craftercms.commons.elasticsearch.DocumentParser;

/**
 * Base class for all implementations of {@link DocumentParser}
 * @author joseross
 */
public abstract class AbstractDocumentParser implements DocumentParser {

    private static final String DEFAULT_FIELD_NAME_CONTENT = "content";
    private static final String DEFAULT_FIELD_NAME_AUTHOR = "author";
    private static final String DEFAULT_FIELD_NAME_TITLE = "title";
    private static final String DEFAULT_FIELD_NAME_KEYWORDS = "keywords";
    private static final String DEFAULT_FIELD_NAME_DESCRIPTION = "description";
    private static final String DEFAULT_FIELD_NAME_CONTENT_TYPE = "content-type";
    private static final String DEFAULT_FIELD_NAME_CREATED = "created";
    private static final String DEFAULT_FIELD_NAME_MODIFIED = "modified";

    protected String fieldNameContent = DEFAULT_FIELD_NAME_CONTENT;

    protected String fieldNameAuthor = DEFAULT_FIELD_NAME_AUTHOR;

    protected String fieldNAmeTitle = DEFAULT_FIELD_NAME_TITLE;

    protected String fieldNameKeywords = DEFAULT_FIELD_NAME_KEYWORDS;

    protected String fieldNameDescription = DEFAULT_FIELD_NAME_DESCRIPTION;

    protected String fieldNameContentType = DEFAULT_FIELD_NAME_CONTENT_TYPE;

    protected String fieldNameCreated = DEFAULT_FIELD_NAME_CREATED;

    protected String fieldNameModified = DEFAULT_FIELD_NAME_MODIFIED;

    public void setFieldNameContent(final String fieldNameContent) {
        this.fieldNameContent = fieldNameContent;
    }

    public void setFieldNameAuthor(final String fieldNameAuthor) {
        this.fieldNameAuthor = fieldNameAuthor;
    }

    public void setFieldNAmeTitle(final String fieldNAmeTitle) {
        this.fieldNAmeTitle = fieldNAmeTitle;
    }

    public void setFieldNameKeywords(final String fieldNameKeywords) {
        this.fieldNameKeywords = fieldNameKeywords;
    }

    public void setFieldNameDescription(final String fieldNameDescription) {
        this.fieldNameDescription = fieldNameDescription;
    }

    public void setFieldNameContentType(final String fieldNameContentType) {
        this.fieldNameContentType = fieldNameContentType;
    }

    public void setFieldNameCreated(final String fieldNameCreated) {
        this.fieldNameCreated = fieldNameCreated;
    }

    public void setFieldNameModified(final String fieldNameModified) {
        this.fieldNameModified = fieldNameModified;
    }

}