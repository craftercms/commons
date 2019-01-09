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

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alfonso Vásquez
 */
public class SimpleCipherTest {

    public static final String CLEAR_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed fringilla.";

    @Test
    public void testEncryption() throws Exception {
        SimpleCipher encryptionCipher = new SimpleCipher();
        String encrypted = encryptionCipher.encryptBase64(CLEAR_TEXT);

        Key key = encryptionCipher.getKey();
        byte[] iv = encryptionCipher.getIv();

        Cipher decryptionCipher = Cipher.getInstance(CryptoUtils.DEFAULT_AES_CIPHER_TRANSFORMATION);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] clearBytes = decryptionCipher.doFinal(Base64.decodeBase64(encrypted));
        String clear = StringUtils.newStringUtf8(clearBytes);

        assertEquals(CLEAR_TEXT, clear);
    }

    @Test
    public void testDecryption() throws Exception {
        Key key = CryptoUtils.generateAesKey();
        byte[] iv = CryptoUtils.generateAesIv();

        Cipher encryptionCipher = Cipher.getInstance(CryptoUtils.DEFAULT_AES_CIPHER_TRANSFORMATION);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        String encrypted = Base64.encodeBase64String(encryptionCipher.doFinal(StringUtils.getBytesUtf8(CLEAR_TEXT)));

        SimpleCipher decryptionCipher = new SimpleCipher();
        decryptionCipher.setKey(key);
        decryptionCipher.setIv(iv);

        String clear = decryptionCipher.decryptBase64(encrypted);

        assertEquals(CLEAR_TEXT, clear);
    }

}
