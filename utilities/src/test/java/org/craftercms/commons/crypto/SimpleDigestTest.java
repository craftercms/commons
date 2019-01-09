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

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author avasquez
 */
public class SimpleDigestTest {

    public static final String CLEAR_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed fringilla.";

    @Test
    public void testDigest() throws Exception {
        SimpleDigest digest = new SimpleDigest();

        String hash = digest.digestBase64(CLEAR_TEXT);
        String salt = digest.getBase64Salt();

        MessageDigest actualDigest = MessageDigest.getInstance(SimpleDigest.DEFAULT_ALGORITHM);

        actualDigest.update(Base64.decodeBase64(salt));

        byte[] hashedBytes = actualDigest.digest(CLEAR_TEXT.getBytes("UTF-8"));

        for (int i = 0; i < SimpleDigest.DEFAULT_ITERATIONS; i++) {
            actualDigest.reset();

            hashedBytes = actualDigest.digest(hashedBytes);
        }

        assertArrayEquals(hashedBytes, Base64.decodeBase64(hash));
    }

}
