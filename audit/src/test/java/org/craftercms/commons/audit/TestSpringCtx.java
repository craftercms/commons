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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/audit-test-context.xml"})
public class TestSpringCtx {

    @Autowired()
    private Reactor auditReactor;

    @Autowired
    private TestAuditServiceImpl auditService;

    @Test
    public void test1ContextIsInit() {
        // Nothing just make sure Spring ctx goes up.
    }

    @Test
    public void testAuditListener() throws InterruptedException {
        auditService.clear();
        TestAuditModel testAuditModel = new TestAuditModel();
        TestAuditModel testAuditModelNotSended = new TestAuditModel();
        EventCallback eventCallback = new EventCallback();
        auditReactor.notify(Audit.AUDIT_EVENT, Event.wrap(testAuditModel), eventCallback);
        while (!eventCallback.isCompleted()) {
            Thread.sleep(1);
        }
        assertEquals(1, auditService.countAuditLogs());
        assertNotNull(auditService.getAuditLog(testAuditModel.getId()));
        assertNull(auditService.getAuditLog(testAuditModelNotSended.getId()));
    }

    class EventCallback implements Consumer<Event<? extends AuditModel>> {

        private boolean completed = false;

        @Override
        public void accept(Event<? extends AuditModel> event) {
            completed = true;
        }

        public boolean isCompleted() {
            return completed;
        }
    }
}
