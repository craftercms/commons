package org.craftercms.commons.validation.annotations.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateStringParam {

    String name() default "";

    boolean notNull() default false;

    boolean notEmpty() default false;

    boolean notBlank() default false;

    int minLength() default 0;

    int maxLength() default Integer.MAX_VALUE;

    String[] whitelistedPatterns() default {};

    String[] blacklistedPatterns() default {};

    boolean matchFullInput() default true;

}
