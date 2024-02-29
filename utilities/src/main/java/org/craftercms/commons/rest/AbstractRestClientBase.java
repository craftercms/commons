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
package org.craftercms.commons.rest;

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

    public AbstractRestClientBase(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    protected String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl + (extension != null? extension: "");
    }

}
