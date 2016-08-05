package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToDoubleConverter implements Converter<String, Double> {

    @Override
    public Double convert(String source) {
        return Double.valueOf(source);
    }

}
