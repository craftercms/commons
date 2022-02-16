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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:cache-context.xml")
public class GuavaCacheFactoryBeanTest {

    public static final String CACHE_KEY = "myKey";

    public static final String CACHE_VALUE = "myValue";

    @Autowired
    Cache<String, String> maxSizeCache;

    @Autowired
    Cache<String, String> timeToLiveCache;

    @Autowired
    Cache<String, String> timeToIdleCache;

    @Before
    public void setUp() {
        maxSizeCache.invalidateAll();
        timeToLiveCache.invalidateAll();
        timeToIdleCache.invalidateAll();
    }

    @Test
    public void maxSizeTest() {
        Stream.generate(UUID::randomUUID).limit(10).map(UUID::toString).forEach(uuid -> maxSizeCache.put(uuid, uuid));

        assertThat(maxSizeCache.size(), equalTo(5L));
    }

    @Test
    public void timeToLiveTest() throws InterruptedException {
        timeToIdleCache.put(CACHE_KEY, CACHE_VALUE);

        var cached = timeToIdleCache.getIfPresent(CACHE_KEY);
        assertThat(cached, equalTo(CACHE_VALUE));

        sleep(2000);

        cached = timeToIdleCache.getIfPresent(CACHE_KEY);
        assertThat(cached, nullValue());
    }

    @Test
    public void timeToIdleTest() throws InterruptedException {
        timeToLiveCache.put(CACHE_KEY, CACHE_VALUE);

        sleep(2000);

        var cached = timeToLiveCache.getIfPresent(CACHE_KEY);
        assertThat(cached, nullValue());
    }

}
