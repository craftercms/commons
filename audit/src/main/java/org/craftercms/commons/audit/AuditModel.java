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
import java.util.UUID;

/**
* Defines the minimum attributes needed in a generic audit entry.
* @author Carlos Ortiz .
**/
public abstract class AuditModel {

    /**
     * Date when the audit was entry.
     */
    protected Date auditDate;
    /**
     * Id of the audit entry.
     */
    protected String id;

    /**
     * Payload of the audit.
     */
    protected Object payload;

    protected AuditModel() {
        this.id= UUID.randomUUID().toString();
        this.setAuditDate(new Date());
    }

    /**
     * Gets Audit entry Date.
     *
     * @return Audit entry date.
     */
    public Date getAuditDate() {
        return auditDate;
    }

    /**
     * Sets audit entry date.
     *
     * @param auditDate Audit Date.
     */
    protected void setAuditDate(final Date auditDate) {
        this.auditDate = auditDate;
    }


    /**
     * Gets  Id of the Audit Entry.
     *
     * @return Id of the Audit Entry.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets  Id of the Audit Entry.
     *
     * @param id Id of the Audit Entry.
     */
    protected void setId(final String id) {
        this.id = id;
    }

    /**
     * Gets Payload of the audit.
     *
     * @return Payload of the audit.
     */
    public Object getPayload() {
        return payload;
    }

    /**
     * Sets payload of the audit.
     *
     * @param payload payload of the audit.
     */
    protected void setPayload(final Object payload) {
        this.payload = payload;
    }
}
