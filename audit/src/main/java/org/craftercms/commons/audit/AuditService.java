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

import java.util.Date;
import java.util.List;

/**
 * Defines  Basic Audit Service.
 * @param <T> Any Object that Extents AuditModel.
 */
public abstract class AuditService<T extends AuditModel> {
    /**
     * Sets date and persist the Audit in the repository.
     *
     * @param auditModel Audit to be save.
     */
    public void audit(final T auditModel) {
        persistAudit(auditModel);
    }

    /**
     * Gets the audit log for its Id.
     * @param id It of the audit log.
     * @return Audit log with the given id, null if not found.
     */
    public abstract T getAuditLog(final String id);

    /**
     * Defines the actual save of the audit to the given audit repository.
     *
     * @param auditModel Audit to be save.
     */
    protected abstract void persistAudit(final T auditModel);

    /**
     * Deletes all audits where its it is in the given List.
     *
     * @param auditId List of audits id to delete.
     */
    protected abstract void deleteAudits(final List<String> auditId);

    /**
     * Returns all Audits starting the given date.
     *
     * @param from Date when the Audit was logged.
     * @return List all audits where logged date is after or the given date.Empty if nothing is found
     */
    public abstract List<T> getAuditLogs(final Date from);

    /**
     * Returns all Audits where logged date is between the given dates.
     *
     * @param from Start Date range.(including)
     * @param to   End of Date range.(including)
     * @return List of audits that were logged between the given date range.Empty if nothing match.
     */
    public abstract List<T> getAuditLogs(final Date from, final Date to);
}
