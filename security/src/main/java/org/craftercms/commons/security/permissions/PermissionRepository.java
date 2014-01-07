package org.craftercms.commons.security.permissions;

import java.util.List;

/**
 * Repository where {@link org.craftercms.commons.security.permissions.Permission}s can be queried, updated or deleted.
 *
 * @author avasquez
 */
public interface PermissionRepository {

    /**
     * Searches for permissions with the given resource URI.
     *
     * @param resourceUri   the resource URI of the permissions
     *
     * @return the list of matching permissions, or null or empty if not found
     */
    List<Permission> findByResourceUri(String resourceUri);

    /**
     * Saves a new permission to the repository.
     *
     * @param permission    the permission to save
     */
    void save(Permission permission);

    /**
     * Update a permission in the repository
     *
     * @param permission    the permission to update
     */
    void update(Permission permission);

}
