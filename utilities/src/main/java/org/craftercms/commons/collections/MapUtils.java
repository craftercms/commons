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

package org.craftercms.commons.collections;

import java.util.ArrayList;
import java.util.List;
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
     * @return a deep merge of the two given maps.
     */
    @SuppressWarnings("unchecked")
    public static Map deepMerge(Map dst, Map src) {
        if (dst != null && src != null) {
            for (Object key : src.keySet()) {
                if (src.get(key) instanceof Map && dst.get(key) instanceof Map) {
                    Map originalChild = (Map)dst.get(key);
                    Map newChild = (Map)src.get(key);
                    dst.put(key, deepMerge(originalChild, newChild));
                } else {
                    dst.put(key, src.get(key));
                }
            }
        }

        return dst;
    }

    /**
     * Adds the value under the specified key in the map. If there's no value in the map with the key, the value is
     * put as is. If there's already a value, the original and the new value are added to a collection and put under
     * the key.
     *
     * @param map the map
     * @param key the key
     * @param value the value to add
     */
    @SuppressWarnings("unchecked")
    public static <K> void add(Map<K, Object> map,  K key, Object value) {
        Object currentVal = map.get(key);
        if (currentVal != null) {
            if (currentVal instanceof List) {
                ((List<Object>) currentVal).add(value);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(currentVal);
                list.add(value);

                map.put(key, list);
            }
        } else {
            map.put(key, value);
        }
    }

}
