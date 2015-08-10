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
        return new MongoClient(new MongoClientURI(uri));
    }

    @Override
    protected void destroyInstance(MongoClient mongoClient) throws Exception {
        mongoClient.close();
    }

}