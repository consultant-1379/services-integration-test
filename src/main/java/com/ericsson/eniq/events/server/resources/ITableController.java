/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.ericsson.eniq.events.server.test.schema.Nullable;

/**
 * @author ealeerm
 * @since 08/2012
 */
public interface ITableController {
    void createTemporaryTable(String table, Collection<String> columns) throws Exception;

    void createTemporaryTableWithColumnTypes(String table, Map<String, String> columns)
            throws Exception;

    void createTemporaryTable(String table, Map<String, Nullable> rawTableColumns)
                    throws SQLException;

    String createTemporaryFromRealTableAndUseInTemplates(String realTableName, String... columnNames);

    void insertRowIntoTemporaryTable(String temporaryTableName, String... columnValues);

    void insertRow(String table, Map<String, Object> values) throws SQLException;
}
