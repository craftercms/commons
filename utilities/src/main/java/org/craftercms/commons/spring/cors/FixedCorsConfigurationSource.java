/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.spring.cors;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

import java.beans.ConstructorProperties;

import static java.util.Arrays.asList;

/**
 * Implementation of {@link CorsConfigurationSource} that setup and returns a single instance
 *
 * @author joseross
 * @since 3.1.11
 */
public class FixedCorsConfigurationSource implements CorsConfigurationSource {

    protected CorsConfiguration config;

    @ConstructorProperties({"disableCORS", "allowOrigins", "allowMethods", "maxAge", "allowHeaders",
            "allowCredentials"})
    public FixedCorsConfigurationSource(boolean disableCORS, String allowOrigins, String allowMethods, String maxAge,
                                        String allowHeaders, boolean allowCredentials) {
        if (!disableCORS) {
            config = new CorsConfiguration();
            config.setAllowedOrigins(asList(allowOrigins.split(",")));
            config.setAllowedMethods(asList(allowMethods.split(",")));
            config.setAllowedHeaders(asList(allowHeaders.split(",")));
            config.setMaxAge(Long.parseLong(maxAge));
            config.setAllowCredentials(allowCredentials);
        }

    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return config;
    }

}
