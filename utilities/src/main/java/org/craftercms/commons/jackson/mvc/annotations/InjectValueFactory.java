package org.craftercms.commons.jackson.mvc.annotations;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 *
 */
public interface InjectValueFactory {
    <T> T getObjectFor(Class<T> declaringClass, Object basePropertyValue, Object object);
}
