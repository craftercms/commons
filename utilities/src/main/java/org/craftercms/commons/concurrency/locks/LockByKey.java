/*
 * Copyright (C) 2007-2025 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.concurrency.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides per-key locking functionality using {@link ReentrantLock} instances
 * stored in a concurrent map. Each distinct key is associated with its own
 * lock, ensuring that only one thread can execute a critical section for that
 * key at a time.
 *
 * <h2>Memory Management</h2>
 * Each key is associated with a {@link LockWrapper} that contains:
 * <ul>
 *   <li>a {@link ReentrantLock} for mutual exclusion, and</li>
 *   <li>a reference counter ({@link AtomicInteger}) that tracks how many
 *       threads are currently holding or waiting on that lock.</li>
 * </ul>
 * When {@link #unlock(Object)} is called, the counter is decremented, and
 * once it reaches zero the lock is automatically removed from the registry.
 * This ensures that locks for inactive keys are eventually garbage collected,
 * preventing unbounded memory growth.
 *
 * <h2>Concurrency Semantics</h2>
 * <ul>
 *   <li>{@link #lock(Object)} is thread-safe and increments the counter
 *       atomically as part of the {@link ConcurrentHashMap#compute} operation.
 *       This prevents races where a lock could be removed before a thread has
 *       incremented its usage count.</li>
 *   <li>{@link #unlock(Object)} releases the lock and decrements the counter.
 *       If the counter reaches zero, the lock is removed using
 *       {@link ConcurrentHashMap#remove(Object, Object)} to avoid races with
 *       new acquirers.</li>
 *   <li>Reentrancy is supported because {@link ReentrantLock} is reentrant,
 *       but each reentrant call also increments the reference count. It is
 *       the caller's responsibility to invoke {@code unlock(key)} the same
 *       number of times as {@code lock(key)}.</li>
 * </ul>
 *
 * <h2>Error Handling</h2>
 * If {@link #unlock(Object)} is called for a key that has no active lock,
 * an {@link IllegalMonitorStateException} is thrown.
 *
 * <h2>Lock cleanup</h2>
 * This class also tracks a {@code lastUsed} timestamp per lock and supports a
 * background cleanup task that can warn about locks that appear stuck
 * (refCount > 0 but idle for too long) or remove any orphaned entries with
 * refCount == 0.
 *
 * @param <K> the type of key used to identify locks
 */
public class LockByKey<K> implements InitializingBean, DisposableBean {

	private static final long DEFAULT_CLEANUP_INTERVAL_SECS = 60 * 60; // 1 hour
	private static final long DEFAULT_WARN_IDLE_MILLIS = 10 * 60 * 1000; // 5 minutes

	private static final Logger logger = LoggerFactory.getLogger(LockByKey.class);

	/**
	 * Wrapper object holding the actual lock, reference counter,
	 * and last-used timestamp (milliseconds).
	 */
	private static class LockWrapper {
		private final Lock lock;
		private final AtomicInteger refCount;
		private volatile long lastUsedMillis;

		public LockWrapper() {
			this.lock = new ReentrantLock();
			this.refCount = new AtomicInteger(0);
			this.lastUsedMillis = System.currentTimeMillis();
		}

		public void lock() {
			lock.lock();
			lastUsedMillis = System.currentTimeMillis();
		}

		public void unlock() {
			lock.unlock();
			lastUsedMillis = System.currentTimeMillis();
		}

		public int incrementRefCount() {
			return refCount.incrementAndGet();
		}

		public int decrementRefCount() {
			return refCount.decrementAndGet();
		}
	}

	/** Registry mapping keys to their lock wrappers. */
	private final ConcurrentHashMap<K, LockWrapper> locks;
	/** Interval between cleanup runs. */
	private final long cleanupIntervalSecs;
	/** Threshold for warning about idle locks. */
	private final long warnIdleMillis;
	/** Optional scheduler for background cleanup. */
	private final ScheduledExecutorService scheduler;

	public LockByKey() {
		this(DEFAULT_CLEANUP_INTERVAL_SECS, DEFAULT_WARN_IDLE_MILLIS);
	}

