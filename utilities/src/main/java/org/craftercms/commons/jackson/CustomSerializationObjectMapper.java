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
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Extended {@link com.fasterxml.jackson.databind.ObjectMapper} that lets you provide your own
 * serializers/deserializers.
 */
public class CustomSerializationObjectMapper extends ObjectMapper {

    protected List<JsonSerializer<?>> serializers;
    protected Map<Class<?>, JsonDeserializer<?>> deserializers;

    public void setSerializers(List<JsonSerializer<?>> serializers) {
        this.serializers = serializers;
    }

    public void setDeserializers(Map<Class<?>, JsonDeserializer<?>> deserializers) {
        this.deserializers = deserializers;
    }

    @PostConstruct
    public void init() {
        registerSerializationModule();
    }

    protected void registerSerializationModule() {
        registerModule(JacksonUtils.createModule(serializers, deserializers));
    }

}
