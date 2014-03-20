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

import org.craftercms.commons.i10n.I10nLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Binds a new request context to the current thread before the chain is called, and then removes it after the chain
 * is called.
 *
 * @author avasquez
 */
public class RequestContextBindingFilter implements Filter {

    private static final I10nLogger logger = new I10nLogger(RequestContextBindingFilter.class,
            "crafter.commons.messages.logging");

    private static final String LOG_KEY_BINGING_CONTEXT = "http.requestContext.bindingContext";
    private static final String LOG_KEY_UNBINDING_CONTEXT = "http.requestContext.unbindingContext";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    /**
     * Binds a new {@link RequestContext} to the current thread, and after the the filter chain has finished
     * executing, removes it from the current thread.
     *
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
            IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        RequestContext context = createRequestContext(req, resp);
        String threadName = Thread.currentThread().getName();

        logger.debug(LOG_KEY_BINGING_CONTEXT, req.getRequestURI(), threadName);

        RequestContext.setCurrent(context);

        try {
            chain.doFilter(request, response);
        } finally {
            logger.debug(LOG_KEY_UNBINDING_CONTEXT, req.getRequestURI(), threadName);

            RequestContext.clear();
        }
    }

    /**
     * Returns a new {@link RequestContext}, using the specified {@link HttpServletRequest} and {@link
     * HttpServletResponse}.
     */
    protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response) {
        return new RequestContext(request, response);
    }

}
