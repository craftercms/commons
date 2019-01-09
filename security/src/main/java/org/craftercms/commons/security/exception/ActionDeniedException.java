/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.security.exception;

/**
 * Thrown when the execution of an action on an object has been denied to a subject.
 *
 * @author avasquez
 */
public class ActionDeniedException extends PermissionException {

    private static final String GLOBAL_ACTION_DENIED_KEY = "security.permission.globalActionDenied";
    private static final String ACTION_DENIED_KEY = "security.permission.actionDenied";

    public ActionDeniedException(String action) {
        super(GLOBAL_ACTION_DENIED_KEY, action);
    }

    public ActionDeniedException(String action, Object resource) {
        super(ACTION_DENIED_KEY, action, resource);
    }

}
