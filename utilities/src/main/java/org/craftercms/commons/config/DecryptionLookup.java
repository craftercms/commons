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
package org.craftercms.commons.config;

import org.apache.commons.configuration2.interpol.Lookup;
import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.SimpleCipher;
import org.craftercms.commons.crypto.TextEncryptor;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * Implementation of {@code Lookup} that decrypts the variable.
 *
 * @author avasquez
 */
public class DecryptionLookup implements Lookup {

    public static final String LOG_KEY_DECRYPTION_ERROR = "configuration.lookup.decryption.error";

    private static final I10nLogger logger = new I10nLogger(SimpleCipher.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    protected TextEncryptor encryptor;

    public DecryptionLookup(TextEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public Object lookup(String variable) {
        try {
            variable = encryptor.decrypt(variable);
        } catch (CryptoException e) {
            logger.error(LOG_KEY_DECRYPTION_ERROR, e);
        }

        return variable;
    }

}
