package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToShortConverter implements Converter<String, Short> {

    @Override
    public Short convert(String source) {
        return Short.valueOf(source);
    }

}
