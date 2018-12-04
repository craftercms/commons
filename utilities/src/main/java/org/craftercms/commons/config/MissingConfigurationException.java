package org.craftercms.commons.config;

/**
 * {@link ConfigurationException} thrown when a specific configuration property or sub-configuration is missing.
 *
 * @author avasquez
 */
public class MissingConfigurationException extends ConfigurationException {

    public MissingConfigurationException(String message) {
        super(message);
    }

    public MissingConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
