package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Converts String to Double.
 *
 * @author avasquez
 */
public class StringToDoubleConverter implements Converter<String, Double> {

    @Override
    public Double convert(String source) {
        return Double.valueOf(source);
    }

}
