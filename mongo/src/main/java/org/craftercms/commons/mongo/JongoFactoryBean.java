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
package org.craftercms.commons.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.mongodb.DB;
import com.mongodb.Mongo;

import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.commons.jackson.JacksonUtils;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Creates a Jongo singleton for application wide use.
 *
 * @author avasquez
 */
public class JongoFactoryBean extends AbstractFactoryBean<Jongo> {

    private String dbName;
    private String username;
    private String password;
    private MongoClient mongo;
    private List<JsonSerializer<?>> serializers;
    private Map<Class<?>, JsonDeserializer<?>> deserializers;

    @Required
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Required
    public void setMongo(MongoClient mongoClient) {
        this.mongo = mongoClient;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setSerializers(List<JsonSerializer<?>> serializers) {
        this.serializers = serializers;
    }

    public void setDeserializers(Map<Class<?>, JsonDeserializer<?>> deserializers) {
        this.deserializers = deserializers;
    }

    @Override
    public Class<?> getObjectType() {
        return Jongo.class;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Jongo createInstance() throws Exception {
        DB db = mongo.getDB(dbName);
        JacksonMapper.Builder builder = new JacksonMapper.Builder();
        builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        if (CollectionUtils.isNotEmpty(serializers) || MapUtils.isNotEmpty(deserializers)) {
            builder.registerModule(JacksonUtils.createModule(serializers, deserializers));
        }
        return new Jongo(db, builder.build());
    }

}
