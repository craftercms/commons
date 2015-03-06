package org.craftercms.commons.security.permissions;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a permission that is a collection of other permissions. If any of the permissions evaluates to false,
 * the final result will be false.
 *
 * @author avasquez
 */
public class CompositePermission implements Permission {

    protected Collection<Permission> permissions;

    public CompositePermission(Permission... permissions) {
        this(Arrays.asList(permissions));
    }

    public CompositePermission(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean isAllowed(String action) {
        for (Permission permission : permissions) {
            if (!permission.isAllowed(action)) {
                return false;
            }
        }

        return true;
    }

}
