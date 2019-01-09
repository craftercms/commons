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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for the {@link java.util.Set} collection.
 *
 * @author avasquez
 */
public class SetUtils {

    private SetUtils() {
    }

    /**
     * Creates a set from the array elements.
     *
     * @param array the array with the elements
     *
     * @return the set with the elements
     */
    @SafeVarargs
    public static <T> Set<T> asSet(T... array) {
        Set<T> set = null;
        if (array != null) {
           set = new HashSet<>(Arrays.asList(array));
        }
        return set;
    }
}
