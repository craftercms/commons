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
package org.craftercms.commons.security.permissions.impl;

import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.security.exception.PermissionExceptionAbstract;
import org.craftercms.commons.security.exception.SubjectNotFoundExceptionAbstract;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionEvaluator;
import org.craftercms.commons.security.permissions.PermissionResolver;
import org.craftercms.commons.security.permissions.SubjectResolver;

/**
 * Default implementation of {@link org.craftercms.commons.security.permissions.PermissionEvaluator}
 *
 * @author avasquez
 */
public class PermissionEvaluatorImpl<S, O> implements PermissionEvaluator<S, O> {

    private static final I10nLogger logger = new I10nLogger(PermissionEvaluatorImpl.class,
        "crafter.security.messages.logging");

    private static final String LOG_KEY_RESOLVING_GLOBAL_PERM = "security.permission.resolvingGlobalPermission";
    private static final String LOG_KEY_RESOLVING_PERM = "security.permission.resolvingPermission";
    private static final String LOG_KEY_EVALUATING_PERM = "security.permission.evaluatingPermission";

    protected SubjectResolver<S> subjectResolver;
    protected PermissionResolver<S, O> permissionResolver;

    public void setSubjectResolver(SubjectResolver<S> subjectResolver) {
        this.subjectResolver = subjectResolver;
    }

    public void setPermissionResolver(PermissionResolver<S, O> permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    @Override
    public boolean isAllowed(O object, String action) throws PermissionExceptionAbstract {
        S subject = subjectResolver.getCurrentSubject();
        if (subject == null) {
            throw new SubjectNotFoundExceptionAbstract();
        }

        return isAllowed(subject, object, action);
    }

    @Override
    public boolean isAllowed(S subject, O object, String action) throws PermissionExceptionAbstract {
        Permission permission;

        if (object == null) {
            logger.debug(LOG_KEY_RESOLVING_GLOBAL_PERM, subject);

            permission = permissionResolver.getGlobalPermission(subject);
        } else {
            logger.debug(LOG_KEY_RESOLVING_PERM, subject, object);

            permission = permissionResolver.getPermission(subject, object);
        }

        if (permission != null) {
            logger.debug(LOG_KEY_EVALUATING_PERM, action, subject, object, permission);

            return permission.isAllowed(action);
        } else {
            return false;
        }
    }

}
