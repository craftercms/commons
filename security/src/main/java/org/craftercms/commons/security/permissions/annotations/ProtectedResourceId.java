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
