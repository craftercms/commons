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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Audit Reaper Service. this class is meant to be run periodically.
 *
 * @author Carlos Ortiz.
 */
public class AuditReaper {

    /**
     * Audit Service implementation.
     */
    protected AuditService<? extends AuditModel> auditService;
    /**
     * Maximum days to keep in the log.
     */
    protected int maxAuditAllowedDays;

    /**
     * Logger of the class.
     */
    private Logger log = LoggerFactory.getLogger(AuditReaper.class);

    /**
     * <p>Search all logs to be deleted and send there id's to the audit service to be deleted.</p>
     * <p>If maximum days is set to  -1 nothing will deleted, 0 it delete all audits daily.</p>
     */
    public void scythe() {
        log.debug("Starting Audit Cleanup");
        if (maxAuditAllowedDays >= 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, maxAuditAllowedDays * -1);
            final Date since = new Date(calendar.getTimeInMillis());
            final List<? extends AuditModel> toDelete = auditService.getAuditLogs(since, new Date());
            if (toDelete != null) {
                final List<String> idstoDel = getIdList(toDelete);
                log.info("Deleting {} audit entries ", idstoDel.size());
                if (toDelete != null) {
                    auditService.deleteAudits(idstoDel);
                }
                log.info("Going to sleep now");
            } else {
                log.info("AuditService return null when ask to give audits with in this range {} {}", since,
                    new Date());
            }
        } else {
            log.info("Skipping scythe maxAuditAllowedDays is set to infinity");
        }
    }

    /**
     * Gets the list of Id's of the given List of Models.
     *
     * @param toDelete List of AuditModels to get there Ids.
     * @return A List of Ids.
     */
    private List<String> getIdList(final List<? extends AuditModel> toDelete) {
        List<String> ids = new ArrayList<>(toDelete.size());
        for (AuditModel auditModel : toDelete) {
            ids.add(auditModel.getId());
        }
        return ids;
    }

    public void setAuditService(final AuditService<?> auditService) {
        this.auditService = auditService;
    }

    public void setMaxAuditAllowedDays(final int maxAuditAllowedDays) {
        this.maxAuditAllowedDays = maxAuditAllowedDays;
    }
}
