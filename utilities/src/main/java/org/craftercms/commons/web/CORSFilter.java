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

package org.craftercms.commons.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpHeaders.VARY;
import static org.springframework.web.cors.CorsUtils.isCorsRequest;
import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;


/**
 * Filter to implement CORS headers validation. Based on Spring's DefaultCorsProcessor
 * @author joseross
 */
public class CORSFilter extends OncePerRequestFilter {

    public static final String ALL = "*";

    private String allowOrigins;
    private String allowMethods;
    private String maxAge;
    private String allowHeaders;
    private String allowCredentials;
    private boolean disableCORS = false;

    public void setAllowOrigins(final String allowOrigins) {
        this.allowOrigins = allowOrigins;
    }

    public void setAllowMethods(final String allowMethods) {
        this.allowMethods = allowMethods;
    }

    public void setMaxAge(final String maxAge) {
        this.maxAge = maxAge;
    }

    public void setAllowHeaders(final String allowHeaders) {
        this.allowHeaders = allowHeaders;
    }

    public void setAllowCredentials(final String allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getAllowOrigins() {
        return allowOrigins;
    }

    public String getAllowMethods() {
        return allowMethods;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public String getAllowCredentials() {
        return allowCredentials;
    }

    public void setDisableCORS(final boolean disableCORS) {
        this.disableCORS = disableCORS;
    }

    public boolean isDisableCORS() {
        return disableCORS;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        boolean isPreflight = isPreFlightRequest(request);

        String requestOrigin = request.getHeader(ORIGIN);
        String allowOrigin = checkOrigin(requestOrigin);

        String requestMethod = isPreflight?
            request.getHeader(ACCESS_CONTROL_REQUEST_METHOD) : request.getMethod();
        String allowedMethods = checkMethod(requestMethod);

        List<String> requestHeaders = getRequestHeaders(request, isPreflight);
        String allowedHeaders = checkHeaders(requestHeaders);

        if(StringUtils.isEmpty(allowOrigin) || StringUtils.isEmpty(allowedMethods) ||
            (isPreflight && StringUtils.isEmpty(allowedHeaders))) {
            rejectRequest(response);
        } else {
            response.addHeader(VARY, ORIGIN);
            response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
            if(isPreflight) {
                response.addHeader(ACCESS_CONTROL_ALLOW_METHODS, allowMethods);
                response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, getAllowHeaders());
            }
            response.addHeader(ACCESS_CONTROL_MAX_AGE, getMaxAge());
            response.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, getAllowCredentials());

            filterChain.doFilter(request, response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return isDisableCORS() || !isCorsRequest(request);
    }

    /**
     * Checks the origin from the request against the configured allowed origins
     * @param requestOrigin origin from the request header
     * @return the value to set in the response header
     */
    protected String checkOrigin(final String requestOrigin) {
        if(StringUtils.isEmpty(requestOrigin) || StringUtils.isEmpty(getAllowOrigins())) {
            return null;
        }

        if(getAllowOrigins().equals(ALL)) {
            if(Boolean.parseBoolean(getAllowCredentials())) {
                return requestOrigin;
            } else {
                return ALL;
            }
        }

        for(String allowedOrigin : getAllowOrigins().split(",")) {
            if(allowedOrigin.equalsIgnoreCase(requestOrigin)) {
                return requestOrigin;
            }
        }

        return null;
    }

    /**
     * Checks the method of the request against the configured allowed methods
     * @param requestMethod the method to check
     * @return the value to set in the response header
     */
    protected String checkMethod(final String requestMethod) {
        String allowedMethods = getAllowMethods();
        if(StringUtils.isEmpty(requestMethod) || StringUtils.isEmpty(allowedMethods)) {
            return null;
        } else if(ALL.equals(allowedMethods)) {
            return requestMethod;
        } else if(allowedMethods.toLowerCase().contains(requestMethod.toLowerCase())) {
            return allowedMethods;
        } else {
            return null;
        }
    }

    /**
     * Extracts the header names from the given request
     * @param request the request
     * @param isPreflight indicates if the request is preflight
     * @return the list of header names
     */
    protected List<String> getRequestHeaders(final HttpServletRequest request, final boolean isPreflight) {
        List<String> requestHeaders = new LinkedList<>();
        if(isPreflight) {
            String headers = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS);
            if(StringUtils.isNotEmpty(headers)) {
                requestHeaders = Arrays.asList(headers.split(","));
            }
        } else {
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                requestHeaders.add(headers.nextElement());
            }
        }
        return requestHeaders;
    }

    /**
     * Checks the given headers against the configured allowed headers
     * @param requestHeaders the headers to check
     * @return the value to set in the response header
     */
    protected String checkHeaders(final List<String> requestHeaders) {
        String allowedHeaders = getAllowHeaders();
        if(CollectionUtils.isEmpty(requestHeaders) || StringUtils.isEmpty(allowedHeaders)) {
            return null;
        }
        List<String> result = requestHeaders.stream()
            .filter(StringUtils::isNotEmpty)
            .map(StringUtils::trim)
            .map(header -> {
                if(ALL.equals(allowedHeaders) || allowedHeaders.toLowerCase().contains(header.toLowerCase())) {
                    return header;
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return String.join(",", result);
    }

    /**
     * Sends a response indicating that the request is invalid according to the configuration
     * @param response the response to send
     * @throws IOException if there is any error sending the response
     */
    protected void rejectRequest(final HttpServletResponse response) throws IOException {
        response.setStatus(SC_FORBIDDEN);
        response.getWriter().write("Invalid CORS request");
        response.flushBuffer();
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
