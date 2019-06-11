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

package org.craftercms.commons.entitlements.usage;

import java.util.List;
import java.util.stream.Collectors;

import org.craftercms.commons.entitlements.model.Entitlement;
import org.craftercms.commons.entitlements.model.EntitlementType;
import org.craftercms.commons.entitlements.model.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Defines the operations to support entitlement usage data in a module.
 *
 * Each module must provide an implementation of this interface.
 *
 * @author joseross
 */
public interface EntitlementUsageProvider {

    /**
     * Identifies the current module running.
     * @return value from {@link Module}
     */
    Module getModule();

    List<EntitlementType> getSupportedEntitlements();

    /**
     * Returns the current usage for a given entitlement type in the current module
     * @param type the entitlement to check
     * @return the current usage
     */
    default int getEntitlementUsage(final EntitlementType type) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("Getting current value for entitlement {}", type);
        int value = Integer.MAX_VALUE;
        StopWatch watch = new StopWatch(getClass().getSimpleName());
        if (logger.isDebugEnabled()) {
            watch.start(type.toString());
        }
        try {
            value = doGetEntitlementUsage(type);
        } catch (Exception e) {
            logger.error("Error getting current value for entitlement {}", type);
        } finally {
            if (logger.isDebugEnabled()) {
                watch.stop();
                logger.debug("{}", watch);
            }
        }
        return value;
    }

    /**
     * Performs the module specific operations to get the current value of the given entitlement
     * @param type the entitlement to check
     * @return the current usage
     */
    int doGetEntitlementUsage(final EntitlementType type) throws Exception;

    /**
     * Provides a list holding the current values for all entitlements supported by the current module.
     * @return the entitlement list
     */
    default List<Entitlement> getCurrentUsage() {
        return getSupportedEntitlements().stream().map(type -> {
            Entitlement entitlement = new Entitlement();
            entitlement.setType(type);
            entitlement.setValue(getEntitlementUsage(type));
            return entitlement;
        }).collect(Collectors.toList());
    }

}
