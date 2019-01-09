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
package org.craftercms.commons.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * Utility methods for Jackson.
 *
 * @author avasquez
 */
public class JacksonUtils {

    private JacksonUtils() {
    }

    /**
     * Creates a module from a set of serializers and deserializes.
     *
     * @param serializers   the serializers, can be null or empty
     * @param deserializers the deserializers, can be null or empty
     *
     * @return a non-reusable Jackson module composed of the specified serializers and deserializers
     */
    @SuppressWarnings("unchecked")
    public static final Module createModule(List<JsonSerializer<?>> serializers,
                                            Map<Class<?>, JsonDeserializer<?>> deserializers) {
        SimpleModule module = new SimpleModule();

        if (CollectionUtils.isNotEmpty(serializers)) {
            for (JsonSerializer<?> serializer : serializers) {
                module.addSerializer(serializer);
            }
        }

        if (MapUtils.isNotEmpty(deserializers)) {
            for (Map.Entry<Class<?>, JsonDeserializer<?>> entry : deserializers.entrySet()) {
                Class<Object> type = (Class<Object>) entry.getKey();
                JsonDeserializer<Object> des = (JsonDeserializer<Object>) entry.getValue();

                module.addDeserializer(type, des);
            }
        }

        return module;
    }

}
