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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileExistsException;
import org.bson.types.ObjectId;

/**
 * @author Carlos Ortiz.
 */
@SuppressWarnings("unchecked")
public interface CrudRepository<T> {

    /**
     * Inserts the document into the collection.
     *
     * @param document document to be inserted (aka Pojo).
     * @throws org.craftercms.commons.mongo.MongoDataException if the document can't be inserted.
     */
    void insert(T document) throws MongoDataException;

    /**
     * Inserts multiple documents into the collection.
     *
     * @param documents documents to be inserted (aka Pojo).
     * @throws org.craftercms.commons.mongo.MongoDataException if the documents can't be inserted.
     */
    void insert(T... documents) throws MongoDataException;

    /**
     * Saves the document into the collection.
     *
     * @param document Document to be save (aka Pojo).
     * @throws org.craftercms.commons.mongo.MongoDataException if Document can't be save.
     */
    void save(T document) throws MongoDataException;

    /**
     * Inserts in the given collection the json <i>"As Is"</i><br>
     * Json String can contain placeholders this will allow the user to have predefine json strings<br/>
     * that will be substitute with the given queryParams.<b>Params are not Name</b> therefor if the same value is
     * needed
     * multiple times for now it has to be send multiple times.<b>Order of the queryParams</b> should match the same in
     * the json string.<br/>Example<br/>
     * <code>
     * String Json ="{name:#,address: #,age:#}" <br/>
     * save("testCollection",Json,"Dr.John Z.", new Address(), 125);
     * </code>
     *
     * @param query       Name of the Query to be look in default-queries.xml or custom-queries.properties
     * @param queryParams Params of the json.
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    void save(String query, Object... queryParams) throws MongoDataException;

    /**
     * Updates the object with the given id with the given Object information.
     *
     * @param id           Id of the object to be updated.
     * @param multi        If set to true, updates multiple documents that meet the query criteria.
     *                     If set to false, updates one document.
     * @param upsert       If set to true, creates a new document when no document matches the query criteria.
     * @param updateObject Object to be use to updated.
     * @throws org.craftercms.commons.mongo.MongoDataException if document can't be save.
     */
    void update(final String id, final Object updateObject, final boolean multi,
                final boolean upsert) throws MongoDataException;


    /**
     *<p> Updates the object with the given id with the given Object information.</p>
     *<p>Should be equals to {@link #update(String, Object, boolean, boolean)} with String,Object,false,false</p>
     * @param id           Id of the object to be updated.
     * @param updateObject Object to be use to updated.
     * @throws org.craftercms.commons.mongo.MongoDataException if document can't be save.
     */
    void update(final String id, final Object updateObject) throws MongoDataException;
    /**
     * Updates the given
     *
     * @param id       Id of the object to be updated.
     * @param modifier The modifications to apply.
     * @param multi    If set to true, updates multiple documents that meet the query criteria.
     *                 If set to false, updates one document.
     * @param upsert   If set to true, creates a new document when no document matches the query criteria.
     * @throws org.craftercms.commons.mongo.MongoDataException if document can't be save.
     */
    void update(final String id, final String modifier, final boolean multi,
                final boolean upsert) throws MongoDataException;

    /**
     * Updates the given
     *
     * @param id       Id of the object to be updated.
     * @param modifier The modifications to apply.
     * @param multi    If set to true, updates multiple documents that meet the query criteria.
     *                 If set to false, updates one document.
     * @param upsert   If set to true, creates a new document when no document matches the query criteria.
     * @param params   Params of the modifier query.
     * @throws org.craftercms.commons.mongo.MongoDataException if document can't be save.
     */
    void update(final String id, final String modifier, final boolean multi, final boolean upsert,
                final Object... params) throws MongoDataException;

    /**
     * Returns the number of all documents in the collection.
     *
     * @return the count of all documents
     * @throws MongoDataException if an error occurs
     */
    long count() throws MongoDataException;

    /**
     * Returns the number of documents that match the query
     *
     * @param query the query the documents should match
     * @return the count of documents that match the query
     * @throws MongoDataException if an error occurs
     */
    long count(String query) throws MongoDataException;

    /**
     * Returns the number of documents that match the query
     *
     * @param query       the query the documents should match
     * @param queryParams the query parameters
     * @return the count of documents that match the query
     * @throws MongoDataException if an error occurs
     */
    long count(String query, Object... queryParams) throws MongoDataException;

    /**
     * Gets all documents of a the given collection. Tries to convert them in to Instances of the given class.
     * <b>Order of the params</b> should match the same in
     * the json string.<br/>Example<br/>
     * <code>
     * String Json ="{name:#,address: #,age:#}" <br/>
     * find("testCollection",Json,"Dr. John.Z", new Address(), 125);
     * </code>
     *
     * @return A Iterable Instance of all the documents.<b>This is lazy loaded</b>
     * @throws org.craftercms.commons.mongo.MongoDataException If couldn't search for the documents. or a mapping
     *                                                         exception happen.
     */
    Iterable<T> findAll() throws MongoDataException;

