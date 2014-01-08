package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionRepository;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;

/**
 * Implementation of {@link org.craftercms.commons.security.permissions.PermissionRepository} using Jongo.
 *
 * @author avasquez
 */
public class JongoPermissionRepository implements PermissionRepository {

    private static final String DEFAULT_COLLECTION_NAME = "permissions";

    private static final String FIND_BY_RESOURCE_URI_QUERY =    "{resourceUri: #}";
    private static final String REMOVE_QUERY =                  "{resourceUri: #}";

    protected String collectionName;
    protected Jongo jongo;

    protected MongoCollection permissions;

    public JongoPermissionRepository() {
        collectionName = DEFAULT_COLLECTION_NAME;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Required
    public void setJongo(Jongo jongo) {
        this.jongo = jongo;
    }

    @PostConstruct
    public void init() {
        permissions = jongo.getCollection(collectionName);
    }

    @Override
    public Iterable<Permission> findByResourceUri(String resourceUri) {
        return permissions.find(FIND_BY_RESOURCE_URI_QUERY, resourceUri).as(Permission.class);
    }

    @Override
    public void insert(Permission permission) {
        permissions.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        permissions.save(permission);
    }

    @Override
    public void delete(String resourceUri) {
        permissions.remove(REMOVE_QUERY, resourceUri);
    }

}
