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

package org.craftercms.commons.elasticsearch;

import java.io.InputStream;

import org.springframework.util.MultiValueMap;

/**
 * Defines the operations to parse binary documents for indexing
 * @author joseross
 */
public interface DocumentParser {

    /**
     * Parses the given document and generates an XML file
     * @param content the document to parse
     * @param additionalFields additional fields to add
     * @return an XML ready to be indexed
     */
    String parseToXml(InputStream content, MultiValueMap<String, String> additionalFields);

}