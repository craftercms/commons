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

/**
 * @author Carlos Ortiz.
 */
public interface CrudRepository<T> {
    /**
     * Saves the Document into the collection.
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
    void update(final String id, final T updateObject, final boolean multi, final boolean upsert)
            throws MongoDataException;

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
    void update(final String id, final String modifier, final boolean multi, final boolean upsert)
            throws MongoDataException;

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
     * @param query         the query the documents should match
     * @param queryParams   the query parameters
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
     * exception happen.
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
     * exception happen.
     */
    Iterable<T> find(String query, Object... queryParams) throws MongoDataException;

    /**
     * Search for documents of the given collection that match the query.<br>In only return the first Document</br>
     *
     * @param query Name of the  Template Query to be look in default-queries.xml or custom-queries
     *              .properties
     * @return A instance of the given class. Null if nothing is found.
     * @throws org.craftercms.commons.mongo.MongoDataException If couldn't search for the documents. or a mapping
     * exception happen.
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
     * exception happen.
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
     * Finds by the Id.
     *
     * @param id String representation of the Id.
     * @return A instance of the object with the given Id.
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
     * @param id
     * @throws org.craftercms.commons.mongo.MongoDataException
     */
    void removeById(String id) throws MongoDataException;
}
