package org.craftercms.commons.mongo;

/*
 * Copyright (C) 2007-2013 Crafter Software Corporation.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Spring helper for Create a MongoClient
 */
public class MongoClientOptionsFactory extends AbstractFactoryBean<MongoClientOptions> {

    private static final int PRIMARY_READ_PREFERENCE = 1;
    private static final int NEAREST_READ_PREFERENCE = 2;
    private static final int SECONDARY_READ_PREFERENCE = 3;
    private int readPreference;
    private boolean alwaysUseMBeans;
    private boolean autoConnectRetry;
    private int connectionsPerHost;
    private boolean cursorFinalizerEnabled;
    private int connectTimeout;
    private long maxAutoConnectRetryTime;
    private int maxWaitTime;
    private boolean socketKeepAlive;
    private String writeConcern;
    private int threadsAllowedToBlockForConnectionMultiplier;

    public void setReadPreference(final int readPreference) {
        this.readPreference = readPreference;
    }

    public void setAlwaysUseMBeans(final boolean alwaysUseMBeans) {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }

    public void setAutoConnectRetry(final boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

    public void setConnectionsPerHost(final int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public void setCursorFinalizerEnabled(final boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setMaxAutoConnectRetryTime(final long maxAutoConnectRetryTime) {
        this.maxAutoConnectRetryTime = maxAutoConnectRetryTime;
    }

    public void setMaxWaitTime(final int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setSocketKeepAlive(final boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setWriteConcern(final String writeConcern) {
        this.writeConcern = writeConcern;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(final int
                                                                    threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    @Override
    public Class<?> getObjectType() {
        return MongoClientOptions.class;
    }

    @Override
    protected MongoClientOptions createInstance() throws Exception {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder.alwaysUseMBeans(this.alwaysUseMBeans);
        builder.autoConnectRetry(this.autoConnectRetry);
        builder.connectionsPerHost(this.connectionsPerHost);
        builder.cursorFinalizerEnabled(this.cursorFinalizerEnabled);
        builder.connectTimeout(this.connectTimeout);
        builder.maxAutoConnectRetryTime(this.maxAutoConnectRetryTime);
        builder.maxWaitTime(this.maxWaitTime);


        switch (this.readPreference) {
            case PRIMARY_READ_PREFERENCE:
                builder.readPreference(ReadPreference.primary());
                break;
            case NEAREST_READ_PREFERENCE:
                builder.readPreference(ReadPreference.nearest());
                break;
            case SECONDARY_READ_PREFERENCE:
                builder.readPreference(ReadPreference.secondary());
                break;
            default:
                builder.readPreference(ReadPreference.primary());
                break;
        }
        builder.socketKeepAlive(this.socketKeepAlive);
        builder.writeConcern(WriteConcern.valueOf(this.writeConcern));
        builder.threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier);
        return builder.build();

    }
}