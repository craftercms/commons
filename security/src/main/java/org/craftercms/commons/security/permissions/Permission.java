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
package org.craftercms.commons.security.permissions;

import java.util.List;

/**
 * Represents a permission that allows or denies a subject (a user, an application, etc.) the execution of an action
 * or set of actions on an object.
 *
 * @author avasquez
 */
public class Permission {

    protected String subjectCondition;
    protected List<String> allowedActions;
    protected List<String> deniedActions;

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
