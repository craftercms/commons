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
package org.craftercms.commons.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Holds the current HTTP servlet request and response.
 *
 * @author avasquez
 */
public class RequestContext {

    private static ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    public RequestContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }

    /**
     * Returns the context for the current thread.
     */
    public static RequestContext getCurrent() {
        return threadLocal.get();
    }

    /**
     * Sets the context for the current thread.
     */
    public static void setCurrent(RequestContext current) {
        threadLocal.set(current);
    }

    /**
     * Removes the context from the current thread.
     */
    public static void clear() {
        threadLocal.remove();
    }

    /**
     * Returns the current request.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the current request.
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Returns the current response.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Sets the current response.
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Returns the current servlet context.
     */
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * Sets the current servlet context.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
