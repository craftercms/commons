package org.craftercms.commons.config;

import java.io.InputStream;

/**
 * Interface for classes that map configuration data to Java bean classes.
 *
 * @author avasquez
 */
public interface ConfigurationMapper<T> {

    /**
     * Reads the configuration from the specified input stream, extracts the specific sub-configuration identified by
     * {@code configId} and maps the configuration to a Java bean.
     *
     * @param inputStream the IS of the configuration file
     * @param encoding the charset encoding of the configuration file
     * @param configId the ID of the specific sub-configuration to map
     *
     * @return
     *
     * @throws ConfigurationException if an error occurs
     */
    T readConfig(InputStream inputStream, String encoding, String configId) throws ConfigurationException;

}
