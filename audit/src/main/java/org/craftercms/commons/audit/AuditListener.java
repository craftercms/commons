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

package org.craftercms.commons.audit;

import org.craftercms.commons.ebus.annotations.EListener;
import org.craftercms.commons.ebus.annotations.EventHandler;
import org.craftercms.commons.ebus.annotations.EventSelectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

/**
 * Ebus Listener to create the Audit entries.
 *
 * @author Carlos Ortiz.
 */
@EListener
public class AuditListener {

    private AuditService auditService;
    private Logger log = LoggerFactory.getLogger(AuditListener.class);

    @EventHandler(
        event = Audit.AUDIT_EVENT,
        ebus = "@" + Audit.AUDIT_REACTOR,
        type = EventSelectorType.REGEX)
    @SuppressWarnings("unchecked") // cortiz, OK raw is data is needed.
    public void onAudit(final Event<? extends AuditModel> auditModel) {
        log.debug("Auditing {}", auditModel);
        auditService.audit(auditModel.getData());
    }

    public void setAuditService(final AuditService auditService) {
        this.auditService = auditService;
    }
}
