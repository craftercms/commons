/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.concurrent.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Abstract implementation of {@link KeyBasedLockFactory} that uses a {@link WeakHashMap} to store the locks. When
 * code using a lock is done, and there are no more references to the lock, the lock is automatically removed from
 * the map.
 *
 * @param <L> the class of the lock
 *
 * @author avasquez
 * @since 3.1.5
 */
public abstract class AbstractWeakKeyBasedLockFactory<L> implements KeyBasedLockFactory<L> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWeakKeyBasedLockFactory.class);

    protected Map<Object, L> locks;

    protected AbstractWeakKeyBasedLockFactory() {
        locks = new WeakHashMap<>();
    }

    @Override
    public synchronized L getLock(Object key) {
        logger.debug("Getting lock for key '{}'", key);

        L lock = locks.get(key);
        if (lock == null) {
            lock = newLock();
            locks.put(key, lock);
        }

        return lock;
    }

    protected abstract L newLock();

}
