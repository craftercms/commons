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

import java.util.Map;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson Module to support mapping XML documents that include repeated tags using the {@link MixedMultivaluedMap}
 * class
 * @author joseross
 */
public class CrafterMultivaluedModule extends SimpleModule {

    public CrafterMultivaluedModule() {
        addAbstractTypeMapping(Map.class, MixedMultivaluedMap.class);
        addDeserializer(Object.class, new RepeatingGroupDeserializer());
    }

}