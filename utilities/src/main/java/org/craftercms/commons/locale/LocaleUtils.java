/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.locale;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isAnyEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

/**
 * Utility class for handling locale codes
 *
 * @author joseross
 * @since 3.2.0
 */
public abstract class LocaleUtils {

    public static final String CONFIG_KEY_DEFAULT_LOCALE = "defaultLocaleCode";
    public static final String CONFIG_KEY_SUPPORTED_LOCALES = "localeCodes.localeCode";
    public static final String CONFIG_KEY_LOCALE_RESOLVER = "localeResolvers.localeResolver";
    public static final String CONFIG_KEY_FALLBACK = "fallbackToDefaultLocale";

    public static final String LOCALE_SEPARATOR = "_";

    public static Locale parseLocale(String localeValue) {
        Locale locale = null;
        if (isNotEmpty(localeValue)) {
            if (localeValue.contains(LOCALE_SEPARATOR)) {
                String language = substringBefore(localeValue, LOCALE_SEPARATOR);
                String country = substringAfter(localeValue, LOCALE_SEPARATOR);
                if (isAnyEmpty(language, country)) {
                    throw new IllegalArgumentException("Invalid locale code " + localeValue);
                }
                locale = new Locale(language, country);
            } else {
                locale = new Locale(localeValue);
            }
        }
        return locale;
    }

    public static List<Locale> parseLocales(List<String> localeValues) {
        return localeValues.stream().map(LocaleUtils::parseLocale).collect(Collectors.toList());
    }

    public static String toString(Locale locale) {
        StringBuilder sb = new StringBuilder(locale.getLanguage().toLowerCase());
        if (isNotEmpty(locale.getCountry())) {
            sb.append(LOCALE_SEPARATOR)
              .append(locale.getCountry().toLowerCase());
        }
        return sb.toString();
    }

    public static String appendLocale(String str, Locale locale) {
        return String.format("%s-%s", str, toString(locale));
    }

}
