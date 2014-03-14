/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for encryption/decryption.
 *
 * @author Alfonso Vásquez
 */
public class CipherUtils {

    public static final String AES_CIPHER_ALGORITHM =               "AES";
    public static final int AES_KEY_BYTE_SIZE =                     16;
    public static final String DEFAULT_AES_CIPHER_TRANSFORMATION =  "AES/CBC/PKCS5Padding";

    private CipherUtils() {
    }

    /**
     * Generates a random AES encryption key.
     *
     * @return the generated key
     */
    public static Key generateAesKey() {
        try {
            return generateKey(AES_CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // Should NEVER happen
            throw new InternalError(e.getMessage());
        }
    }

    /**
     * Generates a random encryption key.
     *
     * @param cipherAlgorithm   the cipher algorithm the key will be used with. Will determine the key size
     *
     * @return the generated key
     */
    public static Key generateKey(String cipherAlgorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(cipherAlgorithm);
        keyGenerator.init(CryptoUtils.secureRandom);

        return keyGenerator.generateKey();
    }

    /**
     * Generates a random initialization vector for an AES cipher.
     *
     * @return the generated IV
     */
    public static byte[] generateAesIv() {
        return CryptoUtils.generateRandomBytes(AES_KEY_BYTE_SIZE);
    }

}
