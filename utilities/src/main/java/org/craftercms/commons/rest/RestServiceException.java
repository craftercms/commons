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

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

/**
 * Exception thrown when an error is received from a REST service. The {@code errorDetails} holds the details
 * of the error, and can be anything the service wants to return.
 *
 * @author avasquez
 */
public class RestServiceException extends RestClientException {

    protected HttpStatus responseStatus;
    protected Object errorDetails;

    public RestServiceException(HttpStatus responseStatus, Object errorDetails) {
        super("responseStatus = " + responseStatus.value() + ", errorDetails = " + errorDetails);

        this.responseStatus = responseStatus;
        this.errorDetails = errorDetails;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public Object getErrorDetails() {
        return errorDetails;
    }

}
