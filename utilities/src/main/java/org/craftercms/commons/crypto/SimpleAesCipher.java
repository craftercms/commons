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

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Sumer Jabri
 *
 */
@Deprecated
public class SimpleAesCipher {
    private SecretKeySpec skeySpec;
    private Cipher cipher;

    public SimpleAesCipher(String base64Key) {
        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        kgen.init(128); // 192 and 256 bits may not be available

        byte[] raw = Base64.decodeBase64(base64Key);

        skeySpec = new SecretKeySpec(raw, "AES");

        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setKey(byte[] key) {
        skeySpec = new SecretKeySpec(key, "AES");
    }

    public byte[] encrypt(byte[] clear) throws InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(clear);
    }

    public byte[] decrypt(byte[] encrypted) throws InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        return cipher.doFinal(encrypted);
    }
}