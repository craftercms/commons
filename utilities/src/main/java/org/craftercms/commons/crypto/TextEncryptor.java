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

/**
 * Thread-safe service that facilitates easy encryption and decryption of text.
 *
 * @author avasquez
 */
public interface TextEncryptor {

    /**
     * Encrypts the specified clear text.
     *
     * @param clear the clear text to encrypt
     *
     * @return the encrypted text
     */
    String encrypt(String clear) throws CryptoException;

    /**
     * Decrypts the specified encrypted text.
     *
     * @param encrypted the encrypted text to decrypt
     *
     * @return the clear text
     */
    String decrypt(String encrypted) throws CryptoException;

}