    /**
     * Search all documents in the given collection that match the query.
     *
     * @param query Name of the Query to be look in default-queries.xml or custom-queries.properties
     * @return A Iterable Instance of all the documents that match the query.<b>This is lazy loaded</b>
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    Iterable<T> find(String query) throws MongoDataException;

    /**
     * Finds all documents of the given collection that match the template query<br/>§§
     *
     * @param query       Name of the  Template Query to be look in default-queries.xml or custom-queries
     *                    .properties
     * @param queryParams Params to be use in the template query. Must match the order of the templates
     *                    .<br/><b>Template
     *                    queryParams are not named, therefor they have to be send multiple times if needed</b>
     * @return A Iterable Instance of all the documents.<b>This is lazy loaded</b>
     * @throws org.craftercms.commons.mongo.MongoDataException If couldn't search for the documents. or a mapping
     *                                                         exception happen.
     */
    Iterable<T> find(String query, Object... queryParams) throws MongoDataException;

    /**
     * Search for documents of the given collection that match the query.<br>In only return the first Document</br>
     *
     * @param query Name of the  Template Query to be look in default-queries.xml or custom-queries
     *              .properties
     * @return A instance of the given class. Null if nothing is found.
     * @throws org.craftercms.commons.mongo.MongoDataException If couldn't search for the documents. or a mapping
     *                                                         exception happen.
     */
    T findOne(String query) throws MongoDataException;

    /**
     * Search for documents of the given collection that match the query.<br>In only return the first Document</br>
     *
     * @param query       Name of the  Template Query to be look in default-queries.xml or custom-queries
     *                    .properties
     * @param queryParams Params to be use in the template query. Must match the order of the templates
     *                    .<br/><b>Template
     *                    queryParams are not named, therefor they have to be send multiple times if needed</b>
     * @return A instance of the given class. Null if nothing is found.
     * @throws org.craftercms.commons.mongo.MongoDataException If couldn't search for the documents. or a mapping
     *                                                         exception happen.
     */
    T findOne(String query, Object... queryParams) throws MongoDataException;

    /**
     * Removes all Documents that are found using the given query.
     *
     * @param query       Name of the  Template Query to be look in default-queries.xml or custom-queries
     *                    .properties
     * @param queryParams Params to be use in the template query. Must match the order of the templates
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    void remove(String query, Object... queryParams) throws MongoDataException;

    /**
     * <p>Finds by the Id.</p>
     * <p><b>Internal transforms the given String to and Object Id</b></p>
     * @param id String representation of the Id.
     * @return A instance of the object with the given Id.
     * @throws java.lang.IllegalArgumentException If given id is not a Valid Object Id.
     */
    T findById(String id) throws MongoDataException;

    /**
     * Removes all Documents that are found using the given query.
     *
     * @param query Name of the  Template Query to be look in default-queries.xml or custom-queries
     *              .properties
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    void remove(String query) throws MongoDataException;

    /**
     * Removes a Document with the given <b>id</b>
     *
     * @param id Id of the object to be remove
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    void removeById(String id) throws MongoDataException;

    /**
     * Saves the given InputStream as with the given name.
     * <b>Closes the Stream after its done</b>.
     * @param inputStream InputStream to be Save.
     * @param storeName File name for the inputStream.
     * @return FileInfo with all the information of the file.
     * @throws MongoDataException If Can't save the file.
     * @throws FileExistsException If a file with the given file name already exists.
     */
    FileInfo saveFile(final InputStream inputStream,final String storeName,final String contentType,final ObjectId fileId) throws
        MongoDataException,
        FileExistsException;

    /**
     * Saves the given InputStream as with the given name.
     * <b>Closes the Stream after its done</b>.
     * @param inputStream InputStream to be Save.
     * @param storeName File name for the inputStream.
     * @return FileInfo with all the information of the file.
     * @throws MongoDataException If Can't save the file.
     * @throws FileExistsException If a file with the given file name already exists.
     */
    FileInfo saveFile(final InputStream inputStream,final String storeName,final String contentType) throws
        MongoDataException,
        FileExistsException;

    /**
     * Gets the file information based on its id..
     * @param fileId file Id to look up the information.
     * @return File Information of the file.
     * @throws FileNotFoundException If file with the given id does not exist.
     */
    FileInfo getFileInfo(final ObjectId fileId) throws FileNotFoundException;
    /**
     * Gets the file information based on its name..
     * @param storeName file name to look up the information.
     * @return File Information of the file.
     * @throws FileNotFoundException If file with the given id does not exist.
     */
    FileInfo getFileInfo(final String storeName) throws FileNotFoundException;

