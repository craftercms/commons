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

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * Utility class for simplifying encryption/decryption with the {@link javax.crypto.Cipher} class. By default, the
 * algorithm used is AES.
 *
 * @author Sumer Jabri
 * @author Alfonso Vásquez
 */
public class SimpleCipher {

    public static final String LOG_KEY_ENC_SUCCESSFUL = "crypto.cipher.encryptionSuccessful";
    public static final String LOG_KEY_DEC_SUCCESSFUL = "crypto.cipher.decryptionSuccessful";
    public static final String LOG_KEY_KEY_GEN = "crypto.cipher.keyGenerated";
    public static final String LOG_KEY_IV_GEN = "crypto.cipher.ivGenerated";
    public static final String LOG_KEY_DEF_CIPHER_CREATED = "crypto.cipher.defaultCipherCreated";

    public static final String ERROR_KEY_KEY_NOT_SET = "crypto.cipher.keyNotSet";
    public static final String ERROR_KEY_IV_NOT_SET = "crypto.cipher.ivNotSet";
    public static final String ERROR_KEY_ENC_ERROR = "crypto.cipher.encryptionError";
    public static final String ERROR_KEY_DEC_ERROR = "crypto.cipher.decryptionError";

    private static final I10nLogger logger = new I10nLogger(SimpleCipher.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    private Key key;
    private byte[] iv;
    private Cipher cipher;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public Cipher getCipher() {
        return cipher;
    }

    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public String encryptBase64(String clear) throws CryptoException {
        return Base64.encodeBase64String(encrypt(StringUtils.getBytesUtf8(clear)));
    }

    public byte[] encrypt(byte[] clear) throws CryptoException {
        if (key == null) {
            key = CryptoUtils.generateAesKey();

            logger.debug(LOG_KEY_KEY_GEN);
        }
        if (iv == null) {
            iv = CryptoUtils.generateAesIv();

            logger.debug(LOG_KEY_IV_GEN);
        }
        if (cipher == null) {
            cipher = createDefaultCipher();
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

            return cipher.doFinal(clear);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(ERROR_KEY_ENC_ERROR, e);
        } finally {
            logger.debug(LOG_KEY_ENC_SUCCESSFUL);
        }
    }

    public String decryptBase64(String encrypted) throws CryptoException {
        return StringUtils.newStringUtf8(decrypt(Base64.decodeBase64(encrypted)));
    }

    public byte[] decrypt(byte[] encrypted) throws CryptoException {
        if (key == null) {
            throw new CryptoException(ERROR_KEY_KEY_NOT_SET);
        }
        if (iv == null) {
            throw new CryptoException(ERROR_KEY_IV_NOT_SET);
        }
        if (cipher == null) {
            cipher = createDefaultCipher();
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            return cipher.doFinal(encrypted);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(ERROR_KEY_DEC_ERROR, e);
        } finally {
            logger.debug(LOG_KEY_DEC_SUCCESSFUL);
        }
    }

    protected Cipher createDefaultCipher() {
        String cipherTransformation = CryptoUtils.DEFAULT_AES_CIPHER_TRANSFORMATION;

        try {
            return Cipher.getInstance(cipherTransformation);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            // Should NEVER happen
            throw new IllegalStateException("JVM doesn't support " + cipherTransformation, e);
        } finally {
            logger.debug(LOG_KEY_DEF_CIPHER_CREATED, cipherTransformation);
        }
    }

}