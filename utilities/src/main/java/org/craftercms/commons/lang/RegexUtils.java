package org.craftercms.commons.lang;

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
    public static boolean matchesAny(String str, String[] regexes) {
        for (String regex : regexes) {
            if (str.matches(regex)) {
                return true;
            }
        }

        return false;
    }

}
