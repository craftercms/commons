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
package org.craftercms.commons.config.profiles;

import org.craftercms.commons.config.ConfigurationException;

/**
 * Generic interfaces for classes that provide a way to load specific configuration profiles.
 *
 * @author avasquez
 */
public interface ConfigurationProfileLoader<T extends ConfigurationProfile> {

    /**
     * Loads the {@link ConfigurationProfile} that corresponds to the given ID.
     *
     * @param id the profile ID
     * @return the configuration profile
     *
     * @throws ConfigurationException if an error occurs while trying to load the profile
     */
    T loadProfile(String id) throws ConfigurationException;

}
