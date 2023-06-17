/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.monitoring.rest;

import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.exceptions.InvalidManagementTokenException;
import org.craftercms.commons.monitoring.MemoryInfo;
import org.craftercms.commons.monitoring.StatusInfo;
import org.craftercms.commons.monitoring.VersionInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Base controller for all monitoring related APIs
 * @author joseross
 */
@RestController
public abstract class MonitoringRestControllerBase {

    public final static String ROOT_URL = "/monitoring";
    public final static String MEMORY_URL = "/memory";
    public final static String STATUS_URL = "/status";
    public final static String VERSION_URL = "/version";

    private final String configuredToken;

    public MonitoringRestControllerBase(final String configuredToken) {
        this.configuredToken = configuredToken;
    }

    @GetMapping(ROOT_URL + MEMORY_URL)
    public MemoryInfo getCurrentMemory(@RequestParam(name = "token", required = true) String token)
            throws InvalidManagementTokenException {
        validateToken(token);
        return MemoryInfo.getCurrentMemory();
    }

    @GetMapping(ROOT_URL + STATUS_URL)
    public ResponseEntity getCurrentStatus(@RequestParam(name = "site", required = false) String site,
                                           @RequestParam(name = "token") String token)
            throws InvalidManagementTokenException {
        validateToken(token);
        return ResponseEntity.ok().body(StatusInfo.getCurrentStatus());
    }

    @GetMapping(ROOT_URL + VERSION_URL)
    public VersionInfo getCurrentVersion(@RequestParam(name = "token", required = true) String token)
            throws InvalidManagementTokenException, IOException {
        validateToken(token);
        return VersionInfo.getVersion(this.getClass());
    }

    protected final void validateToken(final String requestToken) throws InvalidManagementTokenException {
        if (!StringUtils.equals(requestToken, configuredToken)) {
            throw new InvalidManagementTokenException("Management authorization failed, invalid token.");
        }
    }

}
