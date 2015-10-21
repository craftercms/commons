package org.craftercms.commons.jackson.mvc;

/**
 * Defines how the SecureProperty Should be handel.
 */
public interface SecurePropertyHandler {
    /**
     * Checks if the property is allowed for the given role.
     * @param roles Roles to check if Property is allowed.
     * @param propertyName Property name to check. (this will be a full class name + actual property name)
     * @return True if the property must be suppress from the output, False otherwise.
     */
    boolean suppressProperty(final Object propertyName, final String[] roles);
}
