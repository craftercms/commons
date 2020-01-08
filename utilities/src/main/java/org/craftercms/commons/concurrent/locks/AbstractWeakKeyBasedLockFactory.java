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
