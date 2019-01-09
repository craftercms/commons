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
package org.craftercms.commons.crypto.impl;

import java.security.Key;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.craftercms.commons.crypto.CryptoUtils;
import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.SimpleCipher;
import org.craftercms.commons.crypto.TextEncryptor;

/**
 * Implementation of {@link org.craftercms.commons.crypto.TextEncryptor} that uses AES as it's cipher algorithm and
 * Base 64 to encode raw bytes.
 *
 * @author avasquez
 */
public class AesTextEncryptor implements TextEncryptor {

    private Key key;

    public AesTextEncryptor(final Key key) {
        this.key = key;
    }

    @Override
    public String encrypt(final String clear) throws CryptoException {
        SimpleCipher cipher = new SimpleCipher();
        cipher.setKey(key);

        byte[] encrypted = cipher.encrypt(StringUtils.getBytesUtf8(clear));
        byte[] iv = cipher.getIv();

        return Base64.encodeBase64String(ArrayUtils.addAll(iv, encrypted));
    }

    @Override
    public String decrypt(final String encrypted) throws CryptoException {
        byte[] decoded = Base64.decodeBase64(encrypted);
        byte[] iv = ArrayUtils.subarray(decoded, 0, CryptoUtils.AES_KEY_BYTE_SIZE);
        byte[] encryptedBytes = ArrayUtils.subarray(decoded, CryptoUtils.AES_KEY_BYTE_SIZE, decoded.length);

        SimpleCipher cipher = new SimpleCipher();
        cipher.setKey(key);
        cipher.setIv(iv);

        return StringUtils.newStringUtf8(cipher.decrypt(encryptedBytes));
    }

}
