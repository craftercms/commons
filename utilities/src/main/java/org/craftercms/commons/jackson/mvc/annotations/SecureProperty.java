package org.craftercms.commons.jackson.mvc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Check if this property will be serialize depending of the result of
 * {@see org.craftercms.commons.jackson.mvc.SecurePropertyHandler#suppressProperty(String,String)}.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureProperty {
    /**
     * Role name to check if has access to the property.
     * @return Role Name.
     */
    public String[] role();
}
