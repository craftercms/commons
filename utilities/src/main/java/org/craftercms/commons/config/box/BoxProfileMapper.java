package org.craftercms.commons.config.box;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.config.AbstractProfileConfigMapper;
import org.craftercms.commons.config.ConfigurationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration mapper for {@link BoxProfile}s.
 *
 * @author joseross
 * @auhor avasquez
 */
public class BoxProfileMapper extends AbstractProfileConfigMapper<BoxProfile> {

    private static final String KEY_CLIENT_ID = "clientId";
    private static final String KEY_CLIENT_SECRET = "clientSecret";
    private static final String KEY_ENTERPRISE_ID = "enterpriseId";
    private static final String KEY_PUBLIC_KEY_ID = "publicKeyId";
    private static final String KEY_PRIVATE_KEY_PASS = "privateKeyPassword";
    private static final String KEY_PRIVATE_KEY_PATH = "privateKeyPath";
    private static final String KEY_PRIVATE_KEY = "privateKey";
    private static final String KEY_UPLOAD_FOLDER = "uploadFolder";

    @Override
    protected BoxProfile mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig)
            throws ConfigurationException {
        BoxProfile boxProfile = new BoxProfile();
        boxProfile.setClientId(profileConfig.getString(KEY_CLIENT_ID));
        boxProfile.setClientSecret(profileConfig.getString(KEY_CLIENT_SECRET));
        boxProfile.setEnterpriseId(profileConfig.getString(KEY_ENTERPRISE_ID));
        String boxPrivateKey = profileConfig.getString(KEY_PRIVATE_KEY);

        if (StringUtils.isNotBlank(boxPrivateKey)) {
            boxProfile.setPrivateKey(boxPrivateKey);
        } else {
            Path privateKeyPath = Paths.get(profileConfig.getString(KEY_PRIVATE_KEY_PATH));
            try {
                boxProfile.setPrivateKey(new String(Files.readAllBytes(privateKeyPath)));
            } catch (IOException e) {
                throw new ConfigurationException("Unable to read private key file", e);
            }
        }

        boxProfile.setPrivateKeyPassword(profileConfig.getString(KEY_PRIVATE_KEY_PASS));
        boxProfile.setPublicKeyId(profileConfig.getString(KEY_PUBLIC_KEY_ID));
        boxProfile.setUploadFolder(profileConfig.getString(KEY_UPLOAD_FOLDER));

        return boxProfile;
    }

}
