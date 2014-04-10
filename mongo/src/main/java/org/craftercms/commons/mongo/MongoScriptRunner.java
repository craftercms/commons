/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
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

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for running Mongo scripts in JS.
 *
 * @author avasquez
 */
public class MongoScriptRunner {

    private static final Logger logger = LoggerFactory.getLogger(MongoScriptRunner.class);

    private Mongo mongo;
    private String dbName;
    private String username;
    private String password;
    private List<Resource> scriptPaths;
    private boolean runOnInit;

    public MongoScriptRunner() {
        this.runOnInit = true;
    }

    @Required
    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    @Required
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Required
    public void setScriptPaths(List<Resource> scriptPaths) {
        this.scriptPaths = scriptPaths;
    }

    public void setRunOnInit(boolean runOnInit) {
        this.runOnInit = runOnInit;
    }

    @PostConstruct
    public void init() throws MongoDataException {
        if (runOnInit) {
            runScripts();
        }
    }

    public void runScripts() throws MongoDataException {
        DB db = getDB();

        for (Resource scriptPath : scriptPaths) {
            runScript(db, scriptPath);
        }
    }

    private void runScript(DB db, Resource scriptPath) throws MongoDataException {
        String script;
        try {
            script = IOUtils.toString(scriptPath.getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new MongoDataException("Unable to read script " + scriptPath);
        }

        CommandResult result = db.doEval("function() { " + script + " }");
        if (!result.ok()) {
            throw new MongoDataException("An error occurred while running script " + scriptPath, result.getException());
        }

        logger.info("Mongo script {} executed successfully", scriptPath);
    }

    private DB getDB() throws MongoDataException {
        DB db = mongo.getDB(dbName);
        if (!StringUtils.isBlank(password)) {
            if (!db.authenticate(username, password.toCharArray())) {
                throw new MongoDataException("Unable to authenticate with given user/pwd");
            }
        }

        return db;
    }

}
