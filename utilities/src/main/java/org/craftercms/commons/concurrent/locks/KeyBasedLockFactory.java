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

/**
 * Factory for getting locks that can be used for concurrent thread synchronization based on keys.
 *
 * @param <L> the lock class. It's left open so that any {@link java.util.concurrent.locks.Lock},
 *            {@link java.util.concurrent.Semaphore} or {@link java.util.concurrent.locks.ReadWriteLock}
 *            implementation can be used.
 *
 * @author avasquez
 * @since 3.1.5
 */
public interface KeyBasedLockFactory<L> {

    /**
     * Returns a lock for the specified key. Implementations will normally do the following: when the first thread
     * calls this method with a key, a new lock is created and returned. When a second, third and so on concurrent
     * thread tries to get a lock for the same key, the first lock is returned, thus allowing synchronization based
     * on the key.
     *
     * @param key the key associated with the lock
     *
     * @return the lock
     */
    L getLock(Object key);

}
