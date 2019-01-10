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
package org.craftercms.commons.lang;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility methods for regex related operations.
 *
 * @author avasquez
 */
public class RegexUtils {

    private RegexUtils() {
    }

    /**
     * Returns true if the string matches any of the specified regexes.
     *
     * @param str       the string to match
     * @param regexes   the regexes used for matching
     *
     * @return true if the string matches one or more of the regexes
     */
    public static boolean matchesAny(String str, String... regexes) {
        if (ArrayUtils.isNotEmpty(regexes)) {
            return matchesAny(str, Arrays.asList(regexes));
        } else {
            return false;
        }
    }

    /**
     * Returns true if the string matches (full match) any of the specified regexes.
     *
     * @param str       the string to match
     * @param regexes   the regexes used for matching
     *
     * @return true if the string matches (full match) one or more of the regexes
     */
    public static boolean matchesAny(String str, List<String> regexes) {
        return matchesAny(str, regexes, true);
    }

    /**
     * Returns true if the string matches any of the specified regexes.
     *
     * @param str       the string to match
     * @param regexes   the regexes used for matching
     * @param fullMatch if the entire string should be matched
     *
     * @return true if the string matches one or more of the regexes
     */
    public static boolean matchesAny(String str, List<String> regexes, boolean fullMatch) {
        if (CollectionUtils.isNotEmpty(regexes)) {
            for (String regex : regexes) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);

                if (fullMatch) {
                    if (matcher.matches()) {
                        return true;
                    }
                } else {
                    if (matcher.find()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
