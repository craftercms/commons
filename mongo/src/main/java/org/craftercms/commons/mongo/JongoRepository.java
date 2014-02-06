/*
 * Copyright (C) 2007-${year} Crafter Software Corporation.
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
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.mongo;

import java.lang.reflect.ParameterizedType;

import com.mongodb.CommandResult;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Simple interface to interact with Jongo/MongoDB.<br/>
 * Changes MongoException in to MongoRepositoryException (which is a checked exception).As well if Command
 * result is not ok (CommandResult#isOk) return false a exception will be thrown the message for that exception will
 * be CommandResult#getErrorMessage.<br/>
 * Some of the find and insert methods use a template queryName. this means that the string
 * can contain placeholders ('#') this will allow the user to have predefine json strings
 * that will be substitute with the given params.<b>Params are not Name</b> therefor if the same value is needed
 * multiple times for now it has to be send multiple times.<b>Order of the params</b> should match the same in
 * the json string.
 * <p> This class is mark as abstract to force inheritance ,  so we can get in runtime the class of the generic
 * parameter with this we can simplify the mapping.
 * </p>
 *
 * @author Carlos Ortiz.
 */
public abstract class JongoRepository<T> implements CrudRepository<T> {


    private final Class<T> clazz;
    protected Jongo jongo;
    protected String collectionName;
    private Logger log = LoggerFactory.getLogger(JongoRepository.class);
    private JongoQueries queries;

    /**
     * Creates A instance of a Jongo Repository.
     */
    @SuppressWarnings("unchecked")
    public JongoRepository() throws MongoDataException {
        //Thru pure magic get parameter Class .
        this.clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (this.clazz == null) {
            log.error("Unable to get class information for repository.");
            throw new MongoDataException("Unable to create a JongoRepository , I'm unable to get Class for Type " +
                "parameter");
        }
        // Try to get Document Annotation
        Document documentAnnotation = this.clazz.getAnnotation(Document.class);
        if (documentAnnotation == null) { // Go default
            collectionName = clazz.getSimpleName().toLowerCase();
        } else {
            collectionName = documentAnnotation.collectionName();
        }
    }

    /**
     * Gets the Jongo Document.
     *
     * @return a Jongo Document to interact with the Mongo.
     */
    protected MongoCollection getCollection() {
        return jongo.getCollection(collectionName);
    }

    @Override
    public void save(T document) throws MongoDataException {
        try {
            WriteResult result = getCollection().save(document);
            checkCommandResult(result);
        } catch (MongoException ex) {
            log.error("Unable to save Document", ex);
            throw new MongoDataException(ex);
        }
    }

    @Override
    public void save(final String query, final Object... queryParams) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().insert(query, queryParams);
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            log.debug("Something went wrong while trying to save into mongodb ", ex);
            throw new MongoDataException(ex);
        }
    }

    @Override
    public Iterable<T> findAll() throws MongoDataException {
        try {
            return returnList(getCollection().find());
        } catch (MongoException ex) {
            log.error("Unable to find all documents of type " + clazz.toString(), ex);
            throw new MongoDataException(ex);
        }

    }



    @Override
    public Iterable<T> find(final String query) throws MongoDataException {
        try {
            return returnList(getCollection().find(query));
        } catch (MongoException ex) {
            log.error("Unable to find by query" + query + " of type " + clazz.toString(), ex);
            throw new MongoDataException(ex);
        }
    }

    @Override
    public Iterable<T> find(final String query, Object... queryParams) throws MongoDataException {
        try {
            return returnList(getCollection().find(query, queryParams));
        } catch (MongoException ex) {
            log.error("Unable to find by query" + query + " of type " + clazz.toString(), ex);
            throw new MongoDataException(ex);
        }
    }

    @Override
    public T findOne(final String query) throws MongoDataException {
        try {
            return returnSimple(getCollection().findOne(query));
        } catch (MongoException ex) {
            log.error("Unable to find by query" + query + " of type " + clazz.toString(), ex);
            throw new MongoDataException("Unable to query mongodb", ex);
        }
    }

    @Override
    public T findOne(final String query, final Object... queryParams) throws MongoDataException {
        try {
            return getCollection().findOne(query, queryParams).as(clazz);
        } catch (MongoException ex) {
            log.error("Unable to find by query" + query + " of type " + clazz.toString(), ex);
            throw new MongoDataException("Unable to query mongodb", ex);
        }
    }

    @Override
    public void remove(final String query, final Object... queryParams) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(query, queryParams);
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            log.error("Unable to delete ", ex);
            throw new MongoDataException("Unable to remove document ", ex);
        }
    }

    @Override
    public void remove(final String query) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(query);
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            log.error("Unable to delete ", ex);
            throw new MongoDataException("Unable to remove document ", ex);
        }
    }

    @Override
    public void removeById(String objectId) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(new ObjectId(objectId));
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            log.error("Unable to delete ", ex);
            throw new MongoDataException("Unable to remove document ", ex);
        }
    }

    @Override
    public T findById(final String id) throws MongoDataException {
        try {
            return getCollection().findOne(new ObjectId(id)).as(clazz);
        } catch (MongoException ex) {
            log.error("Unable to search Object by id " + id, ex);
            throw new MongoDataException("Unable to find Object by id", ex);
        } catch (IllegalArgumentException ex) {
            log.error("Given Id " + id + " can't be converted to a ObjectId");
            throw new MongoDataException("Invalid Id " + id);
        }
    }

    /**
     * Get the query string for a given key
     *
     * @param key Key of the query,
     * @return Query with the given key. IllegalArgumentException if query does not exist
     * @throws java.lang.IllegalArgumentException if q
     */
    protected String getQueryFor(final String key) {
        log.trace("Trying to get query for {} ", key);
        String query = queries.get(key);
        log.trace("Query found {} for key {}", query, key);
        if (query == null) {
            log.error("Query for {} key does not exist", key);
            throw new IllegalArgumentException("Query for key " + key + " does not exist");
        } else if (StringUtils.isBlank(query)) {
            log.error("Query for key {} can't be blank or whitespace", key);
            throw new IllegalArgumentException("Query for key " + key + " can't be blank or whitespace");
        }
        return query;
    }

    /**
     * Internal checks if the CommandResult is ok , if not will throw a MongoRepositoryException with the last error
     * message given by CommandResult#getErrorMessage as the exception  message.
     *
     * @param result Write Result.
     * @throws MongoDataException if CommandResult#Ok is false.
     */
    protected void checkCommandResult(final WriteResult result) throws MongoDataException {
        CommandResult lastError = result.getLastError();
        log.debug("Saving send to mongodb checking result");
        log.debug("Result is {}", lastError.ok()? "OK": lastError.getErrorMessage());
        if (!lastError.ok()) {
            log.error("Unable to save into mongodb due " + lastError.getErrorMessage(), lastError.getException());
            throw new MongoDataException(lastError.getException());
        }
    }

    /**
     * Actually makes the transformation form Jongo to List of Objects.
     * @param find Find object to transform.
     * @return a Iterable with the results <b>null</b> if nothing is found.
     */
    protected Iterable<T> returnList(final Find find){
        return find.as(clazz);
    }

    /**
     * Actually makes the transformation form Jongo to the Object.
     * @param findOne Find object to transform.
     * @return a Object with the results <b>null</b> if nothing is found.
     */
    protected T returnSimple(final FindOne findOne){
        return findOne.as(clazz);
    }


    @Required
    public void setJongo(final Jongo jongo) {
        this.jongo = jongo;
    }

    public void setQueries(final JongoQueries queries) {
        this.queries = queries;
    }
}