	/** Creates a new per-key lock manager. */
	public LockByKey(long cleanupIntervalSecs, long warnIdleMillis) {
		this.locks = new ConcurrentHashMap<>();
		this.cleanupIntervalSecs = cleanupIntervalSecs > 0 ? cleanupIntervalSecs : DEFAULT_CLEANUP_INTERVAL_SECS;
		this.warnIdleMillis = warnIdleMillis > 0 ? warnIdleMillis : DEFAULT_WARN_IDLE_MILLIS;
		this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "LockByKey-Cleanup-" + Integer.toHexString(System.identityHashCode(this)));
			t.setDaemon(true);
			return t;
		});
	}

	/**
	 * Starts a background cleanup job that runs at the given interval.
	 */
	@Override
	public void afterPropertiesSet() {
		scheduler.scheduleAtFixedRate(this::cleanup, cleanupIntervalSecs, cleanupIntervalSecs, TimeUnit.SECONDS);
	}

	/**
	 * Stops the background cleanup job.
	 */
	@Override
	public void destroy() {
		scheduler.shutdownNow();
	}

	/**
	 * Acquires the lock associated with the given key.
	 * If no lock exists yet for the key, a new one is created.
	 *
	 * @param key the key identifying the lock
	 */
	public void lock(K key) {
		LockWrapper lockWrapper = locks.compute(key, (k, v) -> {
			if (v == null) {
				logger.trace("Creating new lock for key: {}", k); // was debug → now trace
				v = new LockWrapper();
			}
			v.incrementRefCount();
			return v;
		});
		logger.debug("Acquiring lock for key: {} (refCount={})", key, lockWrapper.refCount.get());
		lockWrapper.lock();
	}

	/**
	 * Releases the lock associated with the given key.
	 * <p>
	 * This method is implemented using {@link ConcurrentHashMap#compute(Object, java.util.function.BiFunction)},
	 * so that the decrement of the reference count and the possible removal of the lock
	 * happen atomically under the CHM bin lock. This avoids a race where another thread
	 * could acquire the same key between decrement and remove, leading to premature removal.
	 * </p>
	 *
	 * @param key the key identifying the lock
	 * @throws IllegalMonitorStateException if no lock exists for the key
	 *         (e.g., if {@code unlock(key)} is called without a matching
	 *         {@code lock(key)}).
	 */
	public void unlock(K key) {
		locks.compute(key, (k, lockWrapper) -> {
			if (lockWrapper == null) {
				throw new IllegalMonitorStateException("Unlock attempted for unknown key: " + key);
			}

			// Let ReentrantLock enforce correctness of ownership
			lockWrapper.unlock(); // may throw IllegalMonitorStateException

			// Only decrement if unlock succeeded
			int refCount = lockWrapper.decrementRefCount();
			logger.debug("Released lock for key: {} (refCount={})", key, refCount);

			if (refCount == 0) {
				logger.trace("Removing lock for key: {}", key);
				return null; // safely remove
			}
			return lockWrapper;
		});
	}

	/**
	 * Cleanup logic: removes any zero-refCount locks that somehow weren’t cleaned
	 * up and logs warnings for locks that look "stuck".
	 */
	private void cleanup() {
		logger.debug("Running lock cleanup");
		try {
			long now = System.currentTimeMillis();
			for (K key : locks.keySet()) {
				locks.compute(key, (k, lockWrapper) -> {
					if (lockWrapper == null) {
						return null; // already removed
					}

					int count = lockWrapper.refCount.get();
					long idle = now - lockWrapper.lastUsedMillis;
					if (count == 0) {
						logger.warn("Background cleanup removed unused lock for key {}", key);
						return null; // atomically remove
					} else if (idle > warnIdleMillis) {
						logger.warn("Lock for key {} may be leaked: refCount={}, idleFor={} ms", key, count, idle);
					}
					return lockWrapper;
				});
			}
		} catch (Exception e) {
			logger.error("Error during lock cleanup", e);
		}
	}
}
