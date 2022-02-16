/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.converters.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.craftercms.commons.converters.Converter;
import org.craftercms.commons.exceptions.DateParseException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.InitializingBean;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Converts String to Date, with a custom pattern and time zone.
 *
 * @author avasquez
 */
public class StringToDateConverter implements Converter<String, Date>, InitializingBean {

    protected String datePattern;
    protected TimeZone timeZone;
    protected FastDateFormat dateFormat;

    @Required
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }

    public void afterPropertiesSet() {
        dateFormat = FastDateFormat.getInstance(datePattern, timeZone);
    }

    @Override
    public Class<?> getSourceClass() {
        return String.class;
    }

    @Override
    public Class<?> getTargetClass() {
        return Date.class;
    }

    @Override
    public Date convert(String source) {
        if (isEmpty(source)) {
            return null;
        }
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new DateParseException(source, datePattern, e);
        }
    }

}

