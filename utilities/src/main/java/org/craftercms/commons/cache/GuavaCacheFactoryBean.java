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
package org.craftercms.commons.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.time.Duration;

/**
 * Spring {@link org.springframework.beans.factory.FactoryBean} used to create Guava caches as beans.
 *
 * @author joseross
 * @since 4.0.0
 */
public class GuavaCacheFactoryBean<K, V>  extends AbstractFactoryBean<Cache<K, V>> implements BeanNameAware {

    private static final Logger logger = LoggerFactory.getLogger(GuavaCacheFactoryBean.class);

    private String beanName;

    private boolean recordStats = false;

    private long maxSize = -1;

    private long timeToLive = -1;

    private long timeToIdle = -1;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    public void setRecordStats(boolean recordStats) {
        this.recordStats = recordStats;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setTimeToIdle(long timeToIdle) {
        this.timeToIdle = timeToIdle;
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    protected Cache<K, V> createInstance() {
        logger.info("Creating cache for bean {}", beanName);
        var cacheBuilder = CacheBuilder.newBuilder();
        if (recordStats) {
            cacheBuilder.recordStats();
        }
        if (maxSize >= 0) {
            cacheBuilder.maximumSize(maxSize);
        }
        if (timeToLive >= 0) {
            cacheBuilder.expireAfterWrite(Duration.ofSeconds(timeToLive));
        }
        if (timeToIdle >= 0) {
            cacheBuilder.expireAfterAccess(Duration.ofSeconds(timeToIdle));
        }
        return cacheBuilder.build();
    }

    @Override
    protected void destroyInstance(Cache<K, V> instance) {
        logger.info("Cleaning up cache for bean {}", beanName);
        instance.invalidateAll();
        instance.cleanUp();
    }

}
