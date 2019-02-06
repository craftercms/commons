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
package org.craftercms.commons.spring;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.configuration2.Configuration;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.List;

/**
 * Implementation of {@link EnumerablePropertySource} where a source is an Apache Commons Configuration 2 {@link Configuration}.
 *
 * @author avasquez
 */
public class ApacheCommonsConfiguration2PropertySource extends EnumerablePropertySource<Configuration> {

    public ApacheCommonsConfiguration2PropertySource(String name, Configuration source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return IteratorUtils.toArray(source.getKeys(), String.class);
    }

    @Override
    public Object getProperty(String name) {
        Object value = source.getProperty(name);
        // Call the appropriate getters to resolve ${placeholder}s
        if (value instanceof String) {
            return source.getString(name);
        } else if (value instanceof List) {
            return source.getList(name);
        } else {
            return value;
        }
    }

}
