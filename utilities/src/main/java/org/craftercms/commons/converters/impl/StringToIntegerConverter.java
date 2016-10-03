package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }

}
