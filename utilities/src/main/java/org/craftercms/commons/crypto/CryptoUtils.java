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

import java.security.SecureRandom;

/**
 * Utility methods that can be used both for cipher and message digest operations.
 *
 * @author avasquez
 */
public class CryptoUtils {

    public static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a random array of bytes, using the singleton {@link java.security.SecureRandom}, and then encodes
     * them to Base 64.
     *
     * @param size  the size of the array
     *
     * @return the generated array, encoded as Base 64
     */
    public String generateBase64RandomBytes(int size) {
        return Base64.encodeBase64String(generateRandomBytes(size));
    }

    /**
     * Generates a random array of bytes, using the singleton {@link java.security.SecureRandom}.
     *
     * @param size  the size of the array
     *
     * @return the generated array
     */
    public static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];

        secureRandom.nextBytes(bytes);

        return bytes;
    }

}
