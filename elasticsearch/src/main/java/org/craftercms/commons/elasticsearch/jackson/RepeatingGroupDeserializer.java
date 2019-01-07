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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;

/**
 * Custom Jackson deserializer to prevent repeated XML tags from being overwritten.
 *
 * This is needed because of a known limitation of Jackson and it is a temporary fix taken from
 * https://gist.github.com/joaovarandas/1543e792ed6204f0cf5fe860cb7d58ed
 *
 * Jackson issue https://github.com/FasterXML/jackson-dataformat-xml/issues/205
 *
 * @author joseross
 */
public class RepeatingGroupDeserializer extends UntypedObjectDeserializer {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object mapObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        String firstKey;

        JsonToken t = p.getCurrentToken();

        if (t == JsonToken.START_OBJECT) {
            firstKey = p.nextFieldName();
        } else if (t == JsonToken.FIELD_NAME) {
            firstKey = p.getCurrentName();
        } else {
            if (t != JsonToken.END_OBJECT) {
                throw ctxt.mappingException(handledType(), p.getCurrentToken());
            }
            firstKey = null;
        }

        // empty map might work; but caller may want to modify... so better
        // just give small modifiable
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>(2);
        if (firstKey == null)
            return resultMap;

        p.nextToken();
        resultMap.put(firstKey, deserialize(p, ctxt));

        // 03-Aug-2016, jpvarandas: handle next objects and create an array
        Set<String> listKeys = new LinkedHashSet<>();

        String nextKey;
        while ((nextKey = p.nextFieldName()) != null) {
            p.nextToken();
            if (resultMap.containsKey(nextKey)) {
                Object listObject = resultMap.get(nextKey);

                if (!(listObject instanceof List)) {
                    listObject = new ArrayList<>();
                    ((List) listObject).add(resultMap.get(nextKey));

                    resultMap.put(nextKey, listObject);
                }

                ((List) listObject).add(deserialize(p, ctxt));

                listKeys.add(nextKey);

            } else {
                resultMap.put(nextKey, deserialize(p, ctxt));

            }
        }

        return resultMap;

    }

}