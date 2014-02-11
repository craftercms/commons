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

/**
 * Thrown if the subject condition of a permission is invalid.
 *
 * @author avasquez
 */
public class InvalidSubjectConditionException extends PermissionException {

    public InvalidSubjectConditionException() {
    }

    public InvalidSubjectConditionException(String message) {
        super(message);
    }

    public InvalidSubjectConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSubjectConditionException(Throwable cause) {
        super(cause);
    }

}
