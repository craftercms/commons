/*
 * Copyright (C) 2007-${year} Crafter Software Corporation.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.jackson.mvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.apache.commons.beanutils.PropertyUtils;
import org.craftercms.commons.jackson.mvc.annotations.InjectValue;
import org.craftercms.commons.jackson.mvc.annotations.InjectValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;


public class CrafterJackson2MessageConverter extends MappingJackson2HttpMessageConverter {

    protected String jsonPrefix;
    protected FilterProvider filter;
    protected InjectValueFactory injectValueFactory;
    private Logger log = LoggerFactory.getLogger(CrafterJackson2MessageConverter.class);


    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
        HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(),
                encoding);
        // A workaround for JsonGenerators not applying serialization features
        // https://github.com/FasterXML/jackson-databind/issues/12
        if (this.getObjectMapper().isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        try {
            if (this.jsonPrefix != null) {
                jsonGenerator.writeRaw(this.jsonPrefix);
            }
            if (injectValueFactory != null) {
                injectValues(object);
            }
            ObjectWriter writer = this.getObjectMapper().writer(filter);
            writer.writeValue(jsonGenerator, object);
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private void injectValues(final Object object) {
        try {
            if (Iterable.class.isInstance(object) && !Iterator.class.isInstance(object)) {
                for (Object element : (Iterable)object) {
                    injectValues(element);
                }
            }

            PropertyDescriptor[] propertiesDescriptor = PropertyUtils.getPropertyDescriptors(object);
            for (PropertyDescriptor propertyDescriptor : propertiesDescriptor) {
                // Avoid the "getClass" as a property
                if(propertyDescriptor.getPropertyType().equals(Class.class) ||
                  (propertyDescriptor.getReadMethod() == null && propertyDescriptor.getWriteMethod() == null)){
                    continue;
                }
                Field field = findField(object.getClass(), propertyDescriptor.getName());
                if (field != null && field.isAnnotationPresent(InjectValue.class)) {
                    injectValue(object, field);
                    continue;
                }

                Object fieldValue = PropertyUtils.getProperty(object, propertyDescriptor.getName());
                if (Iterable.class.isInstance(fieldValue) && !Iterator.class.isInstance(object)) {
                    for (Object element : (Iterable)fieldValue) {
                        injectValues(element);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Unable to inject value for " + object.getClass(), e);
        }
    }


    private Field findField(final Class<?> object, final String fieldName) {
        if (object != null) {
            try {
                return object.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                return findField(object.getSuperclass(), fieldName);
            }
        }

        log.debug("Field {} does not exist", fieldName);

        return null;
    }

    private void injectValue(final Object object, final Field field) {
        //Should be null due we ask before if the annotation exists !!
        String propertyToUseName = field.getAnnotation(InjectValue.class).useProperty();
        try {
            Object propertyValue = PropertyUtils.getProperty(object, propertyToUseName);
            Object valueToInject = injectValueFactory.getObjectFor(PropertyUtils.getPropertyType(object,
                    field.getName()), propertyValue, object);
            PropertyUtils.setProperty(object, field.getName(), valueToInject);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("Unable to inject value " + field.getName() + " for class " + object.getClass(), e);
        }
    }

    public String getJsonPrefix() {
        return jsonPrefix;
    }

    public void setJsonPrefix(final String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
        super.setJsonPrefix(jsonPrefix);
    }

    @Required()
    public void setFilter(final FilterProvider filter) {
        this.filter = filter;
    }


    public void setInjectValueFactory(final InjectValueFactory injectValueFactory) {
        this.injectValueFactory = injectValueFactory;
    }
}
