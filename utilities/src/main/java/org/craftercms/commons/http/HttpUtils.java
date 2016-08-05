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
package org.craftercms.commons.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Utility methods for HTTP related stuff.
 *
 * @author avasquez
 */
public class HttpUtils {

    public static final String HTTP_SCHEME =        "http";
    public static final String HTTPS_SCHEME =       "https";
    public static final int DEFAULT_HTTP_PORT =     80;
    public static final int DEFAULT_HTTPS_PORT =    443;

    public static final String PRAGMA_HEADER_NAME = "Pragma";
    public static final String CACHE_CONTROL_HEADER_NAME = "Cache-Control";
    public static final String EXPIRES_HEADER_NAME = "Expires";

    /**
     * Returns the portion from the URL that includes the scheme, server name and port number, without the server
     * path.
     *
     * @param request       the request object used to build the base URL
     * @param forceHttps    if HTTPS should be enforced
     *
     * @return the base request URL
     */
    public static StringBuilder getBaseRequestUrl(HttpServletRequest request, boolean forceHttps) {
        String scheme = forceHttps ? HTTPS_SCHEME : request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        StringBuilder url = new StringBuilder();

        url.append(scheme).append("://").append(serverName);

        if (!(scheme.equals(HTTP_SCHEME) && serverPort == DEFAULT_HTTP_PORT) &&
            !(scheme.equals(HTTPS_SCHEME) && serverPort == DEFAULT_HTTPS_PORT)) {
            url.append(":").append(serverPort);
        }

        return url;
    }

    /**
     * Returns the full URL for the relative URL based in the specified request. The full URL includes the scheme,
     * server name, port number, context path and server path.
     *
     * @param request       the request object used to build the base URL
     * @param relativeUrl   the relative URL
     *
     * @return the full URL
     */
    public static String getFullUrl(HttpServletRequest request, String relativeUrl) {
        StringBuilder baseUrl = getBaseRequestUrl(request, false);
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        if (contextPath.equals("/")) {
            contextPath = "";
        }
        if (servletPath.equals("/")) {
            servletPath = "";
        }

        return baseUrl.append(contextPath).append(servletPath).append(relativeUrl).toString();
    }

    /**
     * Returns the request URI without the context path.
     *
     * @param request   the request where to get the URI from
     */
    public static final String getRequestUriWithoutContextPath(HttpServletRequest request) {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (!uri.isEmpty()) {
            return uri;
        } else {
            return "/";
        }
    }

    /**
     * Returns the full request URI, including scheme, server name, port number and server path. The query string
     * is optional.
     *
     * @param request               the request object used to build the URI
     * @param includeQueryString    if the query string should be appended to the URI
     *
     * @return the full request URI
     */
    public static final String getFullRequestUri(HttpServletRequest request, boolean includeQueryString) {
        StringBuilder uri = getBaseRequestUrl(request, false).append(request.getRequestURI());

        if (includeQueryString) {
            String queryStr = request.getQueryString();
            if (StringUtils.isNotEmpty(queryStr)) {
                uri.append('?').append(queryStr);
            }
        }

        return uri.toString();
    }

    /**
     * Returns the cookie with the given name for the given request
     *
     * @param name      the name of the cookie
     * @param request   the request where to extract the request from
     *
     * @return the cookie object, or null if not found
     */
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * Returns the cookie value with the given name for the given request
     *
     * @param name      the name of the cookie
     * @param request   the request where to extract the request from
     *
     * @return the cookie value, or null if no cookie found
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * Returns a map with the extracted parameters from the specified query string. A multi value map is used
     * since there can be several values for the same param.
     *
     * @param queryString the query string to extract the params from
     * @return the param map
     */
    public static MultiValueMap<String, String> getParamsFromQueryString(String queryString) {
        MultiValueMap queryParams = new LinkedMultiValueMap<>();

        if (StringUtils.isNotEmpty(queryString)) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] splitParam = param.split("=");
                String paramName = splitParam[0];
                String paramValue = splitParam[1];

