package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Converts String to Short.
 *
 * @author avasquez
 */
public class StringToShortConverter implements Converter<String, Short> {

    @Override
    public Short convert(String source) {
        return Short.valueOf(source);
    }

}
