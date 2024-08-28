/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.server.integritytests.piechart;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ericsson.eniq.events.server.test.sql.SQLCommand;
import com.ericsson.eniq.events.server.test.util.DateTimeUtilities;

/**
 * @since 2011
 */
public class PieChartCauseCodePopulator {
    private static final String EMPTY_SCC_HELP = "";

    protected static final int causeProtTypeSgeh = 17;

    protected static final int causeProtTypeLte = 22;

    protected static final String causeProtType_desc_SGEH = "CPT 1_SGEH";

    protected static final String causeProtType_desc_LTE = "CPT 1_LTE";

    protected static final int ratValue = 1;

    protected static final String vendorValue = "Ericsson";

    protected static final String hierarchy3Value = "bsc1";

    protected static final String hierarchy1Value = "cell1";

    protected static final int causeCode_1_LTE = 10;

    protected static final int causeCode_2_LTE = 20;

    protected static final int causeCode_3_LTE = 30;

    protected static final int causeCode_1_SGEH = 1;

    protected static final int causeCode_2_SGEH = 2;

    protected static final int causeCode_3_SGEH = 3;

    protected static final String causeCode_1_desc_LTE = "CC 1_LTE";

    protected static final String causeCode_2_desc_LTE = "CC 2_LTE";

    protected static final String causeCode_3_desc_LTE = "CC 3_LTE";

    protected static final String causeCode_1_desc_SGEH = "CC 1_SGEH";

    protected static final String causeCode_2_desc_SGEH = "CC 2_SGEH";

    protected static final String causeCode_3_desc_SGEH = "CC 3_SGEH";

    protected static final int subCauseCode_1_SGEH = 1;

    protected static final int subCauseCode_1_LTE = 100;

    protected static final int subCauseCode_2_SGEH = 2;

    protected static final int subCauseCode_2_LTE = 200;

    protected static final String subCauseCode_1_desc_LTE = "SCC 1_LTE";

    protected static final String subCauseCode_2_desc_LTE = "SCC 2_LTE";

    protected static final String subCauseCode_1_desc_SGEH = "SCC 1_SGEH";

    protected static final String subCauseCode_2_desc_SGEH = "SCC 2_SGEH";

    protected static final int TEST_IMSI_1 = 123456;

    protected static final int TEST_IMSI_2 = 123455;

    protected static final int TEST_TAC_1 = 1234567;

    protected static final int TEST_TAC_2 = 1234569;

    protected static final String TEST_APN_1 = "apn_1";

    protected static final String TEST_SGSN_1 = "sgsn_1";

    protected static final String TEST_BSC_1 = hierarchy3Value + "," + vendorValue + "," + ratValue;

    protected static final String TEST_CELL_1 = hierarchy1Value + "," + "," + hierarchy3Value + "," + vendorValue + "," + ratValue;

    private final Connection connection;

    private final SimpleDateFormat dateOnlyFormatter = new SimpleDateFormat(DATE_FORMAT);

    public PieChartCauseCodePopulator(final Connection connection) {
        this.connection = connection;
    }

    public void populateTemporaryTables() throws SQLException, ParseException {
        populateEventTables();
        populateCauseCodeTables();
    }

    private void populateEventTables() throws SQLException, ParseException {
        populateRawTables();
        populateAggTables();
        populateAggTablesAPN();
    }

