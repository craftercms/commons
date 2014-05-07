/*
 * Copyright (C) 2007-${year} Crafter Software Corporation.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.mongo;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

/**
 *
 */
public class Tes {

    public static void main(String[] args) {
        DefaultKeyValue<String, Boolean> valuea = new DefaultKeyValue<>("d", false);
        DefaultKeyValue<String, Boolean> value1 = new DefaultKeyValue<>("A", false);
        DefaultKeyValue<String, Boolean> value2 = new DefaultKeyValue<>("b", false);
        DefaultKeyValue<String, Boolean> value3 = new DefaultKeyValue<>("z", false);
        DefaultKeyValue<String, Boolean> value4 = new DefaultKeyValue<>("d", false);
        DefaultKeyValue<String, Boolean> value5 = new DefaultKeyValue<>("o", false);
        DefaultKeyValue<String, Boolean> value6 = new DefaultKeyValue<>("8", false);
        StringBuilder builder = new StringBuilder("{");
        Iterator<DefaultKeyValue<String, Boolean>> iter = Arrays.asList(value1, value2, value3, value4, value5,
            valuea, value6).iterator();
        while (iter.hasNext()) {
            DefaultKeyValue<String, Boolean> field = iter.next();
            builder.append("\"");
            builder.append(field.getKey());
            builder.append("\"");
            builder.append(":");
            if (field.getValue()) {
                builder.append(1);
            } else {
                builder.append(-1);
            }
            if (iter.hasNext()) {
                builder.append(",");
            }
        }
        builder.append("}");
        System.out.println(builder.toString());
    }
}
