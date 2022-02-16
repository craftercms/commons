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
package org.craftercms.commons.web;

import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.beans.ConstructorProperties;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.enumeration;
import static java.util.Collections.list;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 *  Filter to provide support for AWS Cloudfront specific request headers.
 *  This class will copy the value of the headers using the standard name to integrate with all Spring classes.
 *
 * @author joseross
 * @since 3.1.9
 */
public class CloudfrontForwardedHeaderFilter extends OncePerRequestFilter {

    public static final String CLOUDFRONT_PROTO_HEADER_NAME = "CloudFront-Forwarded-Proto";
    public static final String STANDARD_PROTO_HEADER_NAME = "X-Forwarded-Proto";

    protected boolean enabled;

    @ConstructorProperties({"enabled"})
    public CloudfrontForwardedHeaderFilter(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !enabled || isEmpty(request.getHeader(CLOUDFRONT_PROTO_HEADER_NAME));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(new CloudfrontHttpServletRequestWrapper(request), response);
    }

    private static class CloudfrontHttpServletRequestWrapper extends HttpServletRequestWrapper {

        protected Map<String, List<String>> headers = new LinkedCaseInsensitiveMap<>();

        public CloudfrontHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (equalsIgnoreCase(headerName, CLOUDFRONT_PROTO_HEADER_NAME)) {
                    headers.put(STANDARD_PROTO_HEADER_NAME, list(request.getHeaders(CLOUDFRONT_PROTO_HEADER_NAME)));
                } else {
                    headers.put(headerName, list(request.getHeaders(headerName)));
                }
            }
        }

        @Override
        public String getHeader(String name) {
            return headers.getOrDefault(name, singletonList(null)).get(0);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            return enumeration(headers.getOrDefault(name, emptyList()));
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return enumeration(headers.keySet());
        }

    }

}
