/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.utils.techpacks.timerangequeries.impl;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.common.TechPackData.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ericsson.eniq.events.server.datasource.DBConnectionManager;
import com.ericsson.eniq.events.server.integritytests.stubs.InterceptedDBConnectionManager;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.query.DataServiceQueryExecutor;
import com.ericsson.eniq.events.server.test.database.DatabaseConnectionHelper;
import com.ericsson.eniq.events.server.test.sql.SQLCommand;
import com.ericsson.eniq.events.server.test.util.DateTimeUtilities;
import com.ericsson.eniq.events.server.utils.DateTimeRange;
import com.ericsson.eniq.events.server.utils.FormattedDateTimeRange;

/**
 * Class to test the SQL queries in the from end to end. Class does not extend the usual BaseDataIntegrityTest or BaseServiceIntegrationTest as it is
 * not testing a service
 * 
 * @author eemecoy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/ericsson/eniq/events/server/resources/BaseDataIntegrityTest-context.xml" })
public class EventsTPTimerangeQuerierDataTest {

    private InterceptedDBConnectionManager interceptedDbConnectionManager;

    @Autowired
    private DBConnectionManager dbConnectionManager;

    @Autowired
    private DBConnectionManager dwhRepDbConnectionManager;

    @Autowired
    private DataServiceQueryExecutor queryExecutor;

    @Autowired
    private EventsTechPackTimerangeQuerier timerangeQuerier;

    @Before
    public void onSetUp() {
        setupTracing();
    }

    private void setupTracing() {
        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
    }

    private void populateRawTables(final String view, final String minDate, final String maxDate, final Connection connection,
                                   final List<String> rawTableNames) throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        for (final String rawTableName : rawTableNames) {
            values.put(RAW_TABLE_NAME_COLUMN, rawTableName);
            values.put(MIN_DATE, minDate);
            values.put(MAX_DATE, maxDate);
            new SQLCommand(connection).insertRow(getMatchingTimerangeView(view), values);
        }
    }

    private Connection setUpInterceptedDbConnectionManager() throws Exception {
        final Connection connection = DatabaseConnectionHelper.getDBConnection();
        interceptedDbConnectionManager = new InterceptedDBConnectionManager(connection);
        interceptedDbConnectionManager.setDwhrepDataSource(dbConnectionManager.getDwhrepDataSource());
        queryExecutor.setDbConnectionManager(interceptedDbConnectionManager);
        return connection;
    }

    private void setUpInterceptedDwhRepDbConnectionManager() throws Exception {
        final Connection connection = DatabaseConnectionHelper.getDWHREPDBConnection();
        interceptedDbConnectionManager = new InterceptedDBConnectionManager(connection);
        interceptedDbConnectionManager.setDwhrepDataSource(dwhRepDbConnectionManager.getDwhrepDataSource());
        queryExecutor.setDbConnectionManager(interceptedDbConnectionManager);
    }

    private void createRawTables(String timeRangeView, final Connection connection) throws Exception {
        timeRangeView = getMatchingTimerangeView(timeRangeView);
        final Collection<String> columns = new ArrayList<String>();
        columns.add(RAW_TABLE_NAME_COLUMN);
        columns.add(MAX_DATE);
        columns.add(MIN_DATE);
        new SQLCommand(connection).createTemporaryTable(timeRangeView, columns);

    }

    @Test
    public void testGetRawTablesForTimerange() throws Exception {
        final Connection connection = setUpInterceptedDbConnectionManager();
        createRawTables(EVENT_E_LTE_ERR_RAW, connection);
        final List<String> rawTables = new ArrayList<String>();
        populateRawTables(EVENT_E_LTE_ERR_RAW, DateTimeUtilities.getDateTimeMinus36Hours(), DateTimeUtilities.getDateTimeMinus25Minutes(),
                connection, rawTables);
        final FormattedDateTimeRange dateTimeRange = DateTimeRange.getFormattedDateTimeRangeWithOutOffset(
                DateTimeUtilities.getDateTimeMinus55Minutes(), DateTimeUtilities.getDateTimeMinus35Minutes());
        final List<String> result = timerangeQuerier.getRAWTablesUsingQuery(dateTimeRange, EVENT_E_LTE_ERR_RAW);
        assertThat(result, is(rawTables));
    }

    @Test
    public void testGetLatestRawTables_EVENT_E_LTE_RAW() throws Exception {
        final Connection connection = setUpInterceptedDbConnectionManager();
        createRawTables(EVENT_E_LTE_RAW, connection);

        final List<String> rawTablesForOlderTimeRange = new ArrayList<String>();
        rawTablesForOlderTimeRange.add("EVENT_E_LTE_ERR_RAW_98");
        rawTablesForOlderTimeRange.add("EVENT_E_LTE_ERR_RAW_99");
        rawTablesForOlderTimeRange.add("EVENT_E_LTE_SUC_RAW_98");
        rawTablesForOlderTimeRange.add("EVENT_E_LTE_SUC_RAW_99");
        populateRawTables(EVENT_E_LTE_RAW, DateTimeUtilities.getDateTimeMinus36Hours(), DateTimeUtilities.getDateTimeMinus48Hours(), connection,
                rawTablesForOlderTimeRange);
        final List<String> rawTablesForLatestSucTimeRange = new ArrayList<String>();
        rawTablesForLatestSucTimeRange.add("EVENT_E_LTE_SUC_RAW_01");
        rawTablesForLatestSucTimeRange.add("EVENT_E_LTE_SUC_RAW_02");
        populateRawTables(EVENT_E_LTE_RAW, DateTimeUtilities.getDateTimeMinus25Minutes(), DateTimeUtilities.getDateTimeMinus30Minutes(), connection,
                rawTablesForLatestSucTimeRange);
        final List<String> rawTablesForLatestErrTimeRange = new ArrayList<String>();
        rawTablesForLatestErrTimeRange.add("EVENT_E_LTE_ERR_RAW_03");
        rawTablesForLatestErrTimeRange.add("EVENT_E_LTE_ERR_RAW_04");
        populateRawTables(EVENT_E_LTE_RAW, DateTimeUtilities.getDateTimeMinus55Minutes(), DateTimeUtilities.getDateTimeMinus55Minutes(), connection,
                rawTablesForLatestErrTimeRange);

        final List<String> result = timerangeQuerier.getLatestTablesUsingQuery(EVENT_E_LTE_RAW);
        final List<String> expectedTables = new ArrayList<String>();
        expectedTables.addAll(rawTablesForLatestSucTimeRange);
        expectedTables.addAll(rawTablesForLatestErrTimeRange);
        assertThat(result, is(expectedTables));
    }

    @Test
    public void testGetLatestRawTables_EVENT_E_DVTP_DT_RAW() throws Exception {
        final Connection connection = setUpInterceptedDbConnectionManager();
        createRawTables(EVENT_E_DVTP_DT_RAW, connection);
        final List<String> rawTablesForOlderTimeRange = new ArrayList<String>();
        rawTablesForOlderTimeRange.add("EVENT_E_GSN_DT_RAW_01");
        rawTablesForOlderTimeRange.add("EVENT_E_GSN_DT_RAW_01");
        populateRawTables(EVENT_E_DVTP_DT_RAW, DateTimeUtilities.getDateTimeMinus36Hours(), DateTimeUtilities.getDateTimeMinus48Hours(), connection,
                rawTablesForOlderTimeRange);
        final List<String> rawTablesForLatestTimeRange = new ArrayList<String>();
        rawTablesForLatestTimeRange.add("EVENT_E_GSN_DT_RAW_99");
        rawTablesForLatestTimeRange.add("EVENT_E_GSN_DT_RAW_98");
        populateRawTables(EVENT_E_DVTP_DT_RAW, DateTimeUtilities.getDateTimeMinus25Minutes(), DateTimeUtilities.getDateTimeMinus30Minutes(),
                connection, rawTablesForLatestTimeRange);
        final List<String> result = timerangeQuerier.getLatestTablesUsingQuery(EVENT_E_DVTP_DT_RAW);
        assertThat(result, is(rawTablesForLatestTimeRange));
    }

    @Test
    public void testGetLatestRawTables_EVENT_E_SGEH_ERR_RAW() throws Exception {
        final Connection connection = setUpInterceptedDbConnectionManager();
        createRawTables(EVENT_E_SGEH_ERR_RAW, connection);
        final List<String> rawTablesForOlderTimeRange = new ArrayList<String>();
        rawTablesForOlderTimeRange.add("EVENT_E_SGEH_ERR_RAW_98");
        rawTablesForOlderTimeRange.add("EVENT_E_SGEH_ERR_RAW_99");
        populateRawTables(EVENT_E_SGEH_ERR_RAW, DateTimeUtilities.getDateTimeMinus36Hours(), DateTimeUtilities.getDateTimeMinus48Hours(), connection,
                rawTablesForOlderTimeRange);
        final List<String> rawTablesForLatestTimeRange = new ArrayList<String>();
        rawTablesForLatestTimeRange.add("EVENT_E_SGEH_ERR_RAW_01");
        rawTablesForLatestTimeRange.add("EVENT_E_SGEH_ERR_RAW_02");
        populateRawTables(EVENT_E_SGEH_ERR_RAW, DateTimeUtilities.getDateTimeMinus25Minutes(), DateTimeUtilities.getDateTimeMinus30Minutes(),
                connection, rawTablesForLatestTimeRange);
        final List<String> result = timerangeQuerier.getLatestTablesUsingQuery(EVENT_E_SGEH_ERR_RAW);
        assertThat(result, is(rawTablesForLatestTimeRange));
    }

    @Test
    public void testGetLatestRawTables_EVENT_E_RAN_CFA_RAW() throws Exception {
        final Connection connection = setUpInterceptedDbConnectionManager();
        createRawTables(EVENT_E_RAN_CFA_RAW, connection);
        final List<String> rawTablesForOlderTimeRange = new ArrayList<String>();
        rawTablesForOlderTimeRange.add("EVENT_E_RAN_CFA_RAW_98");
        rawTablesForOlderTimeRange.add("EVENT_E_RAN_CFA_RAW_99");
        populateRawTables(EVENT_E_RAN_CFA_RAW, DateTimeUtilities.getDateTimeMinus36Hours(), DateTimeUtilities.getDateTimeMinus25Minutes(),
                connection, rawTablesForOlderTimeRange);
        final List<String> result = timerangeQuerier.getLatestTablesUsingQuery(EVENT_E_RAN_CFA_RAW);
        assertThat(result, is(rawTablesForOlderTimeRange));
    }

    @Test
    public void testGetLatestRawTables_DC_Z_ALARM_INFO_RAW() throws Exception {
        setUpInterceptedDwhRepDbConnectionManager();
        final String rawTable = "DC_Z_ALARM_INFO_RAW";
        final List<String> result = timerangeQuerier.getLatestTablesUsingQuery(DC_Z_ALARM_INFO_RAW);
        assertNotNull(result);
        for (final String table : result) {
            assertTrue(table.startsWith(rawTable));
        }
    }

    @Test
    public void testGetRawTables_DC_Z_ALARM_INFO_RAW() throws Exception {
        setUpInterceptedDwhRepDbConnectionManager();
        final String rawTable = "DC_Z_ALARM_INFO_RAW";
        final FormattedDateTimeRange dateTimeRange = DateTimeRange.getFormattedDateTimeRangeWithOutOffset(DateTimeUtilities.getDateTimeMinusDay(7),
                DateTimeUtilities.getDateTimeMinus35Minutes());
        final List<String> result = timerangeQuerier.getRAWTablesUsingQuery(dateTimeRange, DC_Z_ALARM_INFO_RAW);
        assertNotNull(result);
        for (final String table : result) {
            assertTrue(table.startsWith(rawTable));
        }
    }

    private String getMatchingTimerangeView(final String view) {
        return "#" + view + "_TIMERANGE";
    }

    /**
     * @param dbConnectionManager the dbConnectionManager to set
     */
    public void setDbConnectionManager(final DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    public void setQueryExecutor(final DataServiceQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

}
