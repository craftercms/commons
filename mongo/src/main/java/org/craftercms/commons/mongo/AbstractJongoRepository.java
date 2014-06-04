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

import com.mongodb.CommandResult;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
public abstract class AbstractJongoRepository<T> implements CrudRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractJongoRepository.class);

    protected Class<? extends T> clazz;
    protected Jongo jongo;
    protected String collectionName;
    protected JongoQueries queries;
    protected GridFS gridfs;

    public void init() throws Exception {
        //Thru pure magic get parameter Class .
        this.clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (this.clazz == null) {
            log.error("Unable to get class information for JongoRepository");
            throw new MongoDataException("Unable to get class information for JongoRepository");
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
    public void insert(T document) throws MongoDataException {
        try {
            WriteResult result = getCollection().insert(document);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for document " + document;
            log.error(msg, ex);
            throw new DuplicateKeyException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to insert document " + document;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void insert(T... documents) throws MongoDataException {
        try {
            WriteResult result = getCollection().insert(documents);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for documents " + Arrays.toString(documents);
            log.error(msg, ex);
            throw new DuplicateKeyException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to insert documents " + Arrays.toString(documents);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void save(final T document) throws MongoDataException {
        try {
            WriteResult result = getCollection().save(document);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for document " + document;
            log.error(msg, ex);
            throw new DuplicateKeyException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to save document " + document;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void save(final String query, final Object... queryParams) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().insert(query, queryParams);
            checkCommandResult(writeResult);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for save query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new DuplicateKeyException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to save document by query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public long count() throws MongoDataException {
        try {
            return getCollection().count();
        } catch (MongoException ex) {
            String msg = "Unable to count all documents of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public long count(String query) throws MongoDataException {
        try {
            return getCollection().count(query);
        } catch (MongoException ex) {
            String msg = "Unable to count documents of type " + clazz.getName() + " that match the " +
                "query " + query;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public long count(String query, Object... queryParams) throws MongoDataException {
        try {
            return getCollection().count(query, queryParams);
        } catch (MongoException ex) {
            String msg = "Unable to count documents of type " + clazz.getName() + " that match the " +
                "query " + query +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public Iterable<T> findAll() throws MongoDataException {
        try {
            return returnList(getCollection().find());
        } catch (MongoException ex) {
            String msg = "Unable to find all documents of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public Iterable<T> find(final String query) throws MongoDataException {
        try {
            return returnList(getCollection().find(query));
        } catch (MongoException ex) {
            String msg = "Unable to find documents by query " + query + " of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public Iterable<T> find(final String query, Object... queryParams) throws MongoDataException {
        try {
            return returnList(getCollection().find(query, queryParams));
        } catch (MongoException ex) {
            String msg = "Unable to find documents by query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public T findOne(final String query) throws MongoDataException {
        try {
            return returnSimple(getCollection().findOne(query));
        } catch (MongoException ex) {
            String msg = "Unable to find document by query " + query + " of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public T findOne(final String query, final Object... queryParams) throws MongoDataException {
        try {
            return getCollection().findOne(query, queryParams).as(clazz);
        } catch (MongoException ex) {
            String msg = "Unable to find document by query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void remove(final String query) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(query);
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            String msg = "Unable to remove document by query " + query + " of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void remove(final String query, final Object... queryParams) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(query, queryParams);
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            String msg = "Unable to remove document by query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void removeById(final String id) throws MongoDataException {
        try {
            WriteResult writeResult = getCollection().remove(new ObjectId(id));
            checkCommandResult(writeResult);
        } catch (MongoException ex) {
            String msg = "Unable to remove document of type " + clazz.getName() + " by id '" + id + "'";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        } catch (IllegalArgumentException ex) {
            String msg = "Given id '" + id + "' can't be converted to an ObjectId";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public T findById(final String id) throws MongoDataException {
        try {
            return getCollection().findOne(new ObjectId(id)).as(clazz);
        } catch (MongoException ex) {
            String msg = "Unable to find document of type " + clazz.getName() + " by id '" + id + "'";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        } catch (IllegalArgumentException ex) {
            String msg = "Given id '" + id + "' can't be converted to an ObjectId";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void update(final String id, final T updateObject, final boolean multi,
                       final boolean upsert) throws MongoDataException {
        try {
            Update update = getCollection().update(new ObjectId(id));
            if (multi) {
                update.multi();
            }
            if (upsert) {
                update.upsert();
            }
            WriteResult result = update.with(updateObject);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for update with id='" + id + "', updatedObject=" + updateObject + ", multi=" +
                multi + ", upsert=" + upsert;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to do update with id='" + id + "', updatedObject=" + updateObject + ", multi=" +
                multi + ", upsert=" + upsert;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void update(final String id, final String modifier, final boolean multi,
                       final boolean upsert) throws MongoDataException {
        try {
            Update update = getCollection().update(new ObjectId(id));
            if (multi) {
                update.multi();
            }
            if (upsert) {
                update.upsert();
            }
            WriteResult result = update.with(modifier);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for update with id='" + id + "', modifier=" + modifier + ", multi=" +
                multi + ", upsert=" + upsert;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to do update with id='" + id + "', modifier=" + modifier + ", multi=" +
                multi + ", upsert=" + upsert;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void update(final String id, final T updateObject) throws MongoDataException {
        update(id, updateObject, false, false);
    }

    public void update(final String id, final String modifier, final boolean multi, final boolean upsert,
                       final Object... params) throws MongoDataException {
        try {
            Update update = getCollection().update(new ObjectId(id));
            if (multi) {
                update.multi();
            }
            if (upsert) {
                update.upsert();
            }
            WriteResult result = update.with(modifier, params);
            checkCommandResult(result);
        } catch (MongoException.DuplicateKey ex) {
            String msg = "Duplicate key for update with id='" + id + "', modifier=" + modifier + ", multi=" +
                multi + ", upsert=" + upsert + ", params" + Arrays.toString(params);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to do update with id='" + id + "', modifier=" + modifier + ", multi=" +
                multi + ", upsert=" + upsert + ", params" + Arrays.toString(params);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public FileInfo saveFile(final InputStream inputStream, final String filename,
                             final String contentType) throws MongoDataException, FileExistsException {
        try {

            if (gridfs.findOne(filename) != null) {
                log.error("A file named {} already exists", filename);
                throw new FileExistsException("File with name " + filename + " already Exists");
            }
            GridFSInputFile savedFile = gridfs.createFile(inputStream, filename, true);
            savedFile.setContentType(contentType);
            savedFile.save();
            FileInfo fileInfo = new FileInfo(savedFile, false);
            log.debug("File {} was saved " + fileInfo);
            return fileInfo;
        } catch (MongoException ex) {
            log.error("Unable to save file");
            throw new MongoDataException("Unable to save file to GridFs", ex);
        }
    }

    @Override
    public FileInfo getFileInfo(final ObjectId fileId) throws FileNotFoundException {
        return new FileInfo(validateObject(fileId), false);
    }

    @Override
    public FileInfo getFileInfo(final String fileName) throws FileNotFoundException {
        return new FileInfo(validateObject(fileName), false);
    }

    @Override
    public FileInfo readFile(final ObjectId fileId) throws FileNotFoundException {
        return new FileInfo(validateObject(fileId), true);
    }

    @Override
    public FileInfo readFile(final String fileName) throws FileNotFoundException {
        return new FileInfo(validateObject(fileName), true);
    }

    @Override
    public void deleteFile(final ObjectId fileId) throws FileNotFoundException {
        gridfs.remove(validateObject(fileId));
    }

    @Override
    public void deleteFile(final String fileName) throws FileNotFoundException {
        gridfs.remove(validateObject(fileName));
    }

    @Override
    public FileInfo updateFile(final ObjectId fileId, final InputStream inputStream, final String filename,
                               final String contentType) throws FileNotFoundException, MongoDataException,
        FileExistsException {
        gridfs.remove(validateObject(fileId));
        return saveFile(inputStream, filename, contentType);
    }

    @Override
    public FileInfo updateFile(final InputStream inputStream, final String filename,
                               final String contentType) throws FileNotFoundException, MongoDataException,
        FileExistsException {
        gridfs.remove(validateObject(filename));
        return saveFile(inputStream, filename, contentType);
    }


    protected GridFSDBFile validateObject(final ObjectId fileId) throws FileNotFoundException {
        GridFSDBFile file = gridfs.findOne(fileId);
        if (file == null) {
            log.error("A file with id {} does not exists", fileId);
            throw new FileNotFoundException("File with file name " + fileId + " does not exist");
        }
        return file;
    }

    protected GridFSDBFile validateObject(final String fileName) throws FileNotFoundException {
        GridFSDBFile file = gridfs.findOne(fileName);
        if (file == null) {
            log.error("A file with name {} does not exists", fileName);
            throw new FileNotFoundException("File with file name " + fileName + " does not exist");
        }
        return file;
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
            log.error("Query for key {} can't be blank or be only whitespace", key);
            throw new IllegalArgumentException("Query for key " + key + " can't be blank or be only whitespace");
        }
        return query.trim().replaceAll("\\s+", " ");
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
            MongoException ex = lastError.getException();
            log.error("Unable to save into mongodb due to " + lastError.getErrorMessage(), ex);
            if (ex instanceof MongoException.DuplicateKey) {
                throw new DuplicateKeyException(ex.getMessage(), ex);
            } else {
                throw new MongoDataException(ex.getMessage(), ex);
            }
        }
    }


    /**
     * Creates a Sort query based on the fields.<br/>
     * Key of the map is the field <b>False=Desc,True=asc</b>
     * for the field, Respect order of the keys
     *
     * @param fields Keys are fields, true if asc, false desc
     * @return
     */
    protected String createSortQuery(final List<DefaultKeyValue<String, Boolean>> fields) {
        StringBuilder builder = new StringBuilder("{");
        Iterator<DefaultKeyValue<String, Boolean>> iter = fields.iterator();
        while (iter.hasNext()) {
            DefaultKeyValue<String, Boolean> field = iter.next();
            builder.append("\"");
            builder.append(field.getKey());
            builder.append("\"");
            builder.append(":");
            if (field.getValue()) {
                builder.append(1);
            } else {
                builder.append(-1);
            }
            if (iter.hasNext()) {
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Actually makes the transformation form Jongo to List of Objects.
     *
     * @param find Find object to transform.
     * @return a Iterable with the results <b>null</b> if nothing is found.
     */
    protected Iterable<T> returnList(final Find find) {
        return (Iterable<T>)find.as(clazz);
    }

    /**
     * Actually makes the transformation form Jongo to the Object.
     *
     * @param findOne Find object to transform.
     * @return a Object with the results <b>null</b> if nothing is found.
     */
    protected T returnSimple(final FindOne findOne) {
        return findOne.as(clazz);
    }

    @Required
    public void setJongo(final Jongo jongo) {
        this.jongo = jongo;
        this.gridfs = new GridFS(jongo.getDatabase());
    }

    public void setQueries(final JongoQueries queries) {
        this.queries = queries;
    }

}