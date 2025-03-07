/*
 * Copyright (C) 2007-2025 Crafter Software Corporation. All Rights Reserved.
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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static org.craftercms.commons.validation.annotations.param.ValidPassword.MAX_PASSWORD_LENGTH;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Size(max = MAX_PASSWORD_LENGTH)
public @interface ValidPassword {
	int MAX_PASSWORD_LENGTH = 128;

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
