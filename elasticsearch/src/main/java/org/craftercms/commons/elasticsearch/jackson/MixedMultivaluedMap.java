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

package org.craftercms.commons.elasticsearch.jackson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Implementation of {@link java.util.Map} that can hold a single value or a list for a given key.
 * @author joseross
 */
public class MixedMultivaluedMap extends HashMap<String, Object> {

    @Override
    @SuppressWarnings("unchecked")
    public Object put(final String key, Object value) {

        // This is needed because of the way Jackson parses XML elements with attributes.
        if(value instanceof Map) {
            Map map = (Map) value;
            if(map.containsKey(StringUtils.EMPTY)) {
                value = map.get(StringUtils.EMPTY);
            }
        }

        if(containsKey(key)) {
            Object currentValue = get(key);
            if(currentValue instanceof List) {
                List<Object> original = new LinkedList<>((List<Object>)currentValue);
                ((List)currentValue).add(value);
                return original;
            } else {
                List<Object> list = new LinkedList<>();
                list.add(currentValue);
                list.add(value);
                return super.put(key, list);
            }
        } else {
            return super.put(key, value);
        }
    }

}