package org.craftercms.commons.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.config.AbstractProfileConfigMapper;
import org.craftercms.commons.config.ConfigurationException;

/**
 * Base class for configuration mappers that map to {@link AbstractAwsProfile}s.
 *
 * @author joseross
 * @auhor avasquez
 */
public abstract class AbstractAwsProfileMapper<T extends AbstractAwsProfile> extends AbstractProfileConfigMapper<T> {

    private static final String CONFIG_KEY_REGION = "region";
    private static final String CONFIG_KEY_ACCESS_KEY = "credentials.accessKey";
    private static final String CONFIG_KEY_SECRET_KEY = "credentials.secretKey";

    @Override
    @SuppressWarnings("unchecked")
    protected T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig) throws ConfigurationException {
        AbstractAwsProfile profile = createProfile();
        if (profileConfig.containsKey(CONFIG_KEY_ACCESS_KEY) && profileConfig.containsKey(CONFIG_KEY_SECRET_KEY)) {
            String accessKey = profileConfig.getString(CONFIG_KEY_ACCESS_KEY);
            String secretKey = profileConfig.getString(CONFIG_KEY_SECRET_KEY);

            profile.setCredentialsProvider(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
        } else {
            profile.setCredentialsProvider(new DefaultAWSCredentialsProviderChain());
        }

        if (profileConfig.containsKey(CONFIG_KEY_REGION)) {
            profile.setRegion(profileConfig.getString(CONFIG_KEY_REGION));
        }

        return (T) profile;
    }

    protected abstract AbstractAwsProfile createProfile();

}
