package org.craftercms.commons.security.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in a method parameter to indicate an ID (of several) of the resource to be protected. Multiple annotations of
 * this type can be used on the same method, but they can't be used in conjunction with {@link ProtectedResource}.
 *
 * <p>
 * This annotation is preferred in cases where permission evaluation is being done on a resource that has multiple IDs.
 * For example, to secure a file in a site you probably need the name of the site and then the path of the file in the
 * site.
 * </p>
 *
 * <p>
 * When using this annotation, an map with key = ID name => value = ID value is passed to the
 * {@link org.craftercms.commons.security.permissions.PermissionEvaluator} instead of a single object.
 * </p>
 *
 * @author avasquez
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedResourceId {

    /**
     * The name of the ID, e.g. site, path, etc.
     */
    String value();

}
