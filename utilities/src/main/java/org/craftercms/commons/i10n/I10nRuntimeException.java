/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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

import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;

/**
 * Root runtime exception for any {@link Exception} that wants to be localized.
 *
 * @author avasquez
 */
public abstract class I10nRuntimeException extends RuntimeException {

    protected Object[] args;

    public I10nRuntimeException() {
    }

    public I10nRuntimeException(String key, Object... args) {
        super(key);

        this.args = args;
    }

    public I10nRuntimeException(String key, Throwable cause, Object... args) {
        super(key, cause);

        this.args = args;
    }

    public I10nRuntimeException(Throwable cause) {
        super(cause);
    }

    public I10nRuntimeException(String key, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                                Object... args) {
        super(key, cause, enableSuppression, writableStackTrace);

        this.args = args;
    }

    @Override
    public String getLocalizedMessage() {
        String key = getMessage();
        if (StringUtils.isNotEmpty(key)) {
            return I10nUtils.getLocalizedMessage(getResourceBundle(), key, args);
        } else {
            return null;
        }
    }

    protected abstract ResourceBundle getResourceBundle();

}
