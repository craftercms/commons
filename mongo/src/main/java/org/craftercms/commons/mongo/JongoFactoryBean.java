package org.craftercms.commons.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * {@link org.springframework.beans.factory.FactoryBean} that creates a Jongo singleton for application wide
 * use.
 *
 * @author avasquez
 */
public class JongoFactoryBean extends AbstractFactoryBean<Jongo> {

    private String dbName;
    private MongoClient mongoClient;

    @Required
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Required
    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Class<?> getObjectType() {
        return Jongo.class;
    }

    @Override
    protected Jongo createInstance() throws Exception {
        DB db = mongoClient.getDB(dbName);

        return new Jongo(db);
    }

}
