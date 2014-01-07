package org.craftercms.commons.security.permissions;

import java.util.List;

/**
 * Represents a permission that allows or denies a subject (a user, an application, etc.) the execution of an action
 * or set of actions on a resource, identified by an URI.
 *
 * @author avasquez
 */
public class Permission {

    protected String resourceUri;
    protected String subjectCondition;
    protected List<String> allowedActions;
    protected List<String> deniedActions;

    /**
     * Returns the resource URI. Resource URIs have the following format:
     * {@code resourceType:/path/of/the/resource/in/the/hierarchy}, e.g. users:/avasquez or nodes:/sites/mysite.
     */
    public String getResourceUri() {
        return resourceUri;
    }

    /**
     * Sets the resource URI. Resource URIs have the following format:
     * {@code resourceType:/path/of/the/resource/in/the/hierarchy}, e.g. users:/avasquez or nodes:/sites/mysite.
     *
     * @param resourceUri   the resource URI
     */
    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    /**
     * Returns the subject condition, used to evaluate whether this permission should be applied to a particular
     * subject or not. The condition can be any Spring EL expression that returns a boolean.
     */
    public String getSubjectCondition() {
        return subjectCondition;
    }

    /**
     * Sets the subject condition, used to evaluate whether this permission should be applied to a particular
     * subject or not. The condition can be any Spring EL expression that returns a boolean.
     *
     * @param subjectCondition the subject expression
     */
    public void setSubjectCondition(String subjectCondition) {
        this.subjectCondition = subjectCondition;
    }

    /**
     * Returns the list of allowed actions for this permission.
     */
    public List<String> getAllowedActions() {
        return allowedActions;
    }

    /**
     * Sets the list of allowed actions for this permission.
     *
     * @param allowedActions the list of allowed actions
     */
    public void setAllowedActions(List<String> allowedActions) {
        this.allowedActions = allowedActions;
    }

    /**
     * Returns the list of denied actions for this permission.
     */
    public List<String> getDeniedActions() {
        return deniedActions;
    }

    /**
     * Sets the list of denied actions for this permission.
     *
     * @param deniedActions the list of denied actions
     */
    public void setDeniedActions(List<String> deniedActions) {
        this.deniedActions = deniedActions;
    }

}
