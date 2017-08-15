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

    private static final I10nLogger logger = new I10nLogger(SimpleCipher.class, I10nUtils.DEFAULT_LOGGING_BUNDLE_NAME);

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
