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

package org.craftercms.commons.plugin.model;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds the information for a parameter
 *
 * @author joseross
 * @since 3.1.4
 */
public class Parameter {

    public static String NAME_REGEX = "[a-z]+([A-Z][a-z0-9]+)+";

    /**
     * The label to display for the parameter
     */
    protected String label;

    /**
     * The name of the parameter (must be camelCase)
     */
    protected String name;

    /**
     * The type of the parameter
     */
    protected Type type = Type.STRING;

    /**
     * Indicates if the parameter is required
     */
    protected boolean required = true;

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (StringUtils.isEmpty(name) || !name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException(name + " is not a valid parameter name");
        }
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameter)) {
            return false;
        }
        final Parameter parameter = (Parameter)o;
        return required == parameter.required && label.equals(parameter.label) && name.equals(parameter.name) &&
            type.equals(parameter.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, name, type, required);
    }

    @Override
    public String toString() {
        return "Parameter{" + "label='" + label + '\'' + ", name='" + name + '\'' + ", type='" + type + '\'' + ", "
            + "required=" + required + '}';
    }

    /**
     * Possible types for the parameters
     */
    public enum Type {
        STRING,
        PASSWORD
    }

}