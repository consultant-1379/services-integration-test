/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.server.integritytests.liveload;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.json.*;
import com.ericsson.eniq.events.server.resources.LiveLoadResource;
import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.test.sql.SQLExecutor;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class LiveLoadWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase {

    private LiveLoadResource liveLoadResource;

    private final static List<String> tempTables = new ArrayList<String>();

    private final static List<String> wcdmaTempTables = new ArrayList<String>();

    private final static List<String> handsetMakeTempTables = new ArrayList<String>();

    private final static List<String> mscTables = new ArrayList<String>();

    private static final String DIM_Z_SGEH_HIER321 = "#DIM_Z_SGEH_HIER321";

    private static final String DIM_E_SGEH_HIER321 = "#DIM_E_SGEH_HIER321";

    private static final String DIM_E_LTE_HIER321 = "#DIM_E_LTE_HIER321";

    private static final String DIM_E_SGEH_HIER321_CELL = "#DIM_E_SGEH_HIER321_CELL";

    private static final String DIM_Z_SGEH_HIER321_CELL = "#DIM_Z_SGEH_HIER321_CELL";

    private static final String GSMBSC2 = "GSMBSC2";

    private static final String BERBS2 = "BERBS2";

    private static final String GLTECELL2 = "GLTECELL2";

    private static final String CELL2 = "CELL2";

    private static final String TAC_1002151 = "1002151";

    private static final String MARKETINGNAME_CF788 = "CF788";

    private static final String MARKETINGNAME_CF788_WITH_QUOTES_INSERT = MARKETINGNAME_CF788.substring(0, 3)+"''"+MARKETINGNAME_CF788.substring(3, MARKETINGNAME_CF788.length());

    private static final String MARKETINGNAME_CF788_WITH_QUOTES_VERIFY = MARKETINGNAME_CF788.substring(0, 3)+"'"+MARKETINGNAME_CF788.substring(3, MARKETINGNAME_CF788.length());

    private static final String BSCWITH_QUOTES_INSERT = BSC1.substring(0, 3)+"''"+BSC1.substring(3, BSC1.length());

    private static final String BERBS2WITH_QUOTES_INSERT = BERBS2.substring(0, 4)+"''"+BERBS2.substring(4, BERBS2.length());

    private static final String BSCWITH_QUOTES_VERIFY = BSC1.substring(0, 3)+"'"+BSC1.substring(3, BSC1.length());

    private static final String BERBS2WITH_QUOTES_VERIFY = BERBS2.substring(0, 4)+"'"+BERBS2.substring(4, BERBS2.length());

    final static String transId = "transId0";
    static {
        tempTables.add(DIM_Z_SGEH_HIER321);
        tempTables.add(DIM_E_SGEH_HIER321);
        tempTables.add(DIM_E_LTE_HIER321);
        mscTables.add(TEMP_DIM_E_MSS_EVNTSRC);
        wcdmaTempTables.add(DIM_E_SGEH_HIER321_CELL);
        wcdmaTempTables.add(DIM_Z_SGEH_HIER321_CELL);
        handsetMakeTempTables.add(DIM_E_SGEH_TAC);
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        liveLoadResource = new MockLiveLoadResource();
        attachDependencies(liveLoadResource);
    }

    @Test
    public void testGetLiveLoad_Controller() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTables();
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_BSC);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "BSC");
        assertThat(liveLoadResult.size(), is(4));
        assertThat(liveLoadResult.contains(ERBS1 + "," + ERICSSON + "," + LTE), is(true));
        assertThat(liveLoadResult.contains(BERBS2 + "," + ERICSSON + COMMA + LTE), is(true));
        assertThat(liveLoadResult.contains(BSC1 + "," + ERICSSON + COMMA + GSM), is(true));
        assertThat(liveLoadResult.contains(GSMBSC2 + "," + ERICSSON + COMMA + GSM), is(true));
    }

    @Test
    public void testGetLiveLoadMsc() throws Exception {
        ReplaceTablesWithTempTablesTemplateUtils.addTableNameToReplace(DIM_E_MSS_EVNTSRC, TEMP_DIM_E_MSS_EVNTSRC);
        createMscTables();
        populateMscTables();
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadMsc();
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "MSC");
        assertThat(liveLoadResult.size(), is(1));
        assertThat(liveLoadResult.contains(ERICSSON), is(true));
    }

    @Test
    public void testGetLiveLoad_Controller_withQuote() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTablesWithQuotes();
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, BERBS2WITH_QUOTES_VERIFY);
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_BSC);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "BSC");
        assertThat(liveLoadResult.size(), is(1));
        assertThat(liveLoadResult.contains(BERBS2WITH_QUOTES_VERIFY + "," + ERICSSON + "," + LTE), is(true));
    }

    @Test
    public void testGetLiveLoad_Controller_NodePrefixProvided() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTables();
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(LIVELOAD_QUERY_PARAM, "B");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_BSC);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "BSC");
        assertThat(liveLoadResult.size(), is(4));
        assertThat(liveLoadResult.contains(BERBS2 + "," + ERICSSON + COMMA + LTE), is(true));
        assertThat(liveLoadResult.contains(BSC1 + "," + ERICSSON + COMMA + GSM), is(true));
    }

    @Test
    public void testGetLiveLoad_Cell() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTables();

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_CELL);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "CELL");
        assertThat(liveLoadResult.size(), is(4));
        assertThat(liveLoadResult.contains(LTECELL1 + "," + "" + "," + ERBS1 + "," + ERICSSON + COMMA + LTE), is(true));
        assertThat(liveLoadResult.contains(GLTECELL2 + "," + "" + "," + BERBS2 + "," + ERICSSON + COMMA + LTE),
                is(true));
        assertThat(liveLoadResult.contains(GSMCELL1 + "," + "" + "," + BSC1 + "," + ERICSSON + COMMA + GSM), is(true));
        assertThat(liveLoadResult.contains(CELL2 + "," + "" + "," + GSMBSC2 + "," + ERICSSON + COMMA + GSM), is(true));

    }

    @Test
    public void testGetLiveLoad_CellWithQuotes() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTablesWithQuotes();

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, BSCWITH_QUOTES_VERIFY);
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_CELL);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "CELL");
        assertThat(liveLoadResult.size(), is(1));
        assertThat(liveLoadResult.contains(GSMCELL1 + "," + "" + "," + BSCWITH_QUOTES_VERIFY + "," + ERICSSON + COMMA + GSM), is(true));
    }

    @Test
    public void testGetLiveLoad_Cell_CellPrefixProvided() throws Exception {
        createTempTables();
        createWCDMATempTables();
        populateTempTables();

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "G");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(TYPE_CELL);
        System.out.println(json);
        final List<String> liveLoadResult = translateResult(json, "CELL");
        assertThat(liveLoadResult.size(), is(3));

        assertThat(liveLoadResult.contains(GLTECELL2 + "," + "" + "," + BERBS2 + "," + ERICSSON + COMMA + LTE),
                is(true));
        assertThat(liveLoadResult.contains(GSMCELL1 + "," + "" + "," + BSC1 + "," + ERICSSON + COMMA + GSM), is(true));

    }

    @Test
    public void testGetLiveLoad_HandSetMakes_QueryWithComma() throws Exception {
        ReplaceTablesWithTempTablesTemplateUtils.addTableNameToReplace(DIM_E_SGEH_TAC, TEMP_DIM_E_SGEH_TAC);

        createHandsetMakeTempTables();
        populateHandsetMakeTempTables();

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();

        map.putSingle(LIVELOAD_CALLBACK_PARAM, transId);
        map.putSingle(JSON_ID, ERICSSON);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "10");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "CF788,");

        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadHandsetMakes();
        System.out.println(json);

        final List<String> liveLoadResult = translateResult(json, ERICSSON);

        assertThat(liveLoadResult.size(), is(1));
        assertThat(liveLoadResult.contains(MARKETINGNAME_CF788 + COMMA + TAC_1002151), is(true));
    }

    @Test
    public void testGetLiveLoad_HandSetMakes_QueryWithQuotes() throws Exception {
        ReplaceTablesWithTempTablesTemplateUtils.addTableNameToReplace(DIM_E_SGEH_TAC, TEMP_DIM_E_SGEH_TAC);

        createHandsetMakeTempTables();
        populateHandsetMakeTempTablesQuotes();

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();

        map.putSingle(LIVELOAD_CALLBACK_PARAM, transId);
        map.putSingle(JSON_ID, ERICSSON);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "10");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, MARKETINGNAME_CF788_WITH_QUOTES_VERIFY);

        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadHandsetMakes();
        System.out.println(json);

        final List<String> liveLoadResult = translateResult(json, ERICSSON);

        assertThat(liveLoadResult.size(), is(1));
        assertThat(liveLoadResult.contains(MARKETINGNAME_CF788_WITH_QUOTES_VERIFY + COMMA + TAC_1002151), is(true));
    }

    private List<String> translateResult(final String json, final String keyInJSON) throws JSONException {
        final String realJson = stripOffNonJsonWrapper(json);
        final JSONObject resultAsJSONObject = new JSONObject(realJson);
        final JSONArray nodes = (JSONArray) resultAsJSONObject.get(keyInJSON);

        final List<String> results = new ArrayList<String>();
        for (int i = 0; i < nodes.length(); i++) {
            final JSONObject liveLoadResult = (JSONObject) nodes.get(i);
            results.add(liveLoadResult.getString("id"));
        }
        return results;
    }

    private String stripOffNonJsonWrapper(final String json) {
        return json.substring(json.indexOf(transId) + transId.length() + 1, json.length() - 1);
    }

    private void populateTempTables() throws SQLException {
        SQLExecutor sqlExecutor = null;
        try {
            sqlExecutor = new SQLExecutor(connection);

            sqlExecutor.executeUpdate("insert into " + DIM_E_SGEH_HIER321 + " values('','" + BSC1 + "','" + "" + "','"
                    + GSMCELL1 + "',0,'" + ERICSSON + "')");
            sqlExecutor.executeUpdate("insert into " + DIM_E_SGEH_HIER321 + " values('','" + GSMBSC2 + "','" + ""
                    + "','" + CELL2 + "',0,'" + ERICSSON + "')");
            sqlExecutor.executeUpdate("insert into " + DIM_E_LTE_HIER321 + " values('','" + ERBS1 + "','" + "" + "','"
                    + LTECELL1 + "',2,'" + ERICSSON + "')");
            sqlExecutor.executeUpdate("insert into " + DIM_E_LTE_HIER321 + " values('','" + BERBS2 + "','" + "" + "','"
                    + GLTECELL2 + "',2,'" + ERICSSON + "')");
        } finally {
            if (sqlExecutor != null) {
                sqlExecutor.close();
            }
        }

    }

    private void populateTempTablesWithQuotes() throws SQLException {
        SQLExecutor sqlExecutor = null;
        try {
            sqlExecutor = new SQLExecutor(connection);

            sqlExecutor.executeUpdate("insert into " + DIM_E_SGEH_HIER321 + " values('','" + BSCWITH_QUOTES_INSERT + "','" + "" + "','"
                    + GSMCELL1 + "',0,'" + ERICSSON + "')");
            sqlExecutor.executeUpdate("insert into " + DIM_E_LTE_HIER321 + " values('','" + BERBS2WITH_QUOTES_INSERT + "','" + "" + "','"
                    + GLTECELL2 + "',2,'" + ERICSSON + "')");
        } finally {
            if (sqlExecutor != null) {
                sqlExecutor.close();
            }
        }

    }

    private void populateHandsetMakeTempTables() throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(MARKETING_NAME, MARKETINGNAME_CF788);
        values.put(MANUFACTURER, ERICSSON);
        values.put(TAC, TAC_1002151);
        insertRow(TEMP_DIM_E_SGEH_TAC, values);
    }

    private void populateHandsetMakeTempTablesQuotes() throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(MARKETING_NAME, MARKETINGNAME_CF788_WITH_QUOTES_INSERT);
        values.put(MANUFACTURER, ERICSSON);
        values.put(TAC, TAC_1002151);
        insertRow(TEMP_DIM_E_SGEH_TAC, values);
    }

    private void populateMscTables() throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(EVENT_SOURCE_NAME, ERICSSON);
        insertRow(TEMP_DIM_E_MSS_EVNTSRC, values);
    }

    private void createTempTables() throws SQLException {
        for (final String tempTable : tempTables) {
            SQLExecutor sqlExecutor = null;
            try {
                sqlExecutor = new SQLExecutor(connection);
                sqlExecutor
                        .executeUpdate("create local temporary table "
                                + tempTable
                                + "(ERBS_ID varchar(12), HIERARCHY_3 varchar(128), HIERARCHY_2 varchar(128),HIERARCHY_1 varchar(128), RAT tinyint, VENDOR varchar(20))");

            } finally {
                if (sqlExecutor != null) {
                    sqlExecutor.close();
                }
            }
        }

    }

    private void createWCDMATempTables() throws SQLException {
        for (final String tempTable : wcdmaTempTables) {
            SQLExecutor sqlExecutor = null;
            try {
                sqlExecutor = new SQLExecutor(connection);
                sqlExecutor
                        .executeUpdate("create local temporary table "
                                + tempTable
                                + "(ERBS_ID varchar(12), HIERARCHY_3 varchar(128), HIERARCHY_2 varchar(128),CELL_ID varchar(128), RAT tinyint, VENDOR varchar(20))");

            } finally {
                if (sqlExecutor != null) {
                    sqlExecutor.close();
                }
            }
        }

    }

    private void createMscTables() throws SQLException {
        for (final String tempTable : mscTables) {
            SQLExecutor sqlExecutor = null;
            try {
                sqlExecutor = new SQLExecutor(connection);
                sqlExecutor
                        .executeUpdate("create local temporary table "
                                + tempTable
                                + "(EVENT_SOURCE_NAME varchar(20))");

            } finally {
                if (sqlExecutor != null) {
                    sqlExecutor.close();
                }
            }
        }

    }

    private void createHandsetMakeTempTables() throws Exception {
        final Collection<String> columns = new ArrayList<String>();
        columns.add(MARKETING_NAME);
        columns.add(MANUFACTURER);
        columns.add(TAC);
        createTemporaryTable(TEMP_DIM_E_SGEH_TAC, columns);
    }

    private class MockLiveLoadResource extends LiveLoadResource {
       @Override
       protected List<String> getTechPackCXCNumbers(String techpackName){
            List<String> list = new ArrayList<String>();
            list.add(techpackName);
            return list;
        }
    }

}
