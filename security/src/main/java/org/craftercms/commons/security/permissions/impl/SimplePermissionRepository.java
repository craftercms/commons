package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link org.craftercms.commons.security.permissions.PermissionRepository} that uses a
 * pre-existing list. Insert, update and delete are not supported.
 *
 * @author avasquez
 */
public class SimplePermissionRepository implements PermissionRepository {

    protected List<Permission> permissions;

    @Required
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Iterable<Permission> findByResourceUri(String resourceUri) {
        List<Permission> matchingPermissions = new ArrayList<Permission>();

        for (Permission permission : permissions) {
            if (permission.getResourceUri().equals(resourceUri)) {
                matchingPermissions.add(permission);
            }
        }

        return matchingPermissions;
    }

    @Override
    public void insert(Permission permission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Permission permission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String resourceUri) {
        throw new UnsupportedOperationException();
    }

}
