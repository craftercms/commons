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

import org.craftercms.commons.cal10n.Cal10nUtils;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.exception.SubjectNotFoundException;
import org.craftercms.commons.security.logging.PermissionLogMessage;
import org.craftercms.commons.security.permissions.*;
import org.slf4j.cal10n.LocLogger;

/**
 * Default implementation of {@link org.craftercms.commons.security.permissions.PermissionEvaluator}
 *
 * @author avasquez
 */
public class PermissionEvaluatorImpl<S, O> implements PermissionEvaluator<S, O> {

    private static final LocLogger logger = Cal10nUtils.DEFAULT_LOC_LOGGER_FACTORY.getLocLogger(
            PermissionEvaluatorImpl.class);

    protected SubjectResolver<S> subjectResolver;
    protected PermissionResolver<S, O> permissionResolver;

    public void setSubjectResolver(SubjectResolver<S> subjectResolver) {
        this.subjectResolver = subjectResolver;
    }

    public void setPermissionResolver(PermissionResolver<S, O> permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    @Override
    public boolean isAllowed(O object, String action) throws PermissionException {
        S subject = subjectResolver.getCurrentSubject();
        if (subject == null) {
            throw new SubjectNotFoundException();
        }

        return isAllowed(subject, object, action);
    }

    @Override
    public boolean isAllowed(S subject, O object, String action) throws PermissionException {
        Permission permission;

        if (object == null) {
            logger.debug(PermissionLogMessage.RESOLVING_GLOBAL_PERMISSION, subject);

            permission = permissionResolver.getGlobalPermission(subject);
        } else {
            logger.debug(PermissionLogMessage.RESOLVING_PERMISSION, subject, object);

            permission = permissionResolver.getPermission(subject, object);
        }

        if (permission != null) {
            logger.debug(PermissionLogMessage.EVALUATING_PERMISSION, action, subject, object, permission);

            return permission.isAllowed(action);
        } else {
            return false;
        }
    }

}
