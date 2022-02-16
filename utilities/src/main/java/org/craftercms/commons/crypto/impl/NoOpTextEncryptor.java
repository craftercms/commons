/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.crypto.impl;

import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.TextEncryptor;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * {@link org.craftercms.commons.crypto.TextEncryptor} that does not actually encrypt or decrypt the text at all.
 * Useful for development environments, but should completely be disregarded in production environments. That's why
 * and warning is issued during post construct
 *
 * @author avasquez
 */
public class NoOpTextEncryptor implements TextEncryptor, InitializingBean {

    private static final I10nLogger logger = new I10nLogger(NoOpTextEncryptor.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    public static final String LOG_KEY_NOOP_USED = "crypto.textEncryptor.noOpUsed";

    public void afterPropertiesSet() {
        logger.warn(LOG_KEY_NOOP_USED);
    }

    @Override
    public String encrypt(final String clear) throws CryptoException {
        return clear;
    }

    @Override
    public String decrypt(final String encrypted) throws CryptoException {
        return encrypted;
    }

}
