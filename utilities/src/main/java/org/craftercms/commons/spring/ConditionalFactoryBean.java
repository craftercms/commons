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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Simple Spring factory bean that only returns an actual bean if the specified flag is true.
 *
 * @author avasquez
 */
public class ConditionalFactoryBean implements FactoryBean<Object> {

    private Object actualBean;
    private boolean flag;

    @Required
    public void setActualBean(Object actualBean) {
        this.actualBean = actualBean;
    }

    @Required
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public Object getObject() throws Exception {
        if (flag) {
            return actualBean;
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return actualBean.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
