package org.craftercms.commons.crypto.impl;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.craftercms.commons.crypto.CipherUtils;
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

            return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), CipherUtils.AES_CIPHER_ALGORITHM);
        } catch (GeneralSecurityException e) {
            throw new CryptoException("Unable to generate PBK key", e);
        }
    }

    public PbkAesTextEncryptor(String password, String salt) throws CryptoException {
        super(generateKey(password, salt));
    }

}
