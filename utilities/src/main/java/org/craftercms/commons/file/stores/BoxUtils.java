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
package org.craftercms.commons.file.stores;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.EncryptionAlgorithm;
import com.box.sdk.JWTEncryptionPreferences;
import org.craftercms.commons.config.profiles.box.BoxProfile;

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
