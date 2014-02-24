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
 *
 */
public final class MapUtils {
    private MapUtils() {
    }

    /**
     * Deep merges two maps
     * @param lhs Lhs map
     * @param rhs Rhs Map
     * @return A Deep merge of the two given maps.
     */
    public static Map deepMerge(Map lhs, Map rhs) {
        for (Object key : rhs.keySet()) {
            if (rhs.get(key) instanceof Map && lhs.get(key) instanceof Map) {
                Map originalChild = (Map) lhs.get(key);
                Map newChild = (Map) rhs.get(key);
                lhs.put(key, deepMerge(originalChild, newChild));
            } else {
                lhs.put(key, rhs.get(key));
            }
        }
        return lhs;
    }
}
