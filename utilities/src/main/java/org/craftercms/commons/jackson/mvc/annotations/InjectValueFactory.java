package org.craftercms.commons.jackson.mvc.annotations;

/**
 *
 */
public interface InjectValueFactory {
    <T> T getObjectFor(final Class<T> declaringClass, final Object basePropertyValue, final String originalProperty,
                       final Object object);
}
