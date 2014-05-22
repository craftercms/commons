package org.craftercms.commons.logging;

/**
 * Utility for logging a method entry/exit.
 *
 * @author avasquez
 */
public interface MethodLogger {

    /**
     * Logs a method entry.
     *
     * @param className     the method's class name
     * @param methodName    the method's name
     * @param args          the method's arguments
     */
    void logEntry(String className, String methodName, Object[] args);

    /**
     * Logs a method exit.
     *
     * @param className     the method's class name
     * @param methodName    the method's name
     * @param returnValue   the method's return value
     */
    void logExit(String className, String methodName, Object returnValue);

    /**
     * Logs a method failure with a exception.
     *
     * @param className     the method's class name
     * @param methodName    the method's name
     * @param e             the exception
     */
    void logException(String className, String methodName, Throwable e);

}
