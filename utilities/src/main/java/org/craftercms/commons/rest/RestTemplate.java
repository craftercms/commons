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
package org.craftercms.commons.rest;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * Simple extension of {@link org.springframework.web.client.RestTemplate} that adds the following custom
 * functionality:
 * <p>
 * <ul>
 * <li>Forces the use of {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory},
 * to avoid issues with 40x responses.</li>
 * <li>{@link org.craftercms.commons.rest.HttpMessageConvertingResponseErrorHandler} is used by default.</li>
 * </ul>
 * </p>
 *
 * @author avasquez
 */
public class RestTemplate extends org.springframework.web.client.RestTemplate {

    protected Class<?> errorResponseType;

    public RestTemplate() {
        super(new HttpComponentsClientHttpRequestFactory());

        setErrorHandler(new HttpMessageConvertingResponseErrorHandler());
    }

    public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        super(new HttpComponentsClientHttpRequestFactory());

        setMessageConverters(messageConverters);
        setErrorHandler(new HttpMessageConvertingResponseErrorHandler());
    }

    @Required
    public void setErrorResponseType(Class<?> errorResponseType) {
        this.errorResponseType = errorResponseType;
    }

    @PostConstruct
    public void init() {
        if (getErrorHandler() instanceof HttpMessageConvertingResponseErrorHandler) {
            HttpMessageConvertingResponseErrorHandler errorHandler = (HttpMessageConvertingResponseErrorHandler)
                getErrorHandler();

            if (errorHandler.getMessageConverters() == null) {
                errorHandler.setMessageConverters(getMessageConverters());
            }
            if (errorHandler.getResponseType() == null) {
                errorHandler.setResponseType(errorResponseType);
            }
        }
    }

}
