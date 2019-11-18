/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
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
package org.craftercms.commons.file.stores;

import com.amazonaws.services.s3.AmazonS3;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;
import org.craftercms.commons.config.profiles.aws.AbstractAwsProfile;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.ehcache.EhCacheFactoryBean;

import java.util.Collections;

/**
 * Factory class that creates {@code AmazonS3} clients using {@link S3Profile}s. It also manages a pool of clients so
 * instead of creating a new client for the same profile, it will reuse a previous client. Clients that haven't been
 * used for a while will be shutdown.
 *
 * <p>
 *     <strong>WARNING: </strong> the {@link AbstractAwsProfile}s that you pass to
 *     {@link #getClient(AbstractAwsProfile)} should implement equals and hash code methods that take into account
 *     most of the properties, so that the clients are cached correctly (see {@link S3Profile}).
 * </p>
 *
 * @author avasquez
 */
public class S3ClientPoolingFactory extends CacheEventListenerAdapter implements BeanNameAware, InitializingBean,
                                                                                 DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(S3ClientPoolingFactory.class);

    public static final int DEFAULT_CLIENT_TIME_TO_IDLE = 900;

    private String beanName;
    private int clientTimeToIdle;
    private Ehcache pool;

    public S3ClientPoolingFactory() {
        clientTimeToIdle = DEFAULT_CLIENT_TIME_TO_IDLE;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setClientTimeToIdle(int clientTimeToIdle) {
        this.clientTimeToIdle = clientTimeToIdle;
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
        for (Object key : pool.getKeys()) {
            Element element = pool.get(key);
            if (element != null) {
                shutdownClient(element);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        dispose();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        factoryBean.setCacheEventListeners(Collections.singleton(this));
        factoryBean.setBeanName(beanName);
        factoryBean.setTimeToLive(0);
        factoryBean.setTimeToIdle(clientTimeToIdle);
        factoryBean.afterPropertiesSet();

        pool = factoryBean.getObject();
    }

    public AmazonS3 getClient(AbstractAwsProfile profile) {
        Element element = pool.get(profile);
        if (element == null) {
            synchronized (pool) {
                // Check again, just in case the element was added by another concurrent thread
                element = pool.get(profile);
                if (element == null) {
                    logger.info("Creating AmazonS3 client for {}", profile);

                    element = new Element(profile, S3Utils.createClient(profile));
                    pool.put(element);
                }
            }
        }

        return (AmazonS3) element.getObjectValue();
    }

    protected void shutdownClient(Element element) {
        if (element.getObjectValue() instanceof AmazonS3) {
            logger.info("Shutting down AmazonS3 client for {}", element.getObjectKey());

            AmazonS3 client = (AmazonS3) element.getObjectValue();
            client.shutdown();
        }
    }

}
