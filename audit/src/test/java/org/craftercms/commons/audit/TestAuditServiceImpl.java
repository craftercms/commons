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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

/**
 * Simple "In memory" Audit Service impl for testing only.
 */
@SuppressWarnings("unchecked")
public class TestAuditServiceImpl<T extends AuditModel> extends AuditService<T> {

    HashMap<String, T> memoryPersistence;

    public TestAuditServiceImpl() {
        memoryPersistence = new HashMap<>();
    }


    public void clear() {
        memoryPersistence.clear();
    }

    public int countAuditLogs() {
        return memoryPersistence.keySet().size();
    }

    @Override
    public T getAuditLog(final String id) {
        return memoryPersistence.get(id);
    }

    @Override
    protected void persistAudit(final T auditModel) {
        if (memoryPersistence.containsKey(auditModel.getId())) {
            throw new RuntimeException("Already save");
        }
        memoryPersistence.put(auditModel.getId(), auditModel);
    }

    @Override
    public void deleteAudits(final List auditId) {
        for (Object o : auditId) {
             memoryPersistence.remove(o);
        }
    }

    @Override
    public List<T> getAuditLogs(final Date from) {
        return getAuditLogs(from, new Date());
    }

    @Override
    public List<T> getAuditLogs(final Date from, final Date to) {
        if(memoryPersistence.values()==null) {
            return null;
        }
        List<T> valueSet = new ArrayList(memoryPersistence.values());
        CollectionUtils.filter(valueSet, new Predicate<Object>() {
            @Override
            public boolean evaluate(final Object object) {
                if (object instanceof AuditModel) {
                    final Date auditDate = ((AuditModel)object).getAuditDate();
                    return (auditDate.before(from)) || (auditDate.after(from) && auditDate.before(to));
                }
                return false;
            }
        });
        return valueSet;
    }
}
