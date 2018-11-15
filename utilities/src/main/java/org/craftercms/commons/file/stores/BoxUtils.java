package org.craftercms.commons.file.stores;

import com.box.sdk.*;
import org.craftercms.commons.config.box.BoxProfile;

/**
 * Utility methods for Box.
 *
 * @author joseross
 * @author avasquez
 */
public class BoxUtils {

    /**
     * Creates a {@code BoxAPIConnection} based on the given config profile.
     *
     * @param profile the configuration profile
     *
     * @return a connection to a Box repository
     */
    public static final BoxAPIConnection createConnection(BoxProfile profile) {
        JWTEncryptionPreferences jwtPrefs = new JWTEncryptionPreferences();
        jwtPrefs.setPublicKeyID(profile.getPublicKeyId());
        jwtPrefs.setPrivateKey(profile.getPrivateKey());
        jwtPrefs.setPrivateKeyPassword(profile.getPrivateKeyPassword());
        jwtPrefs.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

        BoxConfig config = new BoxConfig(profile.getClientId(), profile.getClientSecret(), profile.getEnterpriseId(),
                                         jwtPrefs);

        return BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(config);
    }

    private BoxUtils() {
    }
}
