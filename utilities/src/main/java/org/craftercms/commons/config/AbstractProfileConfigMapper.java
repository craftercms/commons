package org.craftercms.commons.config;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

import java.io.InputStream;
import java.util.List;

/**
 * Base class for configuration mappers that read configuration profiles and map them to profile classes.
 *
 * @author avasquez
 */
public abstract class AbstractProfileConfigMapper<T> implements ConfigurationMapper<T> {

    private static final String CONFIG_KEY_PROFILE = "profile";
    private static final String CONFIG_KEY_ID = "id";

    @Override
    @SuppressWarnings("unchecked")
    public T readConfig(InputStream inputStream, String encoding, String profileId) throws ConfigurationException {
        try {
            HierarchicalConfiguration<ImmutableNode> config = ConfigUtils.readXmlConfiguration(inputStream, encoding);

            List<HierarchicalConfiguration<ImmutableNode>> profiles = config.configurationsAt(CONFIG_KEY_PROFILE);
            HierarchicalConfiguration profileConfig = profiles.stream()
                            .filter(c -> profileId.equals(c.getString(CONFIG_KEY_ID)))
                            .findFirst()
                            .orElseThrow(() -> new ConfigurationException("Profile '" + profileId + "' not found"));

            return (T) mapProfile(profileConfig);
        } catch (org.apache.commons.configuration2.ex.ConfigurationException e) {
            throw new ConfigurationException("Error while loading configuration", e);
        }
    }

    protected abstract T mapProfile(HierarchicalConfiguration<ImmutableNode> profileConfig)
            throws ConfigurationException;

}
