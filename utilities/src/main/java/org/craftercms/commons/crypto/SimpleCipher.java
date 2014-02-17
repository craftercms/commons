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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for simplifying encryption/decryption with the {@link javax.crypto.Cipher} class.
 *
 * @author Sumer Jabri
 * @author Alfonso Vásquez
 */
public class SimpleCipher {

	private Key key;
    private byte[] iv;
	private Cipher cipher;

    public Key getKey() {
        return key;
    }

    public String getBase64Key() {
        return Base64.encodeBase64String(key.getEncoded());
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setBase64Key(String aesKey) {
        this.key = new SecretKeySpec(Base64.decodeBase64(aesKey), CipherUtils.AES_CIPHER_ALGORITHM);
    }

    public void setBase64Key(String key, String algorithm) {
        this.key = new SecretKeySpec(Base64.decodeBase64(key), algorithm);
    }

    public byte[] getIv() {
        return iv;
    }

    public String getBase64Iv() {
        return Base64.encodeBase64String(iv);
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public void setBase64Iv(String iv) {
        this.iv = Base64.decodeBase64(iv);
    }

    public Cipher getCipher() {
        return cipher;
    }

    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public void setCipherTransformation(String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(transformation);
    }

    public String encryptBase64FromString(String clear) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        return Base64.encodeBase64String(encrypt(StringUtils.getBytesUtf8(clear)));
    }

    public String encryptBase64(byte[] clear) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        return Base64.encodeBase64String(encrypt(clear));
    }

    public byte[] encrypt(byte[] clear) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        if (key == null) {
            key = CipherUtils.generateAesKey();
        }
        if (iv == null) {
            iv = CipherUtils.generateAesIv();
        }
        if (cipher == null) {
            try {
                cipher = Cipher.getInstance(CipherUtils.DEFAULT_AES_CIPHER_TRANSFORMATION);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                // Should NEVER happen
                throw new InternalError(e.getMessage());
            }
        }

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        return cipher.doFinal(clear);
    }

    public String decryptBase64ToString(String encrypted) throws IllegalStateException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return StringUtils.newStringUtf8(decrypt(Base64.decodeBase64(encrypted)));
    }

    public byte[] decryptBase64(String encrypted) throws IllegalStateException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decrypt(Base64.decodeBase64(encrypted));
    }

    public byte[] decrypt(byte[] encrypted) throws IllegalStateException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (key == null) {
            throw new IllegalStateException("No encryption key set");
        }
        if (iv == null) {
            throw new IllegalStateException("No IV set");
        }
        if (cipher == null) {
            try {
                cipher = Cipher.getInstance(CipherUtils.DEFAULT_AES_CIPHER_TRANSFORMATION);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                // Should NEVER happen
                throw new InternalError(e.getMessage());
            }
        }

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        return cipher.doFinal(encrypted);
    }

}