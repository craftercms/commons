/*
 * Copyright (C) 2007-2023 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.rest.parameters;

/**
 * Represents a sort field-order pair.
 */
public class SortField {
    private final String field;
    private Order order = Order.ASC;

    public SortField(final String sort) {
        String[] sortItems = sort.split("\\s+");
        if (sortItems.length > 2) {
            throw new IllegalArgumentException("Invalid sort field: " + sort);
        }
        this.field = sortItems[0];
        if (sortItems.length > 1) {
            this.order = Order.valueOf(sortItems[1].toUpperCase());
        }
    }

    public SortField(final String field, final Order order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }

    public enum Order {
        ASC, DESC
    }
}
