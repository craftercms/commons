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

package org.craftercms.commons.entitlements.validator;

import java.io.File;
import java.util.jar.JarFile;

import org.craftercms.commons.entitlements.exception.EntitlementException;
import org.craftercms.commons.entitlements.model.EntitlementType;
import org.craftercms.commons.monitoring.VersionInfo;

/**
 * Defines the operations to perform entitlement validations.
 *
 * @author joseross
 */
public interface EntitlementValidator {

    /**
     * Checks that an entitlement is below the value indicated in the configuration file.
     * @param entitlementType entitlement to be validated
     * @param newAmount amount of items to be created
     * @throws EntitlementException if the validation fails
     */
    default void validateEntitlement(EntitlementType entitlementType, int newAmount)
        throws EntitlementException {
        // Do nothing by default
    }

    /**
     * Provides the id of the current validator.
     * @return id of the validator
     */
    default long getId() {
        return -1;
    }

    /**
     * Provides the client id of the current validator.
     * @return if of the client
     */
    default long getClientId() {
        return -1;
    }

    /**
     * Provides the version of the current validator.
     * @return the version
     */
    default String getVersion() {
        return null;
    }

    /**
     * Provides a general description of the current validator.
     * @return validator description
     */
    String getDescription();

    /**
     * Provides the version of the containing JAR file.
     * @return the version
     */
    default String getPackageVersion() {
        try {
            VersionInfo versionInfo = VersionInfo.getVersion(
                new JarFile(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))
                    .getManifest());
            return versionInfo.getPackageVersion();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Provides the build of the containing JAR file.
     * @return the build
     */
    default String getPackageBuild() {
        try {
            VersionInfo versionInfo = VersionInfo.getVersion(
                new JarFile(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))
                    .getManifest());
            return versionInfo.getPackageBuild();
        } catch (Exception e) {
            return null;
        }
    }

}
