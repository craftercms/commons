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

package org.craftercms.commons.entitlements.validator.impl;

import java.util.Base64;
import java.util.Collections;

import org.apache.commons.text.StringSubstitutor;
import org.craftercms.commons.entitlements.validator.EntitlementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;

/**
 * Default Implementation of {@link EntitlementValidator}.
 * No configuration required and all requested validations will be successful.
 *
 * @author joseross
 */
public class DefaultEntitlementValidatorImpl implements EntitlementValidator, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEntitlementValidatorImpl.class);

    private static final String DESCRIPTION = "UG93ZXJlZCBieSBDcmFmdGVyIENNUyB2JHt2ZXJzaW9ufS4gQ2hlY2sgaXQgb3V0IDxhIGhyZWY9Imh"
        + "0dHBzOi8vY3JhZnRlcmNtcy5vcmciIHRhcmdldD0iX2JsYW5rIj5oZXJlPC9hPi4gUmVwb3J0IGEgPGEgaH"
        + "JlZj0iaHR0cHM6Ly9naXRodWIuY29tL2NyYWZ0ZXJjbXMvY3JhZnRlcmNtcy9pc3N1ZXMiIHRhcmdldD0i"
        + "X2JsYW5rIj5idWc8L2E+LiA8YSBocmVmPSJodHRwczovL2NyYWZ0ZXJjbXMub3JnL2Jsb2ciIHRhcmdld"
        + "D0iX2JsYW5rIj5DcmFmdGVyIE5ld3M8L2E+Lg==";

    public void afterPropertiesSet() {
        logger.info("Using CrafterCMS Community Edition");
    }

    @Override
    public String getDescription() {
        String decoded = new String(Base64.getDecoder().decode(DESCRIPTION));
        StringSubstitutor stringSubstitutor =
            new StringSubstitutor(Collections.singletonMap("version", getPackageVersion()));
        return stringSubstitutor.replace(decoded);
    }

}
