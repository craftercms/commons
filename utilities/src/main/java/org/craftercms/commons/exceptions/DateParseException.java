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
package org.craftercms.commons.exceptions;

import java.util.ResourceBundle;

import org.craftercms.commons.i10n.AbstractI10nRuntimeException;
import org.craftercms.commons.i10n.I10nUtils;

/**
 * Exception thrown when there's an error while parsing a date.
 *
 * @author avasquez
 */
public class DateParseException extends AbstractI10nRuntimeException {

    public static final String KEY = "converters.stringToDate.parseFailed";

    public DateParseException(String dateStr, String pattern, Throwable cause) {
        super(KEY, cause, dateStr, pattern);
    }

    @Override
    protected ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(I10nUtils.DEFAULT_ERROR_MESSAGE_BUNDLE_NAME);
    }

}
