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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/audit-test-context.xml"})
public class TestSpringCtx {

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    private TestAuditServiceImpl<TestAuditModel> auditService;

    @Test
    public void test1ContextIsInit() {
        // Nothing just make sure Spring ctx goes up.
    }

    @Test
    public void testAuditListener() {
        auditService.clear();
        TestAuditModel testAuditModel = new TestAuditModel();
        TestAuditModel testAuditModelNotSended = new TestAuditModel();
        applicationContext.publishEvent(testAuditModel);
        assertEquals(1, auditService.countAuditLogs());
        assertNotNull(auditService.getAuditLog(testAuditModel.getId()));
        assertNull(auditService.getAuditLog(testAuditModelNotSended.getId()));
    }
}
