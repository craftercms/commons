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

import java.util.Arrays;

import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * {@link org.craftercms.commons.logging.MethodLogger} using I10n logging.
 *
 * @author avasquez
 */
public class I10nMethodLogger implements MethodLogger {

    private static final I10nLogger logger = new I10nLogger(I10nMethodLogger.class, I10nUtils.DEFAULT_LOGGING_MESSAGE_BUNDLE_NAME);

    public static final String LOG_KEY_ENTRY =      "logging.method.entry";
    public static final String LOG_KEY_EXIT =       "logging.method.exit";
    public static final String LOG_KEY_EXCEPTION =  "logging.method.exception";

    @Override
    public void logEntry(String className, String methodName, Object[] args) {
        logger.trace(LOG_KEY_ENTRY, methodName, className, Arrays.toString(args));
    }

    @Override
    public void logExit(String className, String methodName, Object returnValue) {
        logger.trace(LOG_KEY_EXIT, methodName, className, returnValue);
    }

    @Override
    public void logException(String className, String methodName, Throwable e) {
        logger.debug(LOG_KEY_EXCEPTION, e, methodName, className);
    }

}
