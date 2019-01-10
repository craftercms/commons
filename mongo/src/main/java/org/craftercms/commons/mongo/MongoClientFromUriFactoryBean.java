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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * {@link org.springframework.beans.factory.FactoryBean} that besides creating a {@link MongoClient} from a Mongo URI,
 * closes the client when it's destroyed.
 *
 * @author avasquez
 *
 * @see <a href="http://api.mongodb.org/java/current/com/mongodb/MongoClientURI.html">MongoClientURI</a>
 */
public class MongoClientFromUriFactoryBean extends AbstractFactoryBean<MongoClient> {

    protected String uri;

    @Required
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }

    @Override
    protected MongoClient createInstance() throws Exception {
        final MongoClientURI client = new MongoClientURI(uri);
        logger.debug("Connecting to :"+ client.getDatabase());
        return new MongoClient(client);
    }

    @Override
    protected void destroyInstance(MongoClient mongoClient) throws Exception {
        mongoClient.close();
    }

}