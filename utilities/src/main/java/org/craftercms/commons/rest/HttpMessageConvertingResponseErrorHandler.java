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

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

/**
 * {@link org.springframework.web.client.ResponseErrorHandler} that converts the body of a response with error status
 * code using {@link org.springframework.http.converter.HttpMessageConverter}s. Subclasses should implement
 * {@link #handleErrorInternal(Object, org.springframework.http.client.ClientHttpResponse)} to handle the extracted
 * error object.
 *
 * @author avasquez
 */
public abstract class HttpMessageConvertingResponseErrorHandler<T> extends DefaultResponseErrorHandler {

    protected List<HttpMessageConverter<?>> messageConverters;

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return messageConverters;
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpMessageConverterExtractor<? extends T> responseExtractor =
                new HttpMessageConverterExtractor<>(getErrorResponseType(), messageConverters);

        try {
            T errorObject = responseExtractor.extractData(response);

            handleErrorInternal(errorObject, response);
        } catch (RestClientException e) {
            // No message converter to extract the response, so throw it as the default response handler would.
            super.handleError(response);
        }
    }

    protected abstract Class<? extends T> getErrorResponseType();

    protected abstract void handleErrorInternal(T errorObject, ClientHttpResponse response) throws IOException;

}
