package org.craftercms.commons.security.permissions.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionRepository;
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
    public static final String URI_SEPARATOR =    "/";

    protected PermissionRepository permissionRepository;

    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean allow(Object subject, String resourceUri, String action, Map<String, String> variables) {
        resourceUri = StringUtils.stripEnd(resourceUri, URI_SEPARATOR);

        Iterable<Permission> permissions = permissionRepository.findByResourceUri(resourceUri);

        if (permissions != null) {
            ExpressionParser expressionParser = new SpelExpressionParser();

            for (Permission permission : permissions) {
                Boolean permitted = checkPermission(permission, subject, action, expressionParser, variables);
                if (permitted != null) {
                    return permitted;
                } else {
                    String parentResourceUri = getParentResourceUri(resourceUri);
                    if (StringUtils.isNotEmpty(parentResourceUri)) {
                        return allow(subject, parentResourceUri, action, variables);
                    }
                }
            }
        }

        return false;
    }

    protected Boolean checkPermission(Permission permission, Object subject, String action,
                                      ExpressionParser expressionParser, Map<String, String> variables) {
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
                                              Map<String, String> variables) {
        if (condition.equals(ANY_WILDCARD)) {
            return true;
        }

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            condition = condition.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        Expression parsedCondition = expressionParser.parseExpression(condition);

        Object result = parsedCondition.getValue(subject);
        if (!(result instanceof Boolean)) {
            throw new IllegalArgumentException("Expression " + condition + " should return a boolean value");
        }

        return (Boolean) result;
    }

    protected String getParentResourceUri(String resourceUri) {
        int lastIndexOfSep = resourceUri.lastIndexOf(URI_SEPARATOR);
        if (lastIndexOfSep > 0) {
            return resourceUri.substring(0, lastIndexOfSep);
        } else {
            return null;
        }
    }

}
