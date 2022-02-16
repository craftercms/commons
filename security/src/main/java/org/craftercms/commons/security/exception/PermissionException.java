/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
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

/**
 * {@link java.lang.RuntimeException} version of {@link PermissionException}.
 *
 * @author avasquez
 */
public class PermissionException extends SecurityException {

    public PermissionException() {
    }

    public PermissionException(String key, Object... args) {
        super(key, args);
    }

    public PermissionException(String key, Throwable cause, Object... args) {
        super(key, cause, args);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

}
