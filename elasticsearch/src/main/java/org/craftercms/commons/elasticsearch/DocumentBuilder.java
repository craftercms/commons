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

package org.craftercms.commons.elasticsearch;

/**
 * Defines the operations to build native documents for a search provider
 * @param <T> type for the native documents
 * @author joseross
 */
public interface DocumentBuilder<T> {

    /**
     * Builds a document from the given XML
     * @param site the site name
     * @param id the local id for the document
     * @param xml the XML for the document
     * @return the native document
     */
    T build(String site, String id, String xml);

}