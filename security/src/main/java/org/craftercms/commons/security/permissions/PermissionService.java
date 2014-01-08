package org.craftercms.commons.security.permissions;

import java.util.Map;

/**
 * Service used to check permissions for subjects.
 *
 * @author avasquez
 */
public interface PermissionService {

    /**
     * Checks if the given subject is allowed to perform the specified action.
     *
     * @param subject       the subject
     * @param resourceUri   the resource URI of the permission
     * @param action        the action the subject wants to perform
     * @param variables     the variable values. Placeholders in the resource URI and the subject condition, with the
     *                      format {value}, are replaced using this values. Eg.: ugcs:/{ugcId}, isOwner({ugcId}).
     * @return
     */
    boolean allow(Object subject, String resourceUri, String action, Map<String, String> variables);

}
