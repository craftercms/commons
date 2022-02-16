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
package org.craftercms.commons.file.blob.impl;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.file.blob.BlobUrlResolver;

import java.beans.ConstructorProperties;

/**
 * Default implementation of {@link BlobUrlResolver}
 *
 * @author joseross
 * @since 3.1.6
 */
public class BlobUrlResolverImpl implements BlobUrlResolver {

    /**
     * Extension used for the blob files
     */
    protected String fileExtension;

    @ConstructorProperties({"fileExtension"})
    public BlobUrlResolverImpl(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String getBlobUrl(String contentUrl) {
        return StringUtils.appendIfMissing(contentUrl, "." + fileExtension);
    }

    @Override
    public String getContentUrl(String blobUrl) {
        return StringUtils.removeEnd(blobUrl, "." + fileExtension);
    }

}
