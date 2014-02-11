/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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
package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.mongo.MongoDataException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionSource;
import org.craftercms.commons.security.repositories.JongoPermissionRepository;

/**
 * {@link org.craftercms.commons.security.permissions.PermissionSource} implementation that uses the
 * {@link org.craftercms.commons.security.repositories.JongoPermissionRepository}.
 */
public class JongoPermissionSource implements PermissionSource {

    private JongoPermissionRepository permissionRepository;

    public void setPermissionRepository(JongoPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Iterable<Permission> getPermissions(String resourceUri) throws PermissionException {
        try {
            return permissionRepository.findByResourceUri(resourceUri);
        } catch (MongoDataException e) {
            throw new PermissionException("Find by resource URI " + resourceUri + " failed");
        }
    }

}
