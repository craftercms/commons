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
