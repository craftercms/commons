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
package org.craftercms.commons.cal10n;

import ch.qos.cal10n.IMessageConveyor;

/**
 * Root exception for any {@link java.lang.Exception} that wants to be localized. Uses CAL10N for localization.
 *
 * @author avasquez
 */
public class LocalizedException extends Exception {

    protected Enum<?> errorCode;
    protected IMessageConveyor messageConveyor;
    protected Object[] args;

    public LocalizedException() {
    }

    public LocalizedException(Enum<?> errorCode) {
        this.errorCode = errorCode;
        this.messageConveyor = DefaultMessageConveyor.INSTANCE;
    }

    public LocalizedException(Enum<?> errorCode, Object... args) {
        this.errorCode = errorCode;
        this.messageConveyor = DefaultMessageConveyor.INSTANCE;
        this.args = args;
    }

    public LocalizedException(Enum<?> errorCode, IMessageConveyor messageConveyor) {
        this.errorCode = errorCode;
        this.messageConveyor = messageConveyor;
    }

    public LocalizedException(Enum<?> errorCode, IMessageConveyor messageConveyor, Object... args) {
        this.errorCode = errorCode;
        this.messageConveyor = messageConveyor;
        this.args = args;
    }

    public LocalizedException(Enum<?> errorCode, Throwable cause) {
        super(cause);

        this.errorCode = errorCode;
        this.messageConveyor = DefaultMessageConveyor.INSTANCE;
    }

    public LocalizedException(Enum<?> errorCode, Throwable cause, Object... args) {
        super(cause);

        this.errorCode = errorCode;
        this.messageConveyor = DefaultMessageConveyor.INSTANCE;
        this.args = args;
    }

    public LocalizedException(Enum<?> errorCode, IMessageConveyor messageConveyor, Throwable cause, Object... args) {
        super(cause);

        this.errorCode = errorCode;
        this.messageConveyor = messageConveyor;
        this.args = args;
    }

    @Override
    public String getMessage() {
        if (errorCode != null) {
            return messageConveyor.getMessage(errorCode, args);
        } else {
            return null;
        }
    }

}
