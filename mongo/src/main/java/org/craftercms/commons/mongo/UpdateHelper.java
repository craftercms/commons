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
package org.craftercms.commons.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by alfonsovasquez on 13/6/16.
 */
public class UpdateHelper {

    protected Map<String, Object> setValues;
    protected Map<String, Object> unsetValues;
    protected Map<String, Object> pushValues;
    protected Map<String, Object> pullValues;

    public void set(String field, Object value) {
        setValues = add(setValues, field, value);
    }

    public void unset(String field) {
        unsetValues = add(unsetValues, field, "");
    }

    public void push(String field, Object value) {
        pushValues = add(pushValues, field, value);
    }

    public void pushAll(String field, Collection<?> values) {
        pushValues = add(pushValues, field, Collections.singletonMap("$each", values));
    }

    public void pull(String field, Object value) {
        pullValues = add(pullValues, field, value);
    }

    public void pullAll(String field, Collection<?> values) {
        pullValues = add(pullValues, field, Collections.singletonMap("$in", values));
    }

    public void pullAllDocuments(String field, String embeddedField, Collection<?> values) {
        pullValues = add(pullValues, field, Collections.singletonMap(embeddedField, Collections.singletonMap("$in",
            values)));
    }

    public void executeUpdate(String id, CrudRepository<?> repository) throws MongoDataException {
        List<String> modifiers = new ArrayList<>();
        List<Map<String, Object>> params = new ArrayList<>();

        if (MapUtils.isNotEmpty(setValues)) {
            modifiers.add("$set: #");
            params.add(setValues);
        }
        if (MapUtils.isNotEmpty(unsetValues)) {
            modifiers.add("$unset: #");
            params.add(unsetValues);
        }
        if (MapUtils.isNotEmpty(pushValues)) {
            modifiers.add("$push: #");
            params.add(pushValues);
        }
        if (MapUtils.isNotEmpty(pullValues)) {
            modifiers.add("$pull: #");
            params.add(pullValues);
        }

        if(!modifiers.isEmpty() && !params.isEmpty()) {
            String finalModifier = "{" + StringUtils.join(modifiers, ", ") + "}";
            Object[] paramsArray = params.toArray(new Object[params.size()]);

            repository.update(id, finalModifier, false, false, paramsArray);
        }
    }

    protected Map<String, Object> add(Map<String, Object> map, String field, Object value) {
        if (map == null) {
            map = new HashMap<>();
        }

        map.put(field, value);

        return map;
    }

}
