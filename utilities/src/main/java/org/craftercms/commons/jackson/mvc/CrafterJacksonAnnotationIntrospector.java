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

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import org.craftercms.commons.jackson.mvc.annotations.Exclude;
import org.craftercms.commons.jackson.mvc.annotations.SecureProperty;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extends JacksonAnnotationIntrospector so it will always a filter (so @JsonFilter) annotation is not needed.
 */
public class CrafterJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -4614283468824831495L;

    private String defaultFilter;
    private SecurePropertyHandler securityPropertyFilter;

    @Override
    public Object findFilterId(final Annotated a) {
        Object base = super.findFilterId(a);
        if (base == null) {
            base = defaultFilter;
        }
        return base;
    }

    @Override
    public boolean hasIgnoreMarker(final AnnotatedMember m) {
        if (m.getAnnotated().isAnnotationPresent(Exclude.class)) {
            return true;
        } else if (m.getAnnotated().isAnnotationPresent(SecureProperty.class) && securityPropertyFilter != null) {
            SecureProperty property = m.getAnnotated().getAnnotation(SecureProperty.class);
            return securityPropertyFilter.suppressProperty(m.getDeclaringClass().getCanonicalName() + "." + m.getName
                (), property.role());
        } else {
            return super.hasIgnoreMarker(m);
        }
    }

    public void setSecurityPropertyFilter(final SecurePropertyHandler securityPropertyFilter) {
        this.securityPropertyFilter = securityPropertyFilter;
    }

    @Required
    public void setDefaultFilter(final String defaultFilter) {
        this.defaultFilter = defaultFilter;
    }
}
