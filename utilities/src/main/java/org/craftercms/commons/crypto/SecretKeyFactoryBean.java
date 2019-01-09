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
package org.craftercms.commons.crypto;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Spring factory bean used to easily inject a secret key from a
 * {@link org.craftercms.commons.crypto.SecretKeyRepository}.
 *
 * @author avasquez
 */
public class SecretKeyFactoryBean implements FactoryBean<SecretKey> {

    private String keyName;
    private boolean create;
    private SecretKeyRepository keyRepository;

    @Required
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Required
    public void setCreate(boolean create) {
        this.create = create;
    }

    @Required
    public void setKeyRepository(SecretKeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Override
    public SecretKey getObject() throws Exception {
        return keyRepository.getKey(keyName, create);
    }

    @Override
    public Class<?> getObjectType() {
        return SecretKey.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
