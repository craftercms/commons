package org.craftercms.commons.security.permissions;

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
     * @return an iterable for the matching permissions, or null if not found
     */
    Iterable<Permission> findByResourceUri(String resourceUri);

    /**
     * Inserts a new permission in the repository (optional operation).
     *
     * @param permission    the permission to insert
     */
    void insert(Permission permission);

    /**
     * Updates a permission in the repository (optional operation).
     *
     * @param permission    the permission to update
     */
    void update(Permission permission);

    /**
     * Deletes the permission for the given resource URI.
     *
     * @param resourceUri   the resource URI of the permission to delete.
     */
    void delete(String resourceUri);

}
