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
package org.craftercms.commons.security.repositories;

import org.craftercms.commons.mongo.JongoRepository;
import org.craftercms.commons.mongo.MongoDataException;
import org.craftercms.commons.security.permissions.Permission;

/**
 * Created by alfonsovasquez on 10/02/14.
 */
public class JongoPermissionRepository extends JongoRepository<Permission> {

    private static final String FIND_BY_RESOURCE_URI_QUERY =    "permissions.findByResourceId";
    private static final String REMOVE_BY_RESOURCE_URI_QUERY =  "permissions.removeByResourceId";

    public JongoPermissionRepository() throws MongoDataException {
        super();
    }

    public Iterable<Permission> findByResourceUri(String resourceUri) throws MongoDataException {
        return find(FIND_BY_RESOURCE_URI_QUERY, resourceUri);
    }

    public void removeByResourceId(String resourceUri) throws MongoDataException {
        remove(REMOVE_BY_RESOURCE_URI_QUERY, resourceUri);
    }

}
