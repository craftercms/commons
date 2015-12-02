/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.web;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 *
 */
public class CORSFilter extends OncePerRequestFilter {

    public static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String MAX_AGE = "Access-Control-Max-Age";
    public static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ALLOW_CREDENTIALS ="Access-Control-Allow-Credentials";

    private String allowOrigins;
    private String allowMethods;
    private String maxAge;
    private String allowHeaders;
    private String allowCredentials;
    private boolean disableCORS=false;

    @Override
    public void destroy() {

    }

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

    public void setDisableCORS(final boolean disableCORS) {
        this.disableCORS = disableCORS;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        if(!disableCORS) {
            responseWrapper.addHeader(ALLOW_ORIGIN, allowOrigins);
            responseWrapper.addHeader(ALLOW_METHODS, allowMethods);
            responseWrapper.addHeader(MAX_AGE, maxAge);
            responseWrapper.addHeader(ALLOW_HEADERS, allowHeaders);
            responseWrapper.addHeader(ALLOW_CREDENTIALS, allowCredentials);
        }
        filterChain.doFilter(request, responseWrapper);
    }

}
