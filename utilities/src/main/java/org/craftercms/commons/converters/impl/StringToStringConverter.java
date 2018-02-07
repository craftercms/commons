package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Simple dummy converter that just returns a String value.
 *
 * @author avasquez
 */
public class StringToStringConverter implements Converter<String, String> {

    @Override
    public Class<?> getSourceClass() {
        return String.class;
    }

    @Override
    public Class<?> getTargetClass() {
        return String.class;
    }

    @Override
    public String convert(String source) {
        return source;
    }

}
