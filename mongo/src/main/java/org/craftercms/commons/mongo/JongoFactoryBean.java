package org.craftercms.commons.mongo;

import com.mongodb.DB;
import com.mongodb.Mongo;

import org.apache.commons.lang3.StringUtils;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Creates a Jongo singleton for application wide
 * use.
 *
 * @author Alfonso Vazquez.
 */
public class JongoFactoryBean extends AbstractFactoryBean<Jongo> {

    private String dbName;
    private String username;
    private String password;
    private Mongo mongo;

    @Required
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Required
    public void setMongo(Mongo mongoClient) {
        this.mongo = mongoClient;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public Class<?> getObjectType() {
        return Jongo.class;
    }

    @Override
    protected Jongo createInstance() throws Exception {
        DB db = mongo.getDB(dbName);
        if (!StringUtils.isBlank(password)) {
            if (!db.authenticate(username, password.toCharArray())) {
                throw new MongoDataException("Unable to authenticate with given user/pwd");
            }
        }
        return new Jongo(db);
    }

}
