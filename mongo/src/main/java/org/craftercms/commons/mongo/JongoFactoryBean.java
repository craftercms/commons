package org.craftercms.commons.mongo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.mongodb.DB;
import com.mongodb.Mongo;

import java.util.List;
import java.util.Map;

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
    private Mongo mongo;
    private List<JsonSerializer<?>> serializers;
    private Map<Class<?>, JsonDeserializer<?>> deserializers;

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
    protected Jongo createInstance() throws Exception {
        DB db = mongo.getDB(dbName);
        if (!StringUtils.isBlank(password)) {
            if (!db.authenticate(username, password.toCharArray())) {
                throw new MongoDataException("Unable to authenticate with given user/pwd");
            }
        }

        JacksonMapper.Builder builder = new JacksonMapper.Builder();
        builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        if (CollectionUtils.isNotEmpty(serializers) || MapUtils.isNotEmpty(deserializers)) {
            builder.registerModule(JacksonUtils.createModule(serializers, deserializers));
        }

        return new Jongo(db, builder.build());
    }

}
