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
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * <p>Creates a Mongo Client based on a connection String </p>
 * <p>Connection String format <i>host:PORT,[host1:port1],[hostN,portN]</i> the port portion is optional default
 * one <i>27017</i> will be use. </p>
 * <p>If connection String is null or empty or whitespace only, this factory will
 * use as if connection String value is "127.0.0.1:27017" </p>
 * <p><b>Will throw IllegalArgumentException if port number is not valid</b></p>
 */
public class MongoClientFactory extends AbstractFactoryBean<MongoClient> {

    public static final String DEFAULT_MONGO_HOST = "127.0.0.1";
    public static final int DEFAULT_MONGO_PORT = 27017;

    private Logger logger = LoggerFactory.getLogger(MongoClientFactory.class);

    private MongoClientOptions options;
    private String connectionString;
    private String username;
    private String password;


    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }

    @Override
    protected MongoClient createInstance() throws Exception {

        if (StringUtils.isBlank(connectionString)) {
            logger.info("No connection string specified, connecting to {}:{}", connectionString, DEFAULT_MONGO_HOST,
                        DEFAULT_MONGO_PORT);

            return new MongoClient(new ServerAddress(DEFAULT_MONGO_HOST, DEFAULT_MONGO_PORT));
        }

        StringTokenizer st = new StringTokenizer(connectionString, ",");
        List<ServerAddress> addressList = new ArrayList<>();

        while (st.hasMoreElements()) {
            String server = st.nextElement().toString();

            logger.debug("Processing first server found with string {}", server);

            String[] serverAndPort = server.split(":");
            if (serverAndPort.length == 2) {
                logger.debug("Server string defines host {} and port {}", serverAndPort[0], serverAndPort[1]);

                if (StringUtils.isBlank(serverAndPort[0])) {
                    throw new IllegalArgumentException("Given host can't be empty");
                }

                int portNumber = NumberUtils.toInt(serverAndPort[1]);
                if (portNumber == 0) {
                    throw new IllegalArgumentException("Given port number " + portNumber + " is not valid");
                }

                addressList.add(new ServerAddress(serverAndPort[0], portNumber));
            } else if (serverAndPort.length == 1) {
                logger.debug("Server string defines host {} only. Using default port ", serverAndPort[0]);

                if (StringUtils.isBlank(serverAndPort[0])) {
                    throw new IllegalArgumentException("Given host can't be empty");
                }

                addressList.add(new ServerAddress(serverAndPort[0], DEFAULT_MONGO_PORT));
            } else {
                throw new IllegalArgumentException("Given connection string is not valid");
            }
        }

        logger.debug("Creating MongoClient with addresses: {}", addressList);

        if (options != null) {
            return new MongoClient(addressList, options);
        } else {
            return new MongoClient(addressList);
        }
    }

    public void setConnectionString(final String connectionString) {
        this.connectionString = connectionString;
    }

    public void setOptions(final MongoClientOptions options) {
        this.options = options;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
