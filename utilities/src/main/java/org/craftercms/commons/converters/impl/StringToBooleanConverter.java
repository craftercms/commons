package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Converts String to Boolean.
 *
 * @author avasquez
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convert(String source) {
        return Boolean.valueOf(source);
    }

}
