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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * Utility class for simplifying message digest generation, using the {@link java.security.MessageDigest}. Default
 * digest algorithm is SHA-256, and 1000 iterations are done to make hashes strong against attacks, according to OWASP
 * recommendations.
 *
 * @author avasquez
 */
public class SimpleDigest {

    public static final String LOG_KEY_DEF_DIGEST_CREATED = "crypto.digest.defaultDigestCreated";
    public static final String LOG_KEY_SALT_GEN = "crypto.digest.saltGenerated";
    public static final String LOG_KEY_DIGEST_GEN = "crypto.digest.digestGenerated";
    public static final String ERROR_KEY_INVALID_ALG = "crypto.digest.invalidDigestAlgorithm";

    public static final String DEFAULT_ALGORITHM = "SHA-256";
    public static final int DEFAULT_ITERATIONS = 1000;
    public static final int DEFAULT_SALT_SIZE = 16;

    private static final I10nLogger logger = new I10nLogger(SimpleDigest.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    private MessageDigest digest;
    private int iterations;
    private byte[] salt;

    public SimpleDigest() {
        iterations = DEFAULT_ITERATIONS;
    }

    public MessageDigest getDigest() {
        return digest;
    }

    public void setDigest(MessageDigest digest) {
        this.digest = digest;
    }

    public void setAlgorithm(String algorithm) throws CryptoException {
        try {
            this.digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ERROR_KEY_INVALID_ALG, ex, algorithm);
        }
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getBase64Salt() {
        return salt != null? Base64.encodeBase64String(salt): null;
    }

    public void setBase64Salt(String salt) {
        this.salt = Base64.decodeBase64(salt);
    }

    public String digestBase64(String clear) {
        return Base64.encodeBase64String(digest(StringUtils.getBytesUtf8(clear)));
    }

    public byte[] digest(byte[] clear) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                // Should NEVER happen
                throw new IllegalStateException("JVM doesn't support " + DEFAULT_ALGORITHM, e);
            }

            logger.debug(LOG_KEY_DEF_DIGEST_CREATED, DEFAULT_ALGORITHM);
        }
        if (salt == null) {
            salt = CryptoUtils.generateRandomBytes(DEFAULT_SALT_SIZE);

            logger.debug(LOG_KEY_SALT_GEN, DEFAULT_SALT_SIZE);
        }

        digest.update(salt);

        byte[] hash = digest.digest(clear);

        for (int i = 0; i < iterations; i++) {
            digest.reset();

            hash = digest.digest(hash);
        }

        logger.debug(LOG_KEY_DIGEST_GEN, iterations);

        return hash;
    }

}
