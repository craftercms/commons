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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Audit Model impl for Testing.
 */
public class TestAuditModel extends AuditModel {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public TestAuditModel() {
        this(new SimpleDateFormat(PATTERN).format(new Date()));
    }

    public TestAuditModel(final String date) {
        try {
            this.auditDate = new SimpleDateFormat(PATTERN).parse(date);
            this.id = UUID.randomUUID().toString();
            this.setPayload(null);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
