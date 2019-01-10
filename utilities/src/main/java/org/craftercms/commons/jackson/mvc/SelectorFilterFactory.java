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

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 *
 */
public class SelectorFilterFactory extends AbstractFactoryBean<FilterProvider> {

    private List<AbstractCrafterPropertyFilter> filters;

    public SelectorFilterFactory() {
        filters = new ArrayList<>();
    }

    public SelectorFilterFactory(final List<AbstractCrafterPropertyFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Class<?> getObjectType() {
        return FilterProvider.class;
    }

    @Override
    protected FilterProvider createInstance() throws Exception {
        SimpleFilterProvider provider = new SimpleFilterProvider();
        for (AbstractCrafterPropertyFilter filter : filters) {
            provider.addFilter(filter.getFilterName(), filter);
        }
        if (!filters.isEmpty()) {
            provider.setDefaultFilter(filters.get(0));
        }
        return provider;
    }

    public void setFilters(final List<AbstractCrafterPropertyFilter> filters) {
        this.filters = filters;
    }
}
