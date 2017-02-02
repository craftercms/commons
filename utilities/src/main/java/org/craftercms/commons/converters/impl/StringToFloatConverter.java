package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Converts String to Float.
 *
 * @author avasquez
 */
public class StringToFloatConverter implements Converter<String, Float> {

    @Override
    public Float convert(String source) {
        return Float.valueOf(source);
    }

}
