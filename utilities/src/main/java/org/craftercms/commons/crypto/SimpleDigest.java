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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for simplifying message digest generation, using the {@link java.security.MessageDigest}. Default
 * digest algorithm is SHA-256, and the 1000 iterations to make hashes strong against attack, according to OWASP
 * recommendations.
 *
 * @author avasquez
 */
public class SimpleDigest {

    public static final String DEFAULT_ALGORITHM =  "SHA-256";
    public static final int DEFAULT_ITERATIONS =    1000;
    public static final int DEFAULT_SALT_SIZE =     16;

    private MessageDigest digest;
    private int iterations;
    private byte[] salt;

    public SimpleDigest() {
        iterations = DEFAULT_ITERATIONS;
    }

    public MessageDigest getDigest() {
        return digest;
    }

    public void setAlgorithm(String algorithm) throws IllegalArgumentException {
        try {
            this.digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Unrecognized message digest algorithm '" + algorithm + "'", ex);
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
        return salt != null? Base64.encodeBase64String(salt) : null;
    }

    public void setBase64Salt(String salt) {
        this.salt = Base64.decodeBase64(salt);
    }

    public String digestBase64(String clear) {
        return Base64.encodeBase64String(digest(clear));
    }

    public byte[] digest(String clear) {
        try {
            return digest(clear.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // Should NEVER happen
            throw new InternalError();
        }
    }

    public byte[] digest(byte[] clear) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                // Should NEVER happen
                throw new InternalError();
            }
        }
        if (salt == null) {
            salt = CryptoUtils.generateRandomBytes(DEFAULT_SALT_SIZE);
        }

        digest.update(salt);

        byte[] hash = clear;

        for (int i = 0; i < iterations; i++) {
            digest.reset();

            hash = digest.digest(hash);
        }

        return hash;
    }

}
