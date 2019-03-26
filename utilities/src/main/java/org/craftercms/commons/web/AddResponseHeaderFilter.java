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
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to add a header to all responses
 * @author joseross
 * @since 3.1
 */
public class AddResponseHeaderFilter extends OncePerRequestFilter {

    /**
     * Indicates if the header should be added
     */
    protected boolean enabled = true;

    /**
     * The name of the header to add
     */
    protected String headerName;

    /**
     * The value of the header to add
     */
    protected String headerValue;

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Required
    public void setHeaderName(final String headerName) {
        this.headerName = headerName;
    }

    @Required
    public void setHeaderValue(final String headerValue) {
        this.headerValue = headerValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (enabled) {
            response.setHeader(headerName, headerValue);
        }
        filterChain.doFilter(request, response);
    }

}
