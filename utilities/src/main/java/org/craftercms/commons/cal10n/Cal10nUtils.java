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
import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLoggerFactory;

import java.util.Locale;

/**
 * Utility methods and constants for CALI0N.
 *
 * @author avasquez
 */
public abstract class Cal10nUtils {

    public static final IMessageConveyor DEFAULT_MESSAGE_CONVEYOR = new MessageConveyor(Locale.getDefault());
    public static final LocLoggerFactory DEFAULT_LOC_LOGGER_FACTORY = new LocLoggerFactory(DEFAULT_MESSAGE_CONVEYOR);

    private Cal10nUtils() {
    }

}
