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
package org.craftercms.commons.security.logging;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Message codes for the security module logging.
 *
 * @author avasquez
 */
@BaseName("crafter.security.messages.logging")
@LocaleData(@Locale("en"))
public enum SecurityLogMessage {
    PROTECTED_METHOD_INTERCEPTED,
    PROTECTED_METHOD_INTERCEPTED_NO_SEC_OBJ,
    RESOLVING_GLOBAL_PERMISSION,
    RESOLVING_PERMISSION,
    EVALUATING_PERMISSION;
}