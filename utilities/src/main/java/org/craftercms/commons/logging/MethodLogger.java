/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
