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
package org.craftercms.commons.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Wraps an instance of {@link ApplicationContext} and provides simple access to beans for Groovy & Freemarker
 *
 * @author joseross
 * @since 4.0
 */
public class ApplicationContextAccessor implements ApplicationContextAware {

    private ApplicationContext actualApplicationContext;

    public ApplicationContextAccessor() {
    }

    public ApplicationContextAccessor(ApplicationContext actualApplicationContext) {
        this.actualApplicationContext = actualApplicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.actualApplicationContext = applicationContext;
    }

    public Object get(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    public <T> T get(String beanName, Class<T> requiredType) {
        return getApplicationContext().getBean(beanName, requiredType);
    }

    protected ApplicationContext getApplicationContext() {
        return actualApplicationContext;
    }

}
