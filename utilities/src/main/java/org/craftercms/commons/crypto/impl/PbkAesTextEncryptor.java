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

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.craftercms.commons.crypto.CryptoUtils;
import org.craftercms.commons.crypto.CryptoException;

/**
 * Extension of {@link org.craftercms.commons.crypto.impl.AesTextEncryptor} that generates the encryption key based
 * on a password and salt.
 *
 * @author avasquez
 */
public class PbkAesTextEncryptor extends AesTextEncryptor {

    private static final String PBK_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int PBK_ITER = 65536;
    private static final int PBK_LEN = 128;

    private static Key generateKey(String password, String salt) throws CryptoException {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), Base64.decodeBase64(salt), PBK_ITER, PBK_LEN);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBK_ALGORITHM);

            return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), CryptoUtils.AES_CIPHER_ALGORITHM);
        } catch (GeneralSecurityException e) {
            throw new CryptoException("Unable to generate PBK key", e);
        }
    }

    public PbkAesTextEncryptor(String password, String salt) throws CryptoException {
        super(generateKey(password, salt));
    }

}
