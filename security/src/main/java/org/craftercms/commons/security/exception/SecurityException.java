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
package org.craftercms.commons.security.exception;

import java.util.ResourceBundle;

import org.craftercms.commons.i10n.AbstractI10nRuntimeException;

/**
 * {@link java.lang.RuntimeException} version of {@link SecurityException}.
 *
 * @author avasquez
 */
public class SecurityException extends AbstractI10nRuntimeException {

    public static final String BUNDLE_NAME = "crafter.security.messages.errors";

    public SecurityException() {
    }

    public SecurityException(String key, Object... args) {
        super(key, args);
    }

    public SecurityException(String key, Throwable cause, Object... args) {
        super(key, cause, args);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    @Override
    protected ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME);
    }

}
