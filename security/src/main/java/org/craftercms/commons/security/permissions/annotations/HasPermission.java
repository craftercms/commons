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
package org.craftercms.commons.security.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations that applications can use to indicate that a certain method requires permission checking.
 *
 * @author avasquez
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

    /**
     * The permission type, which indicates the {@link org.craftercms.commons.security.permissions.PermissionService}
     * to use for permission evaluation.
     */
    String type() default "*";

    /**
     * The ID of the secured object. Used when no parameter is annotated with
     * {@link org.craftercms.commons.security.permissions.annotations.SecuredObject}.
     */
    String securedObject() default "*";

    /**
     * The action the current subject is trying to execute.
     */
    String action() default "*";

}
