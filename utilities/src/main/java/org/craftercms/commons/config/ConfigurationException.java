package org.craftercms.commons.config;

/**
 * Exception thrown by classes that handle configuration.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}


