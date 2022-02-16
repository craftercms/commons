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
package org.craftercms.commons.locale;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.union;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.lang3.LocaleUtils.localeLookupList;
import static org.apache.commons.lang3.StringUtils.isAnyEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * Utility class for handling locale codes
 *
 * @author joseross
 * @since 4.0.0
 */
public abstract class LocaleUtils {

    public static final String CONFIG_KEY_DEFAULT_LOCALE = "defaultLocaleCode";
    public static final String CONFIG_KEY_SUPPORTED_LOCALES = "localeCodes.localeCode";
    public static final String CONFIG_KEY_FALLBACK = "fallbackToDefaultLocale";

    public static final String LOCALE_SEPARATOR = "_";
    public static final Pattern LOCALE_PATTERN = Pattern.compile("_(\\w\\w(?:_\\w\\w)?)\\.");

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
        return localeValues.stream().map(LocaleUtils::parseLocale).collect(toList());
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

    public static String localizePath(String path, Locale locale) {
        return getFullPath(path) + getBaseName(path) + LOCALE_SEPARATOR +
                LocaleUtils.toString(locale) + "." + getExtension(path);
    }

    public static String delocalizePath(String path) {
        var matcher = LOCALE_PATTERN.matcher(path);
        if (matcher.find()) {
            var localeValue = matcher.group(1);
            try {
                var locale = parseLocale(localeValue);
                if (locale != null) {
                    return getFullPath(path) + substringBeforeLast(getBaseName(path), LOCALE_SEPARATOR) +
                            "." + getExtension(path);
                }
            } catch (IllegalArgumentException e) {
                // invalid locale code, ignore
            }
        }
        return path;
    }

    public static String findPath(String path, Locale primary, Locale fallback, Predicate<String> exists) {
        return getCompatibleLocales(primary, fallback).stream()
                .map(locale -> localizePath(path, locale))
                .filter(exists)
                .findFirst()
                .orElse(path);
    }

    public static List<String> appendLocales(String name, Locale primary, Locale fallback) {
        return getCompatibleLocales(primary, fallback).stream()
                .map(locale -> appendLocale(name, locale))
                .collect(toList());
    }

    public static List<Locale> getCompatibleLocales(Locale primary, Locale fallback) {
        if (fallback != null) {
            return union(localeLookupList(primary), localeLookupList(fallback));
        } else {
            return localeLookupList(primary);
        }
    }

    public static List<Locale> getCompatibleLocales(Locale locale) {
        return getCompatibleLocales(locale, null);
    }

}
