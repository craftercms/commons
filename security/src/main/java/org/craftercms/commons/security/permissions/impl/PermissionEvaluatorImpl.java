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
package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.SubjectNotFoundException;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionEvaluator;
import org.craftercms.commons.security.permissions.PermissionResolver;
import org.craftercms.commons.security.permissions.SubjectResolver;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link org.craftercms.commons.security.permissions.PermissionEvaluator}
 *
 * @author avasquez
 */
public class PermissionEvaluatorImpl<S, R> implements PermissionEvaluator<S, R> {

    private static final I10nLogger logger = new I10nLogger(PermissionEvaluatorImpl.class, "crafter.security.messages.logging");

    private static final String LOG_KEY_RESOLVING_GLOBAL_PERM = "security.permission.resolvingGlobalPermission";
    private static final String LOG_KEY_RESOLVING_PERM = "security.permission.resolvingPermission";
    private static final String LOG_KEY_EVALUATING_PERM = "security.permission.evaluatingPermission";

    protected SubjectResolver<S> subjectResolver;
    protected PermissionResolver<S, R> permissionResolver;

    @Required
    public void setSubjectResolver(SubjectResolver<S> subjectResolver) {
        this.subjectResolver = subjectResolver;
    }

    @Required
    public void setPermissionResolver(PermissionResolver<S, R> permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    @Override
    public boolean isAllowed(R resource, String action) throws PermissionException {
        S subject = subjectResolver.getCurrentSubject();
        if (subject == null) {
            throw new SubjectNotFoundException();
        }

        return isAllowed(subject, resource, action);
    }

    @Override
    public boolean isAllowed(S subject, R resource, String action) throws PermissionException {
        Permission permission;

        if (resource == null) {
            logger.debug(LOG_KEY_RESOLVING_GLOBAL_PERM, subject);

            permission = permissionResolver.getGlobalPermission(subject);
        } else {
            logger.debug(LOG_KEY_RESOLVING_PERM, subject, resource);

            permission = permissionResolver.getPermission(subject, resource);
        }

        if (permission != null) {
            logger.debug(LOG_KEY_EVALUATING_PERM, action, subject, resource, permission);

            return permission.isAllowed(action);
        } else {
            return false;
        }
    }

}
