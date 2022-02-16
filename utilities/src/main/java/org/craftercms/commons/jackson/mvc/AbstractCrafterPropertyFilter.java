/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.jackson.mvc;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;

/**
 *
 */
public abstract class AbstractCrafterPropertyFilter extends SimpleBeanPropertyFilter {

    public abstract String getFilterName();


    protected boolean isPrimitive(final Class<?> clazz) {
        return Date.class.isAssignableFrom(clazz) || ClassUtils.isPrimitiveOrWrapper(clazz) || List.class
            .isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
    }

}