    private void populateRawTables() throws SQLException, ParseException {
        final String timeStamp = DateTimeUtilities.getDateTimeMinus25Minutes();
        final String localDateId = dateOnlyFormatter.format(dateOnlyFormatter.parse(DateTimeUtilities.getDateTimeMinusDay(3)));
        final String timeStampAgg = DateTimeUtilities.getDateTimeMinus36Hours();
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_1, causeCode_1_SGEH, subCauseCode_1_SGEH, causeProtTypeSgeh, TEST_TAC_1, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_1_SGEH, subCauseCode_1_SGEH, causeProtTypeSgeh, TEST_TAC_1, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_2_SGEH, subCauseCode_2_SGEH, causeProtTypeSgeh, TEST_TAC_2, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_1, causeCode_1_LTE, subCauseCode_1_SGEH, causeProtTypeLte, TEST_TAC_1, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_1, causeCode_1_LTE, subCauseCode_1_SGEH, causeProtTypeLte, TEST_TAC_1, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_SGEH, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_2_SGEH, subCauseCode_2_SGEH, causeProtTypeSgeh, TEST_TAC_2, TEST_APN_1,
                timeStamp, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);

        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_1, causeCode_1_SGEH, subCauseCode_1_SGEH, causeProtTypeSgeh, TEST_TAC_1, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_1_SGEH, subCauseCode_1_SGEH, causeProtTypeSgeh, TEST_TAC_1, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_2_SGEH, subCauseCode_2_SGEH, causeProtTypeSgeh, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_1, causeCode_1_LTE, subCauseCode_1_LTE, causeProtTypeLte, TEST_TAC_1, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_LTE, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_LTE, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_LTE, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_LTE, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_LTE_ERR_RAW, TEST_IMSI_2, causeCode_2_LTE, subCauseCode_2_LTE, causeProtTypeLte, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_2_SGEH, subCauseCode_2_SGEH, causeProtTypeSgeh, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
        insertRowIntoRawTable(TEMP_EVENT_E_SGEH_ERR_RAW, TEST_IMSI_2, causeCode_2_SGEH, subCauseCode_2_SGEH, causeProtTypeSgeh, TEST_TAC_2, TEST_APN_1,
                timeStampAgg, ratValue, vendorValue, hierarchy3Value, hierarchy1Value, TEST_SGSN_1, localDateId);
    }

    private void populateAggTables() throws SQLException {
        final String timeStamp = DateTimeUtilities.getDateTimeMinus25Minutes();
        insertRowIntoAggTable(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_1MIN, causeCode_1_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTable(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_1MIN, causeCode_2_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTable(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_1MIN, causeCode_1_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
        insertRowIntoAggTable(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_1MIN, causeCode_2_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
    }

    private void populateAggTablesAPN() throws SQLException {
        final String timeStamp = DateTimeUtilities.getDateTimeMinus36Hours();
        insertRowIntoAggTableAPN(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_1_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp,
                ratValue, vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_2_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp,
                ratValue, vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_1_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp,
                ratValue, vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_2_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp,
                ratValue, vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_SGEH_APN_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_1_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_SGEH_APN_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_2_SGEH, causeProtTypeSgeh, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_SGEH);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_LTE_APN_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_1_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
        insertRowIntoAggTableAPN(TEMP_EVENT_E_LTE_APN_CC_SCC_ERR_DAY, TEST_APN_1, causeCode_2_LTE, causeProtTypeLte, TEST_SGSN_1, timeStamp, ratValue,
                vendorValue, hierarchy3Value, hierarchy1Value, 2, subCauseCode_1_LTE);
    }

    private void insertRowIntoRawTable(final String table, final int imsi, final int causeCode, final int subcauseCode, final int causeProtoType,
                                       final int tac, final String apn, final String timeStamp, final int rat, final String vendor, final String bsc,
                                       final String cell, final String source, final String localDateId) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(CAUSE_CODE_COLUMN, causeCode);
        valuesForTable.put(SUBCAUSE_CODE_COLUMN, subcauseCode);
        valuesForTable.put(CAUSE_PROT_TYPE_COLUMN, causeProtoType);
        valuesForTable.put(IMSI, imsi);
        valuesForTable.put(TAC, tac);
        valuesForTable.put(APN, apn);
        valuesForTable.put(DATETIME_ID, timeStamp);
        valuesForTable.put(RAT, rat);
        valuesForTable.put(VENDOR_PARAM_UPPER_CASE, vendor);
        valuesForTable.put(HIERARCHY_3, bsc);
        valuesForTable.put(HIERARCHY_1, cell);
        valuesForTable.put(EVENT_SOURCE_NAME, source);
        valuesForTable.put(LOCAL_DATE_ID, localDateId);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void insertRowIntoAggTable(final String table, final int causeCode, final int causeProtoType, final String eventSrc,
                                       final String timeStamp, final int rat, final String vendor, final String bsc, final String cell,
                                       final int numberOfErrors, final int subCauseCode) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(CAUSE_CODE_COLUMN, causeCode);
        valuesForTable.put(CAUSE_PROT_TYPE_COLUMN, causeProtoType);
        valuesForTable.put(EVENT_SOURCE_NAME, eventSrc);
        valuesForTable.put(DATETIME_ID, timeStamp);
        valuesForTable.put(RAT, rat);
        valuesForTable.put(VENDOR_PARAM_UPPER_CASE, vendor);
        valuesForTable.put(HIERARCHY_3, bsc);
        valuesForTable.put(HIERARCHY_1, cell);
        valuesForTable.put(NO_OF_ERRORS, numberOfErrors);
        valuesForTable.put(SUBCAUSE_CODE_COLUMN, subCauseCode);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void insertRowIntoAggTableAPN(final String table, final String apn, final int causeCode, final int causeProtoType, final String eventSrc,
                                          final String timeStamp, final int rat, final String vendor, final String bsc, final String cell,
                                          final int numberOfErrors, final int subCauseCode) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(APN, apn);
        valuesForTable.put(CAUSE_CODE_COLUMN, causeCode);
        valuesForTable.put(CAUSE_PROT_TYPE_COLUMN, causeProtoType);
        valuesForTable.put(EVENT_SOURCE_NAME, eventSrc);
        valuesForTable.put(DATETIME_ID, timeStamp);
        valuesForTable.put(RAT, rat);
        valuesForTable.put(VENDOR_PARAM_UPPER_CASE, vendor);
        valuesForTable.put(HIERARCHY_3, bsc);
        valuesForTable.put(HIERARCHY_1, cell);
        valuesForTable.put(NO_OF_ERRORS, numberOfErrors);
        valuesForTable.put(SUBCAUSE_CODE_COLUMN, subCauseCode);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void populateCauseCodeTables() throws SQLException {
        populateCCTables();
        populateSCCTables();
        populateCPTTables();
    }

    private void populateCCTables() throws SQLException {
        insertRowIntoCCTable(TEMP_DIM_E_SGEH_CAUSECODE, causeCode_1_SGEH, causeProtTypeSgeh, causeCode_1_desc_SGEH);
        insertRowIntoCCTable(TEMP_DIM_E_SGEH_CAUSECODE, causeCode_2_SGEH, causeProtTypeSgeh, causeCode_2_desc_SGEH);
        insertRowIntoCCTable(TEMP_DIM_E_SGEH_CAUSECODE, causeCode_3_SGEH, causeProtTypeSgeh, causeCode_3_desc_SGEH);

        insertRowIntoCCTable(TEMP_DIM_E_LTE_CAUSECODE, causeCode_1_LTE, causeProtTypeLte, causeCode_1_desc_LTE);
        insertRowIntoCCTable(TEMP_DIM_E_LTE_CAUSECODE, causeCode_2_LTE, causeProtTypeLte, causeCode_2_desc_LTE);
        insertRowIntoCCTable(TEMP_DIM_E_LTE_CAUSECODE, causeCode_3_LTE, causeProtTypeLte, causeCode_3_desc_LTE);
    }

    private void populateSCCTables() throws SQLException {
        insertRowIntoSCCTable(TEMP_DIM_E_SGEH_SUBCAUSECODE, subCauseCode_1_SGEH, subCauseCode_1_desc_SGEH, EMPTY_SCC_HELP);
        insertRowIntoSCCTable(TEMP_DIM_E_SGEH_SUBCAUSECODE, subCauseCode_2_SGEH, subCauseCode_2_desc_SGEH, EMPTY_SCC_HELP);

        insertRowIntoSCCTable(TEMP_DIM_E_LTE_SUBCAUSECODE, subCauseCode_1_LTE, subCauseCode_1_desc_LTE, EMPTY_SCC_HELP);
        insertRowIntoSCCTable(TEMP_DIM_E_LTE_SUBCAUSECODE, subCauseCode_2_LTE, subCauseCode_2_desc_LTE, EMPTY_SCC_HELP);
    }

    private void populateCPTTables() throws SQLException {
        insertRowIntoCPTTable(TEMP_DIM_E_SGEH_CAUSE_PROT_TYPE, causeProtTypeSgeh, causeProtType_desc_SGEH);

        insertRowIntoCPTTable(TEMP_DIM_E_LTE_CAUSE_PROT_TYPE, causeProtTypeLte, causeProtType_desc_LTE);
    }

    private void insertRowIntoCCTable(final String table, final int causeCode, final int causeProtoType, final String ccDesc) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(CAUSE_CODE_COLUMN, causeCode);
        valuesForTable.put(CAUSE_PROT_TYPE_COLUMN, causeProtoType);
        valuesForTable.put(CAUSE_CODE_DESC_COLUMN, ccDesc);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void insertRowIntoSCCTable(final String table, final int subCauseCode, final String sccDesc, final String sccHelp) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(SUBCAUSE_CODE_COLUMN, subCauseCode);
        valuesForTable.put(SUB_CAUSE_CODE_DESC_COLUMN, sccDesc);
        valuesForTable.put(SUB_CAUSE_CODE_HELP_COLUMN, sccHelp);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void insertRowIntoCPTTable(final String table, final int causeProtoType, final String cptDesc) throws SQLException {
        final Map<String, Object> valuesForTable = new HashMap<String, Object>();
        valuesForTable.put(CAUSE_PROT_TYPE_COLUMN, causeProtoType);
        valuesForTable.put(CAUSE_PROT_TYPE_DESC_COLUMN, cptDesc);
        new SQLCommand(connection).insertRow(table, valuesForTable);
    }

    private void createTemporaryCCTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(CAUSE_CODE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN);
        columnsForTable.add(CAUSE_CODE_DESC_COLUMN);
        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_SGEH_CAUSECODE, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_LTE_CAUSECODE, columnsForTable);
    }

    private void createTemporarySCCTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(SUBCAUSE_CODE_COLUMN);
        columnsForTable.add(SUB_CAUSE_CODE_DESC_COLUMN);
        columnsForTable.add(SUB_CAUSE_CODE_HELP_COLUMN);

        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_SGEH_SUBCAUSECODE, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_LTE_SUBCAUSECODE, columnsForTable);
    }

    private void createTemporarySCCAdviceTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(CAUSE_CODE_COLUMN);
        columnsForTable.add(SUBCAUSE_CODE_COLUMN);
        columnsForTable.add(ADVICE_COLUMN);
        new SQLCommand(connection).createTemporaryTable("#DIM_E_SGEH_CC_SCC", columnsForTable);
    }

    private void createTemporaryCPTTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_DESC_COLUMN);
        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_SGEH_CAUSE_PROT_TYPE, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_DIM_E_LTE_CAUSE_PROT_TYPE, columnsForTable);
    }

    private void createTemporaryAggTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(CAUSE_CODE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN_NAME);
        columnsForTable.add(EVENT_SOURCE_NAME);
        columnsForTable.add(DATETIME_ID);
        columnsForTable.add(RAT);
        columnsForTable.add(VENDOR_PARAM_UPPER_CASE);
        columnsForTable.add(HIERARCHY_3);
        columnsForTable.add(HIERARCHY_1);
        columnsForTable.add(NO_OF_ERRORS);
        columnsForTable.add(SUBCAUSE_CODE_COLUMN);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_1MIN, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_1MIN, columnsForTable);

    }

    private void createTemporaryAggTablesAPN() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(APN);
        columnsForTable.add(CAUSE_CODE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN_NAME);
        columnsForTable.add(EVENT_SOURCE_NAME);
        columnsForTable.add(DATETIME_ID);
        columnsForTable.add(RAT);
        columnsForTable.add(VENDOR_PARAM_UPPER_CASE);
        columnsForTable.add(HIERARCHY_3);
        columnsForTable.add(HIERARCHY_1);
        columnsForTable.add(NO_OF_ERRORS);
        columnsForTable.add(SUBCAUSE_CODE_COLUMN);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_SGEH_EVNTSRC_CC_SCC_ERR_DAY, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_LTE_EVNTSRC_CC_SCC_ERR_DAY, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_LTE_APN_CC_SCC_ERR_DAY, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_SGEH_APN_CC_SCC_ERR_DAY, columnsForTable);

    }

    private void createTemporaryRawTables() throws Exception {
        final List<String> columnsForTable = new ArrayList<String>();
        columnsForTable.add(CAUSE_CODE_COLUMN);
        columnsForTable.add(SUBCAUSE_CODE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN);
        columnsForTable.add(CAUSE_PROT_TYPE_COLUMN_NAME);
        columnsForTable.add(IMSI);
        columnsForTable.add(TAC);
        columnsForTable.add(APN);
        columnsForTable.add(DATETIME_ID);
        columnsForTable.add(RAT);
        columnsForTable.add(VENDOR_PARAM_UPPER_CASE);
        columnsForTable.add(HIERARCHY_3);
        columnsForTable.add(HIERARCHY_1);
        columnsForTable.add(EVENT_SOURCE_NAME);
        columnsForTable.add(LOCAL_DATE_ID);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_SGEH_ERR_RAW, columnsForTable);
        new SQLCommand(connection).createTemporaryTable(TEMP_EVENT_E_LTE_ERR_RAW, columnsForTable);
    }

    public void createTemporaryTables() throws Exception {
        createTemporaryCCTables();
        createTemporarySCCTables();
        createTemporarySCCAdviceTables();
        createTemporaryCPTTables();
        createTemporaryRawTables();
        createTemporaryAggTables();
        createTemporaryAggTablesAPN();
    }
}