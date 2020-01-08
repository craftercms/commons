package org.craftercms.commons.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Actual implementation of {@link AbstractWeakKeyBasedLockFactory} that uses {@link ReentrantLock}s.
 *
 * @author avasquez
 * @since 3.1.5
 */
public class WeakKeyBasedReentrantLockFactory extends AbstractWeakKeyBasedLockFactory<ReentrantLock> {

    @Override
    protected ReentrantLock newLock() {
        return new ReentrantLock();
    }

}
