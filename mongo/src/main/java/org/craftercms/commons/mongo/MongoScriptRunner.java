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
package org.craftercms.commons.mongo;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.InitializingBean;

/**
 * Utility class for running Mongo scripts in JS.
 *
 * @author avasquez
 */
@SuppressWarnings("deprecation")
public class MongoScriptRunner implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(MongoScriptRunner.class);

    private static final String MONGO_CLIENT_BIN_PATH = System.getenv("MONGODB_HOME") + "/bin/mongosh";

    private MongoClient mongo;
    private String dbName;
    private String username;
    private String password;
    private List<Resource> scriptPaths;
    private boolean runOnInit;
    private String connectionStr;


    public MongoScriptRunner() {
        this.runOnInit = true;
    }

    @Required
    public void setMongo(MongoClient mongo) {
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

    public void setConnectionStr(final String connectionStr) {
        this.connectionStr = connectionStr;
    }

    public void afterPropertiesSet() throws Exception {
        logger.debug("Running Scripts?", runOnInit);
        if (runOnInit) {
            logger.debug("Using Mongo Client");
            runScriptsWithMongoClient();
        }
    }

    private void runScriptsWithMongoClient() {
        List<String> toExecute=new ArrayList<>();

        try {
            for (Resource scriptPath : scriptPaths) {
                if (scriptPath.getFile().isDirectory()) {
                    Files.walkFileTree(scriptPath.getFile().toPath(), new JSFileVisitor(toExecute));
                } else {
                    toExecute.add(scriptPath.getFile().getAbsolutePath());
                }
            }
            Collections.sort(toExecute);

            final Path allScripsFile = Files.createTempFile("ScriptRunner", ".js");
            StringBuilder builder =new StringBuilder();
            for (String path : toExecute) {
                builder.append(String.format("load('%s');\n",path));
            }
            FileUtils.writeStringToFile(allScripsFile.toFile(),builder.toString(),"UTF-8");
            runScript(allScripsFile);
            Files.deleteIfExists(allScripsFile);
        } catch (IOException | MongoDataException ex) {
            logger.error("Unable to run script using MongoClient",ex);
        }
    }

    private void runScript(Path scriptPath) throws MongoDataException {
        try {
            ProcessBuilder mongoProcess = new ProcessBuilder(getCommands(scriptPath));
            Process process=mongoProcess.start();
            int result=process.waitFor();
            String stdOut;
            String errOut;
            try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
                IOUtils.copy(process.getInputStream(),out);
                stdOut = new String(out.toByteArray(),"UTF-8");
                out.reset();
                IOUtils.copy(process.getErrorStream(),out);
                errOut = new String(out.toByteArray(),"UTF-8");
                IOUtils.copy(process.getInputStream(),out);
            }
            if(result!=0){
                throw new IOException("Process return error \n std out:"+stdOut+"\n err out: \n"+errOut);
            }else{
             logger.debug("Process return \n std out:"+stdOut+"\n err out: \n"+errOut);
            }
        } catch (IOException | InterruptedException ex) {
            logger.error("Unable to Execute mongo Process", ex);
        }
    }

    private List<String> getCommands(final Path scriptPath) throws MongoDataException {
        List<String> commandList=new ArrayList<>();
        if(SystemUtils.IS_OS_WINDOWS){
            commandList.add("CMD");
            commandList.add("/C");
        }
        if (StringUtils.isBlank(MONGO_CLIENT_BIN_PATH)) {
            throw new MongoDataException("Unable to run scripts, mongo client bin path is not set ");
        }
        String pwd = null;
        String authSource=null;
        String user=null;
        MongoClientURI uri=new MongoClientURI(connectionStr);
        if(uri.getCredentials()!=null) {
             authSource= uri.getCredentials().getSource();
             user= uri.getCredentials().getUserName();
            if (uri.getCredentials().getPassword() != null) {
                pwd = new String(uri.getCredentials().getPassword());
            }
        }
        String replicaSetName="";
        if(uri.getHosts().size()>1){
            replicaSetName=uri.getOptions().getRequiredReplicaSetName()+"/";
        }
        final String host = StringUtils.trim(replicaSetName+StringUtils.join(uri.getHosts(),","));
        commandList.add(MONGO_CLIENT_BIN_PATH);
        commandList.add("--host");
        commandList.add(host);
        commandList.add(uri.getDatabase());
        if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(pwd) && StringUtils.isNotBlank(authSource)) {
            commandList.add("-u");
            commandList.add(user);
            commandList.add("-p");
            commandList.add(pwd);
            commandList.add("--authenticationDatabase");
            commandList.add(authSource);
        }
        commandList.add(scriptPath.toAbsolutePath().toString());
        return commandList;
    }

    private DB getDB() throws MongoDataException {
        DB db = mongo.getDB(dbName);
        logger.debug("Getting DB {}",dbName);
        return db;
    }


    class JSFileVisitor extends SimpleFileVisitor<Path> {
        private List<String> filesTOAdd;

        public JSFileVisitor(final List<String> filesTOAdd) {
            this.filesTOAdd = filesTOAdd;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            if(attrs.isRegularFile() && attrs.size()>0 && StringUtils.endsWithIgnoreCase(file.toString(),".js")){
                filesTOAdd.add(file.toAbsolutePath().toString());
            }
            return super.visitFile(file, attrs);
        }
    }
}
