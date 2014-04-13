/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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
package org.craftercms.commons.rest;

import org.apache.commons.collections4.MapUtils;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Utilities for REST clients.
 *
 * @author avasquez
 */
public class RestClientUtils {

    private RestClientUtils() {
    }

    /**
     * Adds a param value to a params map. If value is null, nothing is done.
     *
     * @param key       the param key
     * @param value     the param value
     * @param params    the params map
     */
    public static void addValue(String key, Object value, MultiValueMap<String, String> params) {
        if (value != null) {
            params.add(key, value.toString());
        }
    }

    /**
     * Adds a collection of param values to a params map. If the collection is null, nothing is done.
     *
     * @param key       the param key
     * @param values    the collection of param values
     * @param params    the params map
     */
    public static void addValues(String key, Collection<String> values, MultiValueMap<String, String> params) {
        if (values != null) {
            for (String value : values) {
                params.add(key, value);
            }
        }
    }

    /**
     * Adds an array of param values to a params map. If the array is null, nothing is done.
     *
     * @param key       the param key
     * @param values    the array of param values
     * @param params    the params map
     */
    public static void addValues(String key, String[] values, MultiValueMap<String, String> params) {
        if (values != null) {
            for (String value : values) {
                params.add(key, value);
            }
        }
    }

    /**
     * Converts a map of params to a query string, and adds it to the specified URL.
     *
     * @param url           the URL to add the params to
     * @param params        the params to convert to a query string and add to the URL
     * @param encodeParams  if the param values should be encoded
     *
     * @return  the URL with the appended query string
     */
    public static String addQueryParams(String url, MultiValueMap<String, String> params, boolean encodeParams) {
        return url + createQueryStringFromParams(params, encodeParams);
    }

    /**
     * Converts a map of params to a query string.
     *
     * @param params    the params to convert to a query string
     * @param encode    if the param values should be encoded
     *
     * @return the params as a query string
     */
    public static String createQueryStringFromParams(MultiValueMap<String, String> params, boolean encode) {
        StringBuilder queryString = new StringBuilder();

        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String paramName;
                try {
                    paramName = URLEncoder.encode(entry.getKey(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // Should NEVER happen
                    throw new RuntimeException(e);
                }

                for (String paramValue : entry.getValue()) {
                    if (queryString.length() > 0) {
                        queryString.append('&');
                    }

                    if (encode) {
                        try {
                            paramValue = URLEncoder.encode(paramValue, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // Should NEVER happen
                            throw new RuntimeException(e);
                        }
                    }

                    queryString.append(paramName).append('=').append(paramValue);
                }
            }

            queryString.insert(0, '?');
        }

        return queryString.toString();
    }

}
