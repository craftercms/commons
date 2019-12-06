package org.craftercms.commons.exceptions;

/**
 * Exception thrown when monitoring API is called with invalid authorization token
 */
public class InvalidManagementTokenException extends Exception {

    public InvalidManagementTokenException(String message) {
        super(message);
    }
}
