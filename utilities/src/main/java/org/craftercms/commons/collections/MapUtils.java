/*
 * Copyright (C) 2007-${year} Crafter Software Corporation.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.collections;

import java.util.Map;

/**
 * Utility methods for maps.
 *
 * @author cortiz
 */
public final class MapUtils {

    private MapUtils() {
    }

    /**
     * Deep merges two maps
     *
     * @param dst the map where elements will be merged into
     * @param src the map with the elements to merge
     *
     * @return a deep merge of the two given maps.
     */
    public static Map deepMerge(Map dst, Map src) {
        if (dst != null && src != null) {
            for (Object key : src.keySet()) {
                if (src.get(key) instanceof Map && dst.get(key) instanceof Map) {
                    Map originalChild = (Map) dst.get(key);
                    Map newChild = (Map) src.get(key);
                    dst.put(key, deepMerge(originalChild, newChild));
                } else {
                    dst.put(key, src.get(key));
                }
            }
        }

        return dst;
    }
}
