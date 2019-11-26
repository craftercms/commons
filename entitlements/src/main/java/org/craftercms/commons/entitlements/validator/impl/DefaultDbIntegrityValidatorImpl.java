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

package org.craftercms.commons.entitlements.validator.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.craftercms.commons.entitlements.exception.EntitlementException;
import org.craftercms.commons.entitlements.validator.DbIntegrityValidator;

/**
 * Default Implementation of {@link DbIntegrityValidator}.
 *
 * @author joseross
 */
public class DefaultDbIntegrityValidatorImpl implements DbIntegrityValidator {

    private static final String SCHEMA = "{schema}";
    /**
     * SQL statement to update the validation value.
     */
    private static final String UPDATE = "update _meta set integrity = ?";

    /**
     * SQL statement to get the validation value.
     */
    private static final String QUERY = "select integrity from _meta";

    /**
     * SQL statement to generate the validation value.
     */
    protected String query = "select crc32(group_concat(concat(table_name, column_name) order by table_name, " +
            "column_name)) from information_schema.columns where table_schema = '{schema}'";

    protected String oldquery = "select crc32(group_concat(concat(table_name, column_name))) from "
            + "information_schema.columns where table_schema = '{schema}'";

    private static final String UPGRADE_DBMS_INTEGRITY_FAIL_CHECK_QUERY =
            "select crc32(SUBSTRING(group_concat(concat(table_name, column_name) order by field(concat(table_name, " +
                    "column_name), 'item_metadatamodified', 'item_metadataname', 'item_metadatapath', " +
                    "'item_metadatasite', 'item_metadataid', 'group_userrecord_last_updated', 'group_usergroup_id', " +
                    "'group_useruser_id', 'groupgroup_description', 'groupgroup_name', 'grouporg_id', " +
                    "'grouprecord_last_updated', 'groupid', 'gitlogprocessed', 'gitlogcommit_id', 'gitlogsite_id', " +
                    "'gitlogid', 'dependencytype', 'dependencytarget_path', 'dependencysource_path', " +
                    "'dependencysite', 'dependencyid', 'clusterheartbeat', 'clustergit_private_key', " +
                    "'clustergit_token', 'clustergit_password', 'clustergit_username', 'clustergit_auth_type', " +
                    "'clustergit_remote_name', 'clustergit_url', 'clusterstate', 'clusterlocal_address', 'clusterid'," +
                    " 'audit_parameterstarget_value', 'audit_parameterstarget_subtype', " +
                    "'audit_parameterstarget_type', 'audit_parameterstarget_id', 'audit_parametersaudit_id', " +
                    "'audit_parametersid', 'auditcluster_node_id', 'auditactor_details', 'auditactor_id', " +
                    "'auditprimary_target_value', 'auditprimary_target_subtype', 'auditprimary_target_type', " +
                    "'auditprimary_target_id', 'auditorigin', 'auditoperation_timestamp', 'auditoperation', " +
                    "'auditsite_id', 'auditorganization_id', 'auditid', '_metastudio_id', '_metaintegrity', " +
                    "'_metaversion') DESC), 1, 1024)) from information_schema.columns where table_schema = '{schema}'" +
                    " ;";

    /**
     * {@inheritDoc}
     */
    @Override
    public void store(final Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(query.replace(SCHEMA, connection.getCatalog()))) {
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                long value = result.getLong(1);
                try(PreparedStatement update = connection.prepareStatement(UPDATE)) {
                    update.setLong(1, value);
                    update.executeUpdate();
                    connection.commit();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Connection connection) throws EntitlementException, SQLException {
        try(Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(QUERY);
            if (result.next()) {
                long stored = result.getLong(1);
                result = statement.executeQuery(query.replace(SCHEMA, connection.getCatalog()));
                if(result.next()) {
                    long actual = result.getLong(1);
                    if(stored != actual) {
                        result = statement.executeQuery(UPGRADE_DBMS_INTEGRITY_FAIL_CHECK_QUERY.replace(SCHEMA,
                                connection.getCatalog()));
                        if (result.next()) {
                            actual = result.getLong(1);
                            if(stored == actual) {
                                return;
                            } else {
                                result = statement.executeQuery(oldquery.replace(SCHEMA, connection.getCatalog()));
                                if (result.next()) {
                                    actual = result.getLong(1);
                                    if (stored == actual) {
                                        return;
                                    }
                                }
                            }
                        }
                        throw new EntitlementException("Incompatible database detected, unable to start");
                    }
                }
            }
        }
    }

}
