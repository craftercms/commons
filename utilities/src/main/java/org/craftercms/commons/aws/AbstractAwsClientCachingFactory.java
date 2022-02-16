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
package org.craftercms.commons.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.craftercms.commons.config.profiles.aws.AbstractAwsProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Factory class that creates AWS clients based on {@link AbstractAwsProfile}s. It also caches the clients so
 * instead of creating a new client for the same profile, it will reuse a previous client. Clients that haven't been
 * used for a while will be evicted from the cache and shutdown.
 *
 * <p>
 *     <strong>WARNING: </strong> the {@link AbstractAwsProfile}s that you pass to
 *     {@link #getClient(AbstractAwsProfile)} should implement equals and hash code methods that take into account
 *     most of the properties, so that the clients are cached correctly.
 * </p>
 *
 * @author avasquez
 */
public abstract  class AbstractAwsClientCachingFactory<P extends AbstractAwsProfile, C>
        implements InitializingBean, DisposableBean, RemovalListener<P, C> {

    private static final Logger logger = LoggerFactory.getLogger(S3ClientCachingFactory.class);

    public static final int DEFAULT_SHUTDOWN_CLIENT_AFTER_IDLE_SECS = 900;

    private int shutdownClientAfterIdleSecs;
    private Cache<P, C> cache;
    private ScheduledExecutorService evictionService;

    public AbstractAwsClientCachingFactory() {
        shutdownClientAfterIdleSecs = DEFAULT_SHUTDOWN_CLIENT_AFTER_IDLE_SECS;
        evictionService = Executors.newSingleThreadScheduledExecutor();
    }

    public void setShutdownClientAfterIdleSecs(int shutdownClientAfterIdleSecs) {
        this.shutdownClientAfterIdleSecs = shutdownClientAfterIdleSecs;
    }

    @Override
    public void onRemoval(RemovalNotification<P, C> notification) {
        shutdownClient(notification);
    }

    @Override
    public void destroy() {
        logger.info("Shutting down {}", getClass().getSimpleName());

        evictionService.shutdownNow();

        cache.invalidateAll();
        cache.cleanUp();
    }

    @Override
    public void afterPropertiesSet() {
        cache = CacheBuilder.newBuilder()
                .removalListener(this)
                .expireAfterAccess(shutdownClientAfterIdleSecs, TimeUnit.SECONDS)
                .build();

        // Schedule eviction of expired items every minute
        evictionService.scheduleAtFixedRate(cache::cleanUp, 1, 1, TimeUnit.MINUTES);
    }

    public C getClient(P profile) {
        var client = cache.getIfPresent(profile);
        if (client == null) {
            synchronized (cache) {
                // Check again, just in case the element was added by another concurrent thread
                client = cache.getIfPresent(profile);
                if (client == null) {
                    logger.info("Creating client for {}", profile);

                    client = createClient(profile);
                    cache.put(profile, client);
                }
            }
        }

        return client;
    }

    protected void shutdownClient(RemovalNotification<P, C> notification) {
        if (notification.getValue() instanceof AmazonS3) {
            logger.info("Shutting down AWS client for {}", notification.getKey());

            AmazonS3 client = (AmazonS3) notification.getValue();
            client.shutdown();
        }
    }

    protected abstract C createClient(P profile);

}
