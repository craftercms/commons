package org.craftercms.commons.lang;

/**
 * Represents a general callback that can be used almost in any method that needs this pattern.
 *
 * @author avasquez
 */
public interface Callback<T> {

    /**
     * Executes the callback, returning the result or throwing a runtime exception if an error occurs.
     *
     * @return the result of the execution
     */
    T execute();

}
