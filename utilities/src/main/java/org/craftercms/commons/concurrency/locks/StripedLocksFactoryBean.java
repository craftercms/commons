package org.craftercms.commons.concurrency.locks;

import com.google.common.util.concurrent.Striped;
import org.springframework.beans.factory.FactoryBean;

import java.util.concurrent.locks.Lock;

/**
 * A Spring {@link FactoryBean} that creates instances of
 * {@link Striped}{@code <Lock>} with a configurable number of stripes.
 *
 * <h2>What are Striped Locks?</h2>
 * Striped locks are a concurrency pattern that provides a fixed set of lock
 * instances (called <i>stripes</i>) which are shared among many keys. Instead
 * of creating one lock per key (which can be memory-heavy) or using a single
 * global lock (which causes high contention), a striped lock implementation
 * assigns each key to one of the available stripes based on its hash. This
 * balances the trade-off between memory usage and concurrency:
 * <ul>
 *   <li>Keys that hash to the same stripe will contend on the same lock.</li>
 *   <li>Keys that hash to different stripes can be locked independently,
 *       enabling higher concurrency.</li>
 *   <li>The number of stripes is fixed and determines the maximum degree
 *       of parallelism.</li>
 * </ul>
 *
 * <p>
 * This pattern is especially useful in caching, resource coordination, and
 * situations where you need fine-grained synchronization across many keys
 * but want to avoid unbounded lock creation.
 * </p>
 *
 * <h2>Factory Behavior</h2>
 * This factory can operate in two modes:
 * <ul>
 *   <li><b>Singleton mode:</b> The same {@code Striped<Lock>} instance is
 *       returned every time. Useful if you want one central lock manager
 *       shared application-wide.</li>
 *   <li><b>Prototype mode:</b> A new {@code Striped<Lock>} instance is
 *       created for each request. Useful when each consumer should have
 *       an independent set of striped locks.</li>
 * </ul>
 *
 * <h2>Configuration Parameters</h2>
 * <ul>
 *   <li>{@code stripes} – number of stripes (must be positive).</li>
 *   <li>{@code useLazyWeakLocks} – whether to use lazy weak locks
 *       (locks created on demand and weakly referenced) or eager strong
 *       locks (all created upfront and strongly referenced).</li>
 *   <li>{@code singleton} – whether the factory should return a singleton
 *       or create new instances each time.</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * // Singleton striped locks (shared across the app)
 * @Bean
 * public Striped<Lock> stripedLocksSingleton() {
 *     return new StripedLocksFactoryBean(64, false, true).getObject();
 * }
 *
 * // Non-singleton striped locks (each request gets a new instance)
 * @Bean
 * @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
 * public Striped<Lock> stripedLocksPrototype() {
 *     return new StripedLocksFactoryBean(64, true, false).getObject();
 * }
 * }</pre>
 */
public class StripedLocksFactoryBean implements FactoryBean<Striped<Lock>> {

	private final int stripes;
	private final boolean useLazyWeakLocks;
	private final boolean singleton;

	private volatile Striped<Lock> singletonInstance;

	/**
	 * Creates a new {@code StripedLocksFactoryBean}.
	 *
	 * @param stripes          the number of stripes (must be positive)
	 * @param useLazyWeakLocks if true, uses lazy weak locks; otherwise, uses eager strong locks
	 * @param singleton        if true, a single {@link Striped}{@code <Lock>} instance is reused;
	 *                         if false, a new instance is created each time
	 * @throws IllegalArgumentException if {@code stripes} is less than 1
	 */
	public StripedLocksFactoryBean(int stripes, boolean useLazyWeakLocks, boolean singleton) {
		if (stripes < 1) {
			throw new IllegalArgumentException("Stripes must be at least 1");
		}
		this.stripes = stripes;
		this.useLazyWeakLocks = useLazyWeakLocks;
		this.singleton = singleton;
	}

	/**
	 * Returns a {@link Striped}{@code <Lock>} instance.
	 * <ul>
	 *   <li>If {@code singleton} is true, the same instance is returned each time.</li>
	 *   <li>If {@code singleton} is false, a new instance is created on every call.</li>
	 * </ul>
	 *
	 * @return a striped lock instance
	 */
	@Override
	public Striped<Lock> getObject() {
		if (singleton) {
			if (singletonInstance == null) {
				synchronized (this) {
					if (singletonInstance == null) {
						singletonInstance = createStripedLocks();
					}
				}
			}
			return singletonInstance;
		} else {
			return createStripedLocks();
		}
	}

	/**
	 * Returns the type of object created by this factory.
	 *
	 * @return {@code Striped.class}
	 */
	@Override
	public Class<?> getObjectType() {
		return Striped.class;
	}

	/**
	 * Indicates whether this factory produces a singleton or not,
	 * based on the {@code singleton} flag.
	 *
	 * @return {@code true} if producing a singleton; {@code false} otherwise
	 */
	@Override
	public boolean isSingleton() {
		return singleton;
	}

	private Striped<Lock> createStripedLocks() {
		return useLazyWeakLocks ? Striped.lazyWeakLock(stripes) : Striped.lock(stripes);
	}

}
