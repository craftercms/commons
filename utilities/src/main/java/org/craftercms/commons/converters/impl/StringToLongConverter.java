package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Long convert(String source) {
        return Long.valueOf(source);
    }

}
