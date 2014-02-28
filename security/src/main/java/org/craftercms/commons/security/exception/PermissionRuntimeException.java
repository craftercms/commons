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
package org.craftercms.commons.security.exception;

import ch.qos.cal10n.IMessageConveyor;

/**
 * {@link java.lang.RuntimeException} version of {@link org.craftercms.commons.security.exception.PermissionException}.
 *
 * @author avasquez
 */
public class PermissionRuntimeException extends SecurityRuntimeException {

    public PermissionRuntimeException() {
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode) {
        super(errorCode);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, IMessageConveyor messageConveyor) {
        super(errorCode, messageConveyor);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, IMessageConveyor messageConveyor, Object... args) {
        super(errorCode, messageConveyor, args);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }

    public PermissionRuntimeException(PermissionErrorCode errorCode, IMessageConveyor messageConveyor, Throwable cause,
                                      Object... args) {
        super(errorCode, messageConveyor, cause, args);
    }

}