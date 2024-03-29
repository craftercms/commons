/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
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
 * Annotations that applications can use to indicate that a certain method or all methods of a class require
 * permission checking.
 *
 * @author avasquez
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

    /**
     * The permission type, which indicates the {@link org.craftercms.commons.security.permissions.PermissionEvaluator}
     * to use for permission evaluation.
     */
    Class<?> type();

    /**
     * The action the current subject is trying to execute.
     */
    String action();

    /**
     * Indicates if the presence of a valid management token should grant access to the protected resource/action
     */
    boolean acceptManagementToken() default false;

}