    /**
     * Returns the InputStream of the file with the given id.
     * @param fileId File Id to read.
     * @return A InputStream with that file Information.
     * @throws FileNotFoundException If there is no file with that Id.
     */
    FileInfo readFile(final ObjectId fileId) throws FileNotFoundException;
    /**
     * Returns the InputStream of the file with the given name.
     * @param storeName File Id to read.
     * @return A InputStream with that file Information.
     * @throws FileNotFoundException If there is no file with that name.
     */
    FileInfo readFile(final String storeName) throws FileNotFoundException;

    /**
     * Deletes the File with the given Id.
     * @param fileId Id of the file to delete.
     * @throws FileNotFoundException If there is no file with that id.
     */
    void deleteFile(final ObjectId fileId) throws FileNotFoundException;

    /**
     * Deletes the File with the given name.
     * @param storeName Name of the file to delete.
     * @throws FileNotFoundException If there is no file with that name.
     */
    void deleteFile(final String storeName) throws FileNotFoundException;

    /**
     *<p>"Updates" the file with the new information (A name change is valid as long a file with new name does not
     * exists)</p>
     * <p><b>Mongodb Does not actually support any update Operation in GridFs therefor Calling this method
     * should be as calling {@link #deleteFile(org.bson.types.ObjectId)}
     * and then {@link #saveFile(java.io.InputStream, String,String)} <i>It will generate also new FileInfo Including
     * It's Id</i></b>
     * </p>
     * <p></p>
     * @param fileId File id to be Updated
     * @param inputStream new InputStream of the file.
     * @param storeName File name of the inputStream (can differ from the original).
     * @return The new FileInfo of the "Updated" file
     * @throws FileNotFoundException If File with Given Id Does not exists.
     * @throws MongoDataException If unable to save the File.
     * @throws FileExistsException If a file name exists with the new storeName <i>(this should Only happen if you change
     * the file name)</i>
     */
    FileInfo updateFile(final ObjectId fileId, final InputStream inputStream, final String storeName,
                        final String contentType) throws FileNotFoundException,
        MongoDataException, FileExistsException;

    /**
     *<p>"Updates" the file with the new information (A name change is valid as long a file with new name does not
     * exists)</p>
     * <p><b>Mongodb Does not actually support any update Operation in GridFs therefor Calling this method
     * should be as calling {@link #deleteFile(org.bson.types.ObjectId)}
     * and then {@link #saveFile(java.io.InputStream, String,String)} <i>It will generate also new FileInfo Including
     * It's Id</i></b>
     * </p>
     * <p></p>
     * @param fileId File id to be Updated
     * @param inputStream new InputStream of the file.
     * @param storeName File name of the inputStream (can differ from the original).
     * @return The new FileInfo of the "Updated" file
     * @throws FileNotFoundException If File with Given Id Does not exists.
     * @throws MongoDataException If unable to save the File.
     * @throws FileExistsException If a file name exists with the new storeName <i>(this should Only happen if you change
     * the file name)</i>
     */
    FileInfo updateFile(final ObjectId fileId, final InputStream inputStream, final String storeName,
                        final String contentType,final boolean sameFileId) throws FileNotFoundException,
        MongoDataException, FileExistsException;

    /**
     *<p>"Updates" the file with the new information <b><i>(A name change is NOT valid )</i></b></p>
     * <p><b>Mongodb Does not actually support any update Operation in GridFs therefor Calling this method
     * should be as calling {@link #deleteFile(org.bson.types.ObjectId)}
     * and then {@link #saveFile(java.io.InputStream, String,String)} <i>It will generate also new FileInfo Including
     * It's Id</i></b>
     * </p>
     * <p></p>
     * @param storeName File id to be Updated. (Have to the the same as the original of not
     *                  it will throw a FileNotFoundException)
     * @param inputStream new InputStream of the file.
     * @return The new FileInfo of the "Updated" file
     * @throws FileNotFoundException If File with Given Id Does not exists.
     * @throws MongoDataException If unable to save the File.
     * @throws FileExistsException this exception is highly unlikely to happen but posible.
     */
    FileInfo updateFile(final InputStream inputStream, final String storeName,final String contentType) throws
        FileNotFoundException,
        MongoDataException,
        FileExistsException;

    /**
     * <p>Finds by the Id.</p>
     * <p><b> No internal Modification is done, uses Mongodb default '_id' field name</b></p>
     * @param id String representation of the Id.
     * @return A instance of the object with the given Id.
     */
    T findByStringId(String id) throws MongoDataException;

    List<FileInfo> listFilesByName(String filename);

    /**
     * <p>Removes by the Id.</p>
     * <p><b> No internal Modification is done, uses Mongodb default '_id' field name</b></p>
     * @param id String representation of the Id.
     */
    void removeByStringId(String id) throws MongoDataException;

}
