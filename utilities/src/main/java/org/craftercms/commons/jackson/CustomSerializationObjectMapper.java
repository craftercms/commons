/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * Extended {@link com.fasterxml.jackson.databind.ObjectMapper} that lets you provide your own
 * serializers/deserializers.
 */
public class CustomSerializationObjectMapper extends ObjectMapper {

    public static final String MODULE_NAME = "CustomSerializationModule";

    protected List<JsonSerializer> serializers;
    protected Map<Class, JsonDeserializer> deserializers;

    public void setSerializers(List<JsonSerializer> serializers) {
        this.serializers = serializers;
    }

    public void setDeserializers(Map<Class, JsonDeserializer> deserializers) {
        this.deserializers = deserializers;
    }

    @PostConstruct
    public void init() {
        registerSerializationModule();
    }

    protected void registerSerializationModule() {
        SimpleModule module = new SimpleModule(MODULE_NAME, getModuleVersion());

        if (CollectionUtils.isNotEmpty(serializers)) {
            for (JsonSerializer<?> serializer : serializers) {
                module.addSerializer(serializer);
            }
        }

        if (MapUtils.isNotEmpty(deserializers)) {
            for (Map.Entry<Class, JsonDeserializer> entry : deserializers.entrySet()) {
                module.addDeserializer(entry.getKey(), entry.getValue());
            }
        }

        registerModule(module);
    }

    protected Version getModuleVersion() {
        return new Version(1, 0, 0, null, null, null);
    }

}
