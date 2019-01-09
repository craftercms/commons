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

/**
 * Repository for storing/retrieving encryption keys.
 *
 * @author avasquez
 */
public interface SecretKeyRepository {

    /**
     * Returns the secret key for the specified key name
     *
     * @param name   the key's name in the repository
     * @param create true to create new key if there's no key with the specified name in the repository.
     * @return the key
     */
    SecretKey getKey(String name, boolean create) throws CryptoException;

    /**
     * Saves the secret key in the repository, with the specified name
     *
     * @param name the key's name in the repository
     * @param key  the key to save
     */
    void saveKey(String name, SecretKey key) throws CryptoException;

}
