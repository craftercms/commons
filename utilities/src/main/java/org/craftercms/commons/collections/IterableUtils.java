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
package org.craftercms.commons.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility methods for {@link java.lang.Iterable}.
 *
 * @author avasquez
 */
public class IterableUtils {

    private IterableUtils() {
    }

    /**
     * Creates a new list from the iterable elements.
     *
     * @param iterable the iterable
     * @return a list with the iterable elements
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = null;

        if (iterable != null) {
            list = new ArrayList<>();
            for (T elem : iterable) {
                list.add(elem);
            }
        }

        return list;
    }

    /**
     * Returns the number of elements the iterable contains.
     *
     * @param iterable the iterable
     * @return the element count of the iterable
     */
    public static <T> int count(Iterable<T> iterable) {
        int count = 0;

        if (iterable != null) {
            Iterator<T> iter = iterable.iterator();
            while (iter.hasNext()) {
                iter.next();
                count++;
            }
        }

        return count;
    }

}