                queryParams.add(paramName, paramValue);
            }
        }

        return queryParams;
    }

    /**
     * Builds a query string from the specified params. The param name and value are also URL encoded before adding
     * them to the query string.
     *
     * @param queryParams   the params to build the query string with
     * @param charset       the charset to use for URL encoding
     *
     * @return the query string
     */
    public static String getQueryStringFromParams(MultiValueMap<String, String> queryParams,
                                                  String charset) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();

        if (MapUtils.isNotEmpty(queryParams)) {
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                String paramName = URLEncoder.encode(entry.getKey(), charset);

                for (String paramValue : entry.getValue()) {
                    if (queryString.length() > 0) {
                        queryString.append('&');
                    }

                    paramValue = URLEncoder.encode(paramValue, charset);

                    queryString.append(paramName).append('=').append(paramValue);
                }
            }

            queryString.insert(0, '?');
        }

        return queryString.toString();
    }

    /**
     * Creates a map from the parameters in the specified request. Multi value parameters will be in an array.
     *
     * @param request the request
     */
    public static Map<String, Object> createRequestParamsMap(HttpServletRequest request) {
        Map<String, Object> paramsMap = new HashMap<>();
        for (Enumeration paramNameEnum = request.getParameterNames(); paramNameEnum.hasMoreElements(); ) {
            String paramName = (String)paramNameEnum.nextElement();
            String[] paramValues = request.getParameterValues(paramName);

            if (paramValues.length == 1) {
                paramsMap.put(paramName, paramValues[0]);
            } else {
                paramsMap.put(paramName, paramValues);
            }
        }

        return paramsMap;
    }

    /**
     * Creates a map from the request attributes in the specified request.
     *
     * @param request the request
     */
    public static Map<String, Object> createRequestAttributesMap(HttpServletRequest request) {
        Map<String, Object> attributesMap = new HashMap<>();
        for (Enumeration attributeNameEnum = request.getAttributeNames(); attributeNameEnum.hasMoreElements(); ) {
            String attributeName = (String)attributeNameEnum.nextElement();

            attributesMap.put(attributeName, request.getAttribute(attributeName));
        }

        return attributesMap;
    }

    /**
     * Creates a map from the headers in the specified request. Multi value headers will be in an array.
     *
     * @param request the request
     */
    public static Map<String, Object> createHeadersMap(HttpServletRequest request) {
        Map<String, Object> headersMap = new HashMap<>();
        for (Enumeration headerNameEnum = request.getHeaderNames(); headerNameEnum.hasMoreElements(); ) {
            String headerName = (String)headerNameEnum.nextElement();
            List<String> headerValues = new ArrayList<String>();

            CollectionUtils.addAll(headerValues, request.getHeaders(headerName));

            if (headerValues.size() == 1) {
                headersMap.put(headerName, headerValues.get(0));
            } else {
                headersMap.put(headerName, headerValues.toArray(new String[headerValues.size()]));
            }
        }

        return headersMap;
    }

    /**
     * Creates a map from the cookies in the specified request.
     *
     * @param request the request
     */
    public static Map<String, String> createCookiesMap(HttpServletRequest request) {
        Map<String, String> cookiesMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();

        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : request.getCookies()) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
        }

        return cookiesMap;
    }

    /**
     * Creates a map from the session attributes in the specified request.
     *
     * @param request the request
     */
    public static Map<String, Object> createSessionMap(HttpServletRequest request) {
        Map<String, Object> sessionMap = new HashMap<>();
        HttpSession session = request.getSession(false);

        if (session != null) {
            for (Enumeration attributeNameEnum = session.getAttributeNames(); attributeNameEnum.hasMoreElements(); ) {
                String attributeName = (String)attributeNameEnum.nextElement();
                sessionMap.put(attributeName, session.getAttribute(attributeName));
            }
        }

        return sessionMap;
    }

    /**
     * Disable caching in the client.
     *
     * @param response the response to add the headers for disabling caching.
     */
    public static void disableCaching(HttpServletResponse response) {
        response.addHeader(PRAGMA_HEADER_NAME, "no-cache");
        response.addHeader(CACHE_CONTROL_HEADER_NAME, "no-cache, no-store, max-age=0");
        response.addDateHeader(EXPIRES_HEADER_NAME, 1L);
    }

}
