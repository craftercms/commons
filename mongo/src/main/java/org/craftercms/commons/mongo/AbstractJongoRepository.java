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

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Update;
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
@SuppressWarnings("unchecked") // cortiz, OK no better way to do it.
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

    @Override
    public void insert(T document) throws MongoDataException {
        try {
            getCollection().insert(document);
        } catch (com.mongodb.DuplicateKeyException ex) {
            String msg = "Duplicate key for document " + document;
            log.error(msg, ex);
            throw new DuplicateKeyException(msg, ex);
        } catch (MongoException ex) {
            String msg = "Unable to insert document " + document;
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
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
    public void insert(T... documents) throws MongoDataException {
        try {
            getCollection().insert(documents);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
            getCollection().save(document);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
            getCollection().insert(query, queryParams);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
    public void update(final String id, final Object updateObject, final boolean multi, final boolean upsert) throws
        MongoDataException {
        try {
            Update update = getCollection().update(new ObjectId(id));
            if (multi) {
                update.multi();
            }
            if (upsert) {
                update.upsert();
            }
            update.with(updateObject);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
    public void update(final String id, final Object updateObject) throws MongoDataException {
        update(id, updateObject, false, false);
    }

    @Override
    public void update(final String id, final String modifier, final boolean multi, final boolean upsert) throws
        MongoDataException {
        try {
            Update update = getCollection().update(new ObjectId(id));
            if (multi) {
                update.multi();
            }
            if (upsert) {
                update.upsert();
            }
            update.with(modifier);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
            update.with(modifier, params);
        } catch (com.mongodb.DuplicateKeyException ex) {
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
    public void remove(final String query, final Object... queryParams) throws MongoDataException {
        try {
            getCollection().remove(query, queryParams);
        } catch (MongoException ex) {
            String msg = "Unable to remove document by query " + query + " of type " + clazz.getName() +
                " with params " + Arrays.toString(queryParams);
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public T findById(final String id) throws MongoDataException {
        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Given String " + id + " is not a valid Object Id");
        }
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
    public void remove(final String query) throws MongoDataException {
        try {
            getCollection().remove(query);
        } catch (MongoException ex) {
            String msg = "Unable to remove document by query " + query + " of type " + clazz.getName();
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public void removeById(final String id) throws MongoDataException {
        try {
            getCollection().remove(new ObjectId(id));
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
    public FileInfo saveFile(final InputStream inputStream, final String storeName, final String contentType, final
    ObjectId fileId) throws MongoDataException, FileExistsException {
        try {

            if (gridfs.findOne(storeName) != null) {
                log.error("A file named {} already exists", storeName);
                throw new FileExistsException("File with name " + storeName + " already Exists");
            }
            GridFSInputFile savedFile = gridfs.createFile(inputStream, storeName, true);
            savedFile.setContentType(contentType);
            if (fileId != null) {
                log.debug("Saving file with given Id {} probably a update", fileId);
                savedFile.setId(fileId);
            }
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
    public FileInfo saveFile(final InputStream inputStream, final String storeName, final String contentType) throws
        MongoDataException, FileExistsException {
        return saveFile(inputStream, storeName, contentType, null);
    }

    @Override
    public FileInfo getFileInfo(final ObjectId fileId) throws FileNotFoundException {
        return new FileInfo(validateObject(fileId), false);
    }

    @Override
    public FileInfo getFileInfo(final String storeName) throws FileNotFoundException {
        return new FileInfo(validateObject(storeName), false);
    }

    @Override
    public FileInfo readFile(final ObjectId fileId) throws FileNotFoundException {
        return new FileInfo(validateObject(fileId), true);
    }

    @Override
    public FileInfo readFile(final String storeName) throws FileNotFoundException {
        return new FileInfo(validateObject(storeName), true);
    }

    @Override
    public void deleteFile(final ObjectId fileId) throws FileNotFoundException {
        gridfs.remove(validateObject(fileId));
    }

    @Override
    public void deleteFile(final String storeName) throws FileNotFoundException {
        gridfs.remove(validateObject(storeName));
    }

    @Override
    public FileInfo updateFile(final ObjectId fileId, final InputStream inputStream, final String storeName, final
    String contentType) throws FileNotFoundException, MongoDataException, FileExistsException {
        return updateFile(fileId, inputStream, storeName, contentType, false);
    }

    @Override
    public FileInfo updateFile(final ObjectId fileId, final InputStream inputStream, final String storeName, final
    String contentType, boolean sameFileId) throws FileNotFoundException, MongoDataException, FileExistsException {
        gridfs.remove(validateObject(fileId));
        return saveFile(inputStream, storeName, contentType, sameFileId? fileId: null);
    }

    @Override
    public FileInfo updateFile(final InputStream inputStream, final String storeName, final String contentType)
        throws FileNotFoundException, MongoDataException, FileExistsException {
        gridfs.remove(validateObject(storeName));
        return saveFile(inputStream, storeName, contentType);
    }

    @Override
    public T findByStringId(final String id) throws MongoDataException {
        try {
            return getCollection().findOne("{_id:#}", id).as(clazz);
        } catch (MongoException ex) {
            String msg = "Unable to find document of type " + clazz.getName() + " by id '" + id + "'";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    @Override
    public List<FileInfo> listFilesByName(final String filename){
        final List<GridFSDBFile> files = gridfs.find(new BasicDBObject("filename",new BasicDBObject("$regex",
            ".*"+filename+".*")));
        final ArrayList<FileInfo> toReturn = new ArrayList<FileInfo>();
        for (GridFSDBFile file : files) {
            toReturn.add(new FileInfo(file,false));
        }
        return toReturn;
    }

    @Override
    public void removeByStringId(final String id) throws MongoDataException {
        try {
            getCollection().remove("{_id:#}", id);
        } catch (MongoException ex) {
            String msg = "Unable to remove document of type " + clazz.getName() + " by id '" + id + "'";
            log.error(msg, ex);
            throw new MongoDataException(msg, ex);
        }
    }

    protected GridFSDBFile validateObject(final String storeName) throws FileNotFoundException {
        GridFSDBFile file = gridfs.findOne(storeName);
        if (file == null) {
            log.error("A file with name {} does not exists", storeName);
            throw new FileNotFoundException("File with file name " + storeName + " does not exist");
        }
        return file;
    }

    protected GridFSDBFile validateObject(final ObjectId fileId) throws FileNotFoundException {
        GridFSDBFile file = gridfs.findOne(fileId);
        if (file == null) {
            log.error("A file with id {} does not exists", fileId);
            throw new FileNotFoundException("File with file name " + fileId + " does not exist");
        }
        return file;
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

    /**
     * Actually makes the transformation form Jongo to List of Objects.
     *
     * @param find Find object to transform.
     * @return a Iterable with the results <b>null</b> if nothing is found.
     */
    @SuppressWarnings("uncheck")// cortiz, might change in jongo 1.4
    protected Iterable<T> returnList(final Find find) {
        return (Iterable<T>)find.as(clazz);
    }

    @Required
    public void setJongo(final Jongo jongo) {
        this.jongo = jongo;
        this.gridfs = new GridFS(jongo.getDatabase());
    }

    public void setQueries(final JongoQueries queries) {
        this.queries = queries;
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
     * Creates a Sort query based on the fields.<br/>
     * Key of the map is the field <b>False=Desc,True=asc</b>
     * for the field, Respect order of the keys
     *
     * @param fields Keys are fields, true if asc, false desc
     * @return Sorted Json string with the query.
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

}