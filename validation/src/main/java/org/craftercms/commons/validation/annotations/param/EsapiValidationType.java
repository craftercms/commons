/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.validation.annotations.param;

/**
 * Supported ESAPI validator "types".
 */
public enum EsapiValidationType {

    ALPHANUMERIC("ALPHANUMERIC"),
    ASSET_PATH_WRITE("ASSET_PATH_WRITE"),
    ASSET_PATH_READ("ASSET_PATH_READ"),
    CONTENT_FILE_NAME_WRITE("CONTENT_FILE_NAME_WRITE"),
    CONTENT_PATH_WRITE("CONTENT_PATH_WRITE"),
    CONTENT_PATH_READ("CONTENT_PATH_READ"),
    CONFIGURATION_PATH("CONFIGURATION_PATH"),
    HTTPParameterName("HTTPParameterName"),
    SITE_ID("SITEID"),
    EMAIL("EMAIL"),
    USERNAME("USERNAME"),
    GROUP_NAME("GROUP_NAME"),
    SEARCH_KEYWORDS("SEARCH_KEYWORDS"),
    SQL_ORDER_BY("SQL_ORDER_BY");

    public final String typeKey;

    EsapiValidationType(final String typeKey) {
        this.typeKey = typeKey;
    }
}
