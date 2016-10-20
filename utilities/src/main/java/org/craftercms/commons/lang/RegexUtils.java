package org.craftercms.commons.lang;

import java.util.Arrays;
import java.util.List;

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
     * Returns true if the string matches any of the specified regexes.
     *
     * @param str       the string to match
     * @param regexes   the regexes used for matching
     *
     * @return true if the string matches one or more of the regexes
     */
    public static boolean matchesAny(String str, List<String> regexes) {
        if (CollectionUtils.isNotEmpty(regexes)) {
            for (String regex : regexes) {
                if (str.matches(regex)) {
                    return true;
                }
            }
        }

        return false;
    }

}
