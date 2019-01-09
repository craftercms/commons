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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import org.bson.types.ObjectId;

/**
 * Custom Jackson serializer for {@link org.bson.types.ObjectId}s, which serializes an ObjectId as a String by
 * calling {@link org.bson.types.ObjectId#toString()}.
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(final ObjectId objectId, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(objectId.toString());
    }

    @Override
    public Class<ObjectId> handledType() {
        return ObjectId.class;
    }

}
