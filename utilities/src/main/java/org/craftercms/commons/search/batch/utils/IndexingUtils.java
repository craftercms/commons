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

package org.craftercms.commons.search.batch.utils;

import java.util.List;
import javax.activation.FileTypeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MimeType;

/**
 * @author joseross
 */
public abstract class IndexingUtils {

    public static boolean isMimeTypeSupported(FileTypeMap mimeTypesMap, List<String> supportedMimeTypes,
                                              String filename) {
        if (mimeTypesMap != null && CollectionUtils.isNotEmpty(supportedMimeTypes)) {
            MimeType mimeType = MimeType.valueOf(mimeTypesMap.getContentType(filename.toLowerCase()));
            return supportedMimeTypes.stream().anyMatch(type -> MimeType.valueOf(type).isCompatibleWith(mimeType));
        } else {
            return true;
        }
    }

    public static String getSiteBasedPath(String siteName, String path) {
        return siteName + ":" + path;
    }

    public static String getIndexNameStr(String indexId) {
        return StringUtils.isNotEmpty(indexId)? "'" + indexId + "'": "default";
    }

}
