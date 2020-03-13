/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;
import org.craftercms.commons.config.profiles.aws.AbstractAwsProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.ehcache.EhCacheFactoryBean;

import java.util.Collections;
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
        extends CacheEventListenerAdapter implements BeanNameAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(S3ClientCachingFactory.class);

    public static final int DEFAULT_SHUTDOWN_CLIENT_AFTER_IDLE_SECS = 900;

    private String beanName;
    private int shutdownClientAfterIdleSecs;
    private Ehcache cache;
    private ScheduledExecutorService evictionService;

    public AbstractAwsClientCachingFactory() {
        shutdownClientAfterIdleSecs = DEFAULT_SHUTDOWN_CLIENT_AFTER_IDLE_SECS;
        evictionService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setShutdownClientAfterIdleSecs(int shutdownClientAfterIdleSecs) {
        this.shutdownClientAfterIdleSecs = shutdownClientAfterIdleSecs;
    }

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        shutdownClient(element);
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        shutdownClient(element);
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        shutdownClient(element);
    }

    @Override
    public void dispose() {
        logger.info("Shutting down {}", getClass().getSimpleName());

        evictionService.shutdownNow();

        for (Object key : cache.getKeys()) {
            Element element = cache.get(key);
            if (element != null) {
                shutdownClient(element);
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        factoryBean.setCacheEventListeners(Collections.singleton(this));
        factoryBean.setBeanName(beanName);
        factoryBean.setTimeToLive(0);
        factoryBean.setTimeToIdle(shutdownClientAfterIdleSecs);
        factoryBean.afterPropertiesSet();

        cache = factoryBean.getObject();

        // Schedule eviction of expired items every minute
        evictionService.scheduleAtFixedRate(cache::evictExpiredElements, 1, 1, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    public C getClient(P profile) {
        Element element = cache.get(profile);
        if (element == null) {
            synchronized (cache) {
                // Check again, just in case the element was added by another concurrent thread
                element = cache.get(profile);
                if (element == null) {
                    logger.info("Creating client for {}", profile);

                    element = new Element(profile, createClient(profile));
                    cache.put(element);
                }
            }
        }

        return (C) element.getObjectValue();
    }

    protected void shutdownClient(Element element) {
        if (element.getObjectValue() instanceof AmazonS3) {
            logger.info("Shutting down AWS client for {}", element.getObjectKey());

            AmazonS3 client = (AmazonS3) element.getObjectValue();
            client.shutdown();
        }
    }

    protected abstract C createClient(P profile);

}
