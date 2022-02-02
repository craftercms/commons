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
package org.craftercms.commons.git.utils;

import org.craftercms.commons.git.auth.GitAuthenticationConfigurator;

/**
 * Builder class for {@link GitAuthenticationConfigurator} objects
 *
 * @author joseross
 * @since 4.0.0
 */
public interface AuthConfiguratorBuilder {

    /**
     * Creates an instance based on the current configuration
     * @return a {@link GitAuthenticationConfigurator} object
     */
    GitAuthenticationConfigurator build();

}
