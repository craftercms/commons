package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToFloatConverter implements Converter<String, Float> {

    @Override
    public Float convert(String source) {
        return Float.valueOf(source);
    }

}
