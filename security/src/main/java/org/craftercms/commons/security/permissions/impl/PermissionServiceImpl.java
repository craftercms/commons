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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.craftercms.commons.security.exception.InvalidSubjectConditionException;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.ParentResolver;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionResolver;
import org.craftercms.commons.security.permissions.PermissionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link org.craftercms.commons.security.permissions.PermissionService}
 *
 * @author avasquez
 */
public class PermissionServiceImpl implements PermissionService {

    public static final String ANY_WILDCARD =   "*";

    protected PermissionResolver permissionResolver;
    protected ParentResolver parentResolver;

    public void setPermissionResolver(PermissionResolver permissionResolver) {
        this.permissionResolver = permissionResolver;
    }

    public void setParentResolver(ParentResolver parentResolver) {
        this.parentResolver = parentResolver;
    }

    @Override
    public boolean allow(Object subject, Object object, String action, Map<String, String> variables,
                         boolean checkAncestorPermissions) throws PermissionException {
        Iterable<Permission> permissions = permissionResolver.getPermissions(object);

        if (permissions != null) {
            ExpressionParser expressionParser = new SpelExpressionParser();

            for (Permission permission : permissions) {
                Boolean permitted = checkPermission(permission, subject, action, expressionParser, variables);
                if (permitted != null) {
                    return permitted;
                } else if (checkAncestorPermissions) {
                    Object parent = parentResolver.getParent(object);
                    if (parent != null) {
                        allow(subject, object, action, variables, checkAncestorPermissions);
                    }
                }
            }
        }

        return false;
    }

    protected Boolean checkPermission(Permission permission, Object subject, String action,
                                      ExpressionParser expressionParser, Map<String, String> variables)
            throws InvalidSubjectConditionException {
        if (subjectMatchesCondition(subject, permission.getSubjectCondition(), expressionParser, variables)) {
            List<String> allowedActions = permission.getAllowedActions();
            List<String> deniedActions = permission.getDeniedActions();

            if (CollectionUtils.isNotEmpty(allowedActions) &&
                (allowedActions.contains(ANY_WILDCARD) || allowedActions.contains(action))) {
                return true;
            }
            if (CollectionUtils.isNotEmpty(deniedActions) &&
                (deniedActions.contains(ANY_WILDCARD) || deniedActions.contains(action))) {
                return false;
            }
        }

        return null;
    }

    protected boolean subjectMatchesCondition(Object subject, String condition, ExpressionParser expressionParser,
                                              Map<String, String> variables) throws InvalidSubjectConditionException {
        if (condition.equals(ANY_WILDCARD)) {
            return true;
        }

        if (MapUtils.isNotEmpty(variables)) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                condition = condition.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        Expression parsedCondition = expressionParser.parseExpression(condition);

        Object result = parsedCondition.getValue(subject);
        if (!(result instanceof Boolean)) {
            throw new InvalidSubjectConditionException("Expression " + condition + " should return a boolean value");
        }

        return (Boolean) result;
    }

}
