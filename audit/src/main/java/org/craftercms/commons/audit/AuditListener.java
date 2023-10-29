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

package org.craftercms.commons.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * Ebus Listener to create the Audit entries.
 *
 * @author Carlos Ortiz.
 */
public class AuditListener {

    private AuditService auditService;
    private Logger log = LoggerFactory.getLogger(AuditListener.class);

    @SuppressWarnings("unchecked") // cortiz, OK raw is data is needed.
    @EventListener
    public void onAudit(final AuditModel auditModel) {
        log.debug("Auditing {}", auditModel);
        auditService.audit(auditModel);
    }

    public void setAuditService(final AuditService auditService) {
        this.auditService = auditService;
    }
}
