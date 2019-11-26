package org.craftercms.commons.exceptions;

/**
 * Exception thrown when monitoring API is called with invalid authorization token
 */
public class InvalidMonitoringTokenException extends Exception {

    public InvalidMonitoringTokenException(String message) {
        super(message);
    }
}
