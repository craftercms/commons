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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class AuditReaperTest {

    private TestAuditServiceImpl auditService;
    private AuditReaper reaper;

    @Before
    public void before() {
        auditService = new TestAuditServiceImpl();
        this.reaper = new AuditReaper();
        this.reaper.setAuditService(auditService);

    }


    @Test
    public void testScytheDeleteAndDaysEnd() throws Exception {
        auditService.clear();
        auditService.audit(new TestAuditModel("1984-09-20 12:01:00"));
        auditService.audit(new TestAuditModel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        reaper.setMaxAuditAllowedDays(0);
        reaper.scythe();
        assertEquals(0, auditService.countAuditLogs());
    }

    @Test
    public void testScytheDeleteYearOrLonger() throws Exception {
        auditService.clear();
        auditService.audit(new TestAuditModel("1984-09-20 12:01:00"));
        auditService.audit(new TestAuditModel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        auditService.audit(new TestAuditModel("3001-09-20 12:01:00"));
        reaper.setMaxAuditAllowedDays(365);
        reaper.scythe();
        assertEquals(1, auditService.countAuditLogs());
    }


    @Test
    public void testScytheDeleteNon() throws Exception {
        auditService.clear();
        auditService.audit(new TestAuditModel("1984-09-20 12:01:00"));
        auditService.audit(new TestAuditModel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        auditService.audit(new TestAuditModel("3001-09-20 12:01:00"));
        reaper.setMaxAuditAllowedDays(-1);
        reaper.scythe();
        assertEquals(3, auditService.countAuditLogs());
    }

    @Test
    public void testScytheAuditServiceNull() throws Exception {
        auditService.clear();
        auditService.audit(new TestAuditModel("1984-09-20 12:01:00"));
        auditService.audit(new TestAuditModel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        auditService.audit(new TestAuditModel("3001-09-20 12:01:00"));
        reaper.setMaxAuditAllowedDays(-1);
        reaper.scythe();
        assertEquals(3, auditService.countAuditLogs());
    }

}