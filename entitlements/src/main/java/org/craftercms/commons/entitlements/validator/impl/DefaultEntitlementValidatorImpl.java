/*
 * Copyright (C) 2007-2019 Crafter Software Corporation.
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

package org.craftercms.commons.entitlements.validator.impl;

import java.util.Base64;
import java.util.Collections;
import javax.annotation.PostConstruct;

import org.apache.commons.text.StringSubstitutor;
import org.craftercms.commons.entitlements.validator.EntitlementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default Implementation of {@link EntitlementValidator}.
 * No configuration required and all requested validations will be successful.
 *
 * @author joseross
 */
public class DefaultEntitlementValidatorImpl implements EntitlementValidator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEntitlementValidatorImpl.class);

    private static final String DESCRIPTION = "UG93ZXJlZCBieSBDcmFmdGVyIENNUyB2JHt2ZXJzaW9ufS4gQ2hlY2sgaXQgb3V0IDxhIG"
        + "hyZWY9Imh0dHBzOi8vY3JhZnRlcmNtcy5vcmciPmhlcmU8L2E+LiBSZXBvcnQgYSA8YSBocmVmPSJodHRwczovL2dpdGh1Yi5jb20vY3Jh"
        + "ZnRlcmNtcy9jcmFmdGVyY21zL2lzc3VlcyI+YnVnPC9hPi4gPGEgaHJlZj0iaHR0cHM6Ly9jcmFmdGVyY21zLm9yZy9ibG9nIj5DcmFmdG"
        + "VyIE5ld3M8L2E+Lg==";

    @PostConstruct
    protected void init() {
        logger.info("Using Crafter CMS Community Edition");
    }

    @Override
    public String getDescription() {
        String decoded = new String(Base64.getDecoder().decode(DESCRIPTION));
        StringSubstitutor stringSubstitutor =
            new StringSubstitutor(Collections.singletonMap("version", getPackageVersion()));
        return stringSubstitutor.replace(decoded);
    }

}
