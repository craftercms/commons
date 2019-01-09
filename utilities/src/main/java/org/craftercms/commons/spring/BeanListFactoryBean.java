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
package org.craftercms.commons.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring factory bean that creates a list of beans based on a list of bean names.
 *
 * @author avasquez
 */
public class BeanListFactoryBean implements FactoryBean<List<Object>>, ApplicationContextAware {

    protected ApplicationContext applicationContext;
    protected String[] beanNames;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Required
    public void setBeanNames(String[] beanNames) {
        this.beanNames = beanNames;
    }

    @Override
    public List<Object> getObject() throws Exception {
        List<Object> beans = new ArrayList<>(beanNames.length);

        for (String beanName : beanNames) {
            beans.add(applicationContext.getBean(beanName));
        }

        return beans;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
