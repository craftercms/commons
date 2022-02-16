/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.file.blob;

/**
 * Defines operations for blob file urls
 *
 * @author joseross
 * @since 3.1.6
 */
public interface BlobUrlResolver {

    /**
     * Returns the blob file url for the given content
     * @param contentUrl the url of the content
     * @return the blob url
     */
    String getBlobUrl(String contentUrl);

    /**
     * Returns the content url for the given blob file
     * @param blobUrl the url of the blob file
     * @return the content url
     */
    String getContentUrl(String blobUrl);

}
