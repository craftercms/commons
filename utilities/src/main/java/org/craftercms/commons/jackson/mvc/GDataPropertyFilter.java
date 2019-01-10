/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.jackson.mvc;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.jackson.mvc.annotations.InjectValue;
import org.craftercms.commons.jackson.mvc.annotations.InjectValueFactory;
import org.craftercms.commons.properties.OverrideProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A Gdata like property filter (aka selector) gets the information from .
 */
public class GDataPropertyFilter extends AbstractCrafterPropertyFilter {

    public static final String SELECTOR_ALIAS_PREFIX = ":";
    protected String selectorParameterName;
    protected OverrideProperties alias;
    protected Map<String, String> superClassCache;
    protected Map<String, List<String>> aliasParsedCache;
    protected Pattern p = Pattern.compile("\\w+\\(\\w+(\\,\\w+)*\\)", Pattern.CASE_INSENSITIVE);
    private Logger log = LoggerFactory.getLogger(GDataPropertyFilter.class);
    private InjectValueFactory injectValueFactory;

    public GDataPropertyFilter() {
        superClassCache = new HashMap<>();
        aliasParsedCache = new HashMap<>();
    }

    @Override
    public String getFilterName() {
        return "gdata";
    }


    @Override
    protected boolean include(final BeanPropertyWriter writer) {
        Class<?> clazz = writer.getMember().getDeclaringClass();
        String propName = writer.getName();
        if (!isPrimitive(clazz)) {
            propName = getMostSuperClassName(clazz) + "." + propName;
        }
        checkForCrafterAnnotations(writer);
        return checkProperty(propName);
    }

    private void checkForCrafterAnnotations(final BeanPropertyWriter writer) {
        InjectValue annotations = writer.getAnnotation(InjectValue.class);
        if(annotations!=null && injectValueFactory!=null){

        }
    }

    @Override
    protected boolean include(final PropertyWriter writer) {
        if (writer instanceof BeanPropertyWriter) {
            return include((BeanPropertyWriter)writer);
        }
        return checkProperty(writer.getName());
    }

    protected boolean checkProperty(final String propertyName) {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
            .getRequest();
        Object attributes = request.getParameter(selectorParameterName);

        if (attributes == null) {
            return true;
        } else {
            List<String> query = parseRequestSelector(attributes.toString());
            if (query == null || query.isEmpty()) {
                log.warn("Result of parsing selector {} is null or empty ignoring selector", attributes);
                return true;
            } else {
                return checkPropertyAgainstPattern(query, propertyName);
            }
        }
    }

    protected List<String> parseRequestSelector(final String selectorStr) {

        if (selectorStr.startsWith(SELECTOR_ALIAS_PREFIX)) {
            String cacheKey = selectorStr.substring(1);
            if (aliasParsedCache.containsKey(cacheKey)) {
                return aliasParsedCache.get(cacheKey);
            } else {
                String aliasStr = alias.get(cacheKey);
                if (aliasStr == null || StringUtils.isWhitespace(aliasStr)) {
                    log.error("Selector with name {} is not register or is whitespace ignoring", selectorStr);
                    return null;
                }
                List<String> list = internalParser(aliasStr);
                aliasParsedCache.put(cacheKey, list);
                return list;
            }
        } else {
            return internalParser(selectorStr);
        }
    }

    protected List<String> internalParser(final String selectorStr) {
        try {
            Matcher m = p.matcher(selectorStr);
            List<String> matches = new ArrayList<>();
            while (m.find()) {
                String str = m.group().toLowerCase();
                String[] properties = str.substring(str.lastIndexOf("(")).replaceAll("\\(", "").replaceAll("\\)",
                    "").split(",");

                for (String property : properties) {
                    matches.add(str.substring(0, str.lastIndexOf("(")) + "." + property);
                }
            }
            return matches;
        } catch (Exception ex) {
            log.error("Unable to parse Selector " + selectorStr, ex);
            return null;
        }
    }

    protected String getMostSuperClassName(Class<?> clazz) {
        if (!superClassCache.containsKey(clazz.getName())) {
            List<Class<?>> superClasses = ClassUtils.getAllSuperclasses(clazz);
            // WE don't count object
            String className;
            if (superClasses.size() == 1) {
                className = clazz.getSimpleName();
            } else {
                className = superClasses.get(0).getSimpleName();
            }
            superClassCache.put(clazz.getName(), className);
        }
        return superClassCache.get(clazz.getName());
    }

    protected boolean checkPropertyAgainstPattern(final List<String> pattern, final String propertyName) {
        return pattern.contains(propertyName.toLowerCase());
    }

    public void setSelectorParameterName(final String selectorParameterName) {
        this.selectorParameterName = selectorParameterName;
    }

    public void setAlias(final OverrideProperties alias) {
        this.alias = alias;
    }
}
