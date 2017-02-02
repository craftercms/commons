package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Converts String to Long.
 *
 * @author avasquez
 */
public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Long convert(String source) {
        return Long.valueOf(source);
    }

}
