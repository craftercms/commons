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
package org.craftercms.commons.config.profiles.box;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.profiles.AbstractProfileConfigMapper;
import org.craftercms.commons.config.ConfigurationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.craftercms.commons.config.ConfigUtils.*;

/**
 * Configuration mapper for {@link BoxProfile}s.
 *
 * @author joseross
 * @author avasquez
 */
public class BoxProfileMapper extends AbstractProfileConfigMapper<BoxProfile> {

    private static final String KEY_BOX = "box";
    private static final String KEY_CLIENT_ID = "clientId";
    private static final String KEY_CLIENT_SECRET = "clientSecret";
    private static final String KEY_ENTERPRISE_ID = "enterpriseId";
    private static final String KEY_PUBLIC_KEY_ID = "publicKeyId";
    private static final String KEY_PRIVATE_KEY_PASS = "privateKeyPassword";
    private static final String KEY_PRIVATE_KEY_PATH = "privateKeyPath";
    private static final String KEY_PRIVATE_KEY = "privateKey";
    private static final String KEY_UPLOAD_FOLDER = "uploadFolder";

    public BoxProfileMapper() {
        super(KEY_BOX);
    }

    @Override
    protected BoxProfile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig)
            throws ConfigurationException {
        BoxProfile boxProfile = new BoxProfile();
        boxProfile.setClientId(getRequiredStringProperty(profileConfig, KEY_CLIENT_ID));
        boxProfile.setClientSecret(getRequiredStringProperty(profileConfig, KEY_CLIENT_SECRET));
        boxProfile.setEnterpriseId(getRequiredStringProperty(profileConfig, KEY_ENTERPRISE_ID));
        String boxPrivateKey = getRequiredStringProperty(profileConfig, KEY_PRIVATE_KEY);

        if (StringUtils.isNotBlank(boxPrivateKey)) {
            boxProfile.setPrivateKey(boxPrivateKey);
        } else {
            Path privateKeyPath = Paths.get(getStringProperty(profileConfig, KEY_PRIVATE_KEY_PATH));
            try {
                boxProfile.setPrivateKey(new String(Files.readAllBytes(privateKeyPath)));
            } catch (IOException e) {
                throw new ConfigurationException("Unable to read private key file", e);
            }
        }

        boxProfile.setPrivateKeyPassword(getRequiredStringProperty(profileConfig, KEY_PRIVATE_KEY_PASS));
        boxProfile.setPublicKeyId(getRequiredStringProperty(profileConfig, KEY_PUBLIC_KEY_ID));
        boxProfile.setUploadFolder(getStringProperty(profileConfig, KEY_UPLOAD_FOLDER));

        return boxProfile;
    }

}
