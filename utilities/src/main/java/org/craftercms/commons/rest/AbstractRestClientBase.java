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

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.RestTemplate;

/**
 * Base class for all Crafter REST clients.
 *
 * @author avasquez
 */
public abstract class AbstractRestClientBase {

    protected String baseUrl;
    protected String extension;
    protected RestTemplate restTemplate;

    @Required
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Required
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl + (extension != null? extension: "");
    }

}
