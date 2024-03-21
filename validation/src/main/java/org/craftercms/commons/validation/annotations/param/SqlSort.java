/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.validation.annotations.param;

import org.craftercms.commons.validation.validators.impl.SortFieldValidator;
import org.craftercms.commons.validation.validators.impl.SqlSortValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Elements annotated with {@link SqlSort} will be validated by {@link SqlSortValidator}.
 * Accepted values are comma-separated lists of "column order(optional)".
 * e.g.:
 * <ul>
 * <li>date asc</li>
 * <li>date asc, id desc</li>
 * <li> date, id asc</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SqlSortValidator.class, SortFieldValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface SqlSort {
    String message() default "{validation.error.sql.sort.failed}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String columns();
}
