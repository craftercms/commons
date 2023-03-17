package org.craftercms.commons.validation.annotations.param;

import org.craftercms.commons.validation.validators.impl.SqlSortValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
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
@Constraint(validatedBy = {SqlSortValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface SqlSort {
    String message() default "{validation.error.sql.sort.failed}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String columns();
}
