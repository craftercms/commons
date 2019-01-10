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

import org.apache.commons.lang3.StringUtils;

/**
 * Root exception for any {@link java.lang.Exception} that wants to be localized.
 *
 * @author avasquez
 */
public abstract class AbstractI10nException extends Exception {

    private static final long serialVersionUID = 5633541877690995389L;
    protected String key;
    protected Object[] args;

    public AbstractI10nException() {
    }

    public AbstractI10nException(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    public AbstractI10nException(String key, Throwable cause, Object... args) {
        super(cause);

        this.key = key;
        this.args = args;
    }

    public AbstractI10nException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNotEmpty(key)) {
            return I10nUtils.getLocalizedMessage(getResourceBundle(), key, args);
        } else {
            return null;
        }
    }

    protected abstract ResourceBundle getResourceBundle();

}
