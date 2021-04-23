/*
 * Copyright (C) 2007-2021 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Holds the data required to install a plugin in a configuration file
 *
 * @author joseross
 * @since 4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Installation {

    /**
     * The type of the widget to install
     */
    protected String type;

    /**
     * The parent in the configuration file
     */
    protected Parent parent;

    /**
     * The new element to add in the configuration file
     */
    protected Element element;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Holds the data to locate the parent in the configuration file
     */
    public static class Parent {

        /**
         * The id of the parent widget, optional
         */
        protected String id = "null";

        /**
         * The xpath of the parent element, optional
         */
        protected String xpath = "null";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getXpath() {
            return xpath;
        }

        public void setXpath(String xpath) {
            this.xpath = xpath;
        }

    }

    /**
     * Represents an XML element
     */
    public static class Element {

        protected String name;

        protected String value;

        protected List<Attribute> attributes;

        protected List<Element> children;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }

        public List<Element> getChildren() {
            return children;
        }

        public void setChildren(List<Element> children) {
            this.children = children;
        }
    }

    /**
     * Represents an XML attribute
     */
    public static class Attribute {

        protected String name;

        protected String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
