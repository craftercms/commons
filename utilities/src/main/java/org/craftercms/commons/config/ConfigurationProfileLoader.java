package org.craftercms.commons.config;

public interface ConfigurationProfileLoader<T extends ConfigurationProfile> {

    T loadProfile(String id) throws ConfigurationException;

}
