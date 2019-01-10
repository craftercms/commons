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
package org.craftercms.commons.i10n;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Localized logger, which wraps an slf4j logger.
 *
 * @author avasquez
 */
public class I10nLogger {

    protected Logger actualLogger;
    protected ResourceBundle resourceBundle;

    public I10nLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);
    }

    public I10nLogger(Class<?> clazz, String bundleName) {
        this(LoggerFactory.getLogger(clazz), bundleName);
    }

    public I10nLogger(Logger actualLogger, String bundleName) {
        this.actualLogger = actualLogger;
        this.resourceBundle = ResourceBundle.getBundle(bundleName);
    }

    public Logger getActualLogger() {
        return actualLogger;
    }

    public boolean isTraceEnabled() {
        return actualLogger.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return actualLogger.isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return actualLogger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return actualLogger.isErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return actualLogger.isInfoEnabled();
    }

    public void trace(String key, Object... args) {
        if (isTraceEnabled()) {
            actualLogger.trace(getLocalizedMessage(key, args));
        }
    }

    public void trace(String key, Throwable e, Object... args) {
        if (isTraceEnabled()) {
            actualLogger.trace(getLocalizedMessage(key, args), e);
        }
    }

    public void debug(String key, Object... args) {
        if (isDebugEnabled()) {
            actualLogger.debug(getLocalizedMessage(key, args));
        }
    }

    public void debug(String key, Throwable e, Object... args) {
        if (isDebugEnabled()) {
            actualLogger.debug(getLocalizedMessage(key, args), e);
        }
    }

    public void warn(String key, Object... args) {
        if (isWarnEnabled()) {
            actualLogger.warn(getLocalizedMessage(key, args));
        }
    }

    public void warn(String key, Throwable e, Object... args) {
        if (isWarnEnabled()) {
            actualLogger.warn(getLocalizedMessage(key, args), e);
        }
    }

    public void error(String key, Object... args) {
        if (isErrorEnabled()) {
            actualLogger.error(getLocalizedMessage(key, args));
        }
    }

    public void error(String key, Throwable e, Object... args) {
        if (isErrorEnabled()) {
            actualLogger.error(getLocalizedMessage(key, args), e);
        }
    }

    public void info(String key, Throwable e, Object... args) {
        if (isInfoEnabled()) {
            actualLogger.info(getLocalizedMessage(key, args), e);
        }
    }

    public void info(String key, Object... args) {
        if (isInfoEnabled()) {
            actualLogger.info(getLocalizedMessage(key, args));
        }
    }
    protected String getLocalizedMessage(String key, Object... args) {
        return I10nUtils.getLocalizedMessage(resourceBundle, key, args);
    }

}
