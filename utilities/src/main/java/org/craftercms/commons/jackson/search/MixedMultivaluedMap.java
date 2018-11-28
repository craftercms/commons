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

package org.craftercms.commons.jackson.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link java.util.Map} that can hold a single value or a list for a given key.
 * @author joseross
 */
public class MixedMultivaluedMap extends HashMap<String, Object> {

    @Override
    @SuppressWarnings("unchecked")
    public Object put(final String key, final Object value) {
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