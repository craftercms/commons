package org.craftercms.commons.crypto.impl;

import javax.annotation.PostConstruct;

import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.TextEncryptor;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * {@link org.craftercms.commons.crypto.TextEncryptor} that does not actually encrypt or decrypt the text at all.
 * Useful for development environments, but should completely be disregarded in production environments. That's why
 * and warning is issued during post construct
 *
 * @author avasquez
 */
public class NoOpTextEncryptor implements TextEncryptor {

    private static final I10nLogger logger = new I10nLogger(NoOpTextEncryptor.class, I10nUtils.COMMONS_LOGGING_MESSAGES_BUNDLE_NAME);

    public static final String LOG_KEY_NOOP_USED = "crypto.textEncryptor.noOpUsed";

    @PostConstruct
    public void init() {
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
