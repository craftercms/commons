/*
 * Copyright (C) 2007-2026 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;

import static java.lang.String.format;

/**
 * Custom Jackson deserializer for enums that allows case-insensitive deserialization.
 * This is useful for cases where the JSON input may not match the exact case of the enum constants.
 */
public class CaseInsensitiveEnumDeserializer extends JsonDeserializer implements ContextualDeserializer {
	private JavaType fieldType;

	public CaseInsensitiveEnumDeserializer() {
	}

	private CaseInsensitiveEnumDeserializer(JavaType fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
			throws JsonMappingException {
		// 'property' can be null if the deserializer is for a root-level value, not a bean property.
		if (property != null) {
			// Get the declared type of the field
			JavaType type = property.getType();
			return new CaseInsensitiveEnumDeserializer(type);
		}
		return this;
	}

	@Override
	public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		String value = p.getText();
		Class<Enum<?>> enumClass = (Class<Enum<?>>) fieldType.getRawClass();
		for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
			if (enumConstant.name().equalsIgnoreCase(value)) {
				return enumConstant;
			}
		}
		throw new InvalidFormatException(p, format("Unable to deserialize value '%s' to enum %s", value,
				fieldType.getRawClass().getName()), value, enumClass);
	}
}
