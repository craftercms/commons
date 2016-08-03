package org.craftercms.commons.converters.impl;

import org.craftercms.commons.converters.Converter;

/**
 * Created by alfonsovasquez on 1/8/16.
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convert(String source) {
        return Boolean.valueOf(source);
    }

}
