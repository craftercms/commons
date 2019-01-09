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
package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Simple dummy converter that just returns a String value.
 *
 * @author avasquez
 */
public class StringToStringConverter implements Converter<String, String> {

    @Override
    public Class<?> getSourceClass() {
        return String.class;
    }

    @Override
    public Class<?> getTargetClass() {
        return String.class;
    }

    @Override
    public String convert(String source) {
        return source;
    }

}
