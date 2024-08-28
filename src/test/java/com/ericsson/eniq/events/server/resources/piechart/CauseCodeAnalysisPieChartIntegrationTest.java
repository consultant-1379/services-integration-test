/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.piechart;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.resources.DataServiceBaseTestCase;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This test class is designed
 * 
 * for  all possible parameters 
 *      and all methods 
 *      and all possible paths (both java code and velocity templates)
 * 
 * of   #CauseCodeAnalysisPieChartAPI 
 *      and #CauseCodeAnalysisPieChartResource
 * 
 * @author eavidat
 * @since 2011
 *
 */
public class CauseCodeAnalysisPieChartIntegrationTest extends DataServiceBaseTestCase {

    private static final String TEST_VALUE_CAUSE_PROT_TYPE = "1";

    private MultivaluedMap<String, String> map;

    private CauseCodeAnalysisPieChartResource causeCodeAnalysisPieChartResource;

    private CauseCodeAnalysisPieChartAPI causeCodeAnalysisPieChartAPI;

    @Override
    public void onSetUp() {
        this.causeCodeAnalysisPieChartResource = new CauseCodeAnalysisPieChartResource();
        this.causeCodeAnalysisPieChartAPI = new CauseCodeAnalysisPieChartAPI();
        map = new MultivaluedMapImpl();
        attachDependencies(causeCodeAnalysisPieChartResource);
        this.causeCodeAnalysisPieChartAPI.causeCodeAnalysisPieChartResource = this.causeCodeAnalysisPieChartResource;
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an APN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByApn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_APN);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an APN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByApnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_APN_GROUP);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an SGSN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListBySgsn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_SGSN);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an SGSN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListBySgsnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_SGSN_GROUP);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an BSC type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByBsc() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_BSC);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an BSC group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByBscGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_BSC_GROUP);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an CELL type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByCell() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_CELL);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code list API {@see CauseCodeAnalysisPieChartAPI#getCausecodeList()}
     * for all time combinations and 
     * for an CELL group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeListByCellGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_CELL_GROUP);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCausecodeList());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an APN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByApn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_APN);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an APN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByApnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_APN_GROUP);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an SGSN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisBySgsn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_SGSN);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an SGSN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisBySgsnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_APN_GROUP);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an BSC type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByBsc() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_BSC);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an BSC group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByBscGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_BSC_GROUP);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an CELL type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByCell() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_CELL);
            map.putSingle(TIME_QUERY_PARAM, time);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getCauseCodeAnalysis()}
     * for all time combinations and 
     * for an CELL group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetCauseCodeAnalysisByCellGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_CELL_GROUP);
            map.putSingle(TIME_QUERY_PARAM, time);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        }
    }

    @Test
    public void testGetCauseCodeAnalysisByWeek() throws Exception {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(NODE_PARAM, "1");
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(DISPLAY_PARAM, CHART_PARAM);
            map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
            map.putSingle(CAUSE_CODE_IDS_PARAM, TEST_VALUE_CAUSE_CODE_IDS);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());

    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for APN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByApn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_APN);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    @Test
    public void testGetSubCauseCodeAnalysisByApnWeek() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_APN);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for APN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByApnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_APN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_APN_GROUP);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for SGSN type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisBySgsn() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_SGSN);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for SGSN group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisBySgsnGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_SGSN);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_SGSN_GROUP);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for BSC type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByBsc() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_BSC);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for BSC group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByBscGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_BSC);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_BSC_GROUP);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for CELL type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByCell() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(NODE_PARAM, TEST_VALUE_CELL);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the sub cause code analysis API {@see CauseCodeAnalysisPieChartAPI#getSubCauseCodeAnalysis()}
     * for all time combinations and 
     * for CELL group type 
     * 
     * @throws Exception
     */
    @Test
    public void testGetSubCauseCodeAnalysisByCellGroup() throws Exception {
        for (final String time : TIME_INPUTS_GRID_VIEW) {
            map.clear();
            map.putSingle(TYPE_PARAM, TYPE_CELL);
            map.putSingle(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET);
            map.putSingle(GROUP_NAME_PARAM, TEST_VALUE_CELL_GROUP);
            map.putSingle(CAUSE_CODE_PARAM, TEST_VALUE_CAUSE_CODE);
            map.putSingle(CAUSE_PROT_TYPE_PARAM, TEST_VALUE_CAUSE_PROT_TYPE);
            map.putSingle(TIME_QUERY_PARAM, time);

            DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);
            assertJSONSucceeds(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        }
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with type missing 
     * 
     * @throws Exception
     */
    @Test
    public void testTypeAbsence() throws Exception {
        map.clear();
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        String testString = causeCodeAnalysisPieChartAPI.getCausecodeList();
        assertResultContains(testString, TYPE_PARAM);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        testString = causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis();
        assertResultContains(testString, TYPE_PARAM);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        testString = causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis();
        assertResultContains(testString, TYPE_PARAM);
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid type 
     * 
     * @throws Exception
     */
    @Test
    public void testTypeValidity() throws Exception {
        map.clear();
        map.putSingle(TYPE_PARAM, TYPE_IMSI);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        String testString = causeCodeAnalysisPieChartAPI.getCausecodeList();
        assertResultContains(testString, TYPE_PARAM);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        testString = causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis();
        assertResultContains(testString, TYPE_PARAM);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
        testString = causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis();
        assertResultContains(testString, TYPE_PARAM);
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid APN 
     * 
     * @throws Exception
     */
    @Test
    public void testAPNValidity() throws Exception {
        map.clear();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(NODE_PARAM, "+" + TEST_VALUE_APN);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid BSC 
     * 
     * @throws Exception
     */
    @Test
    public void testBSCValidity() throws Exception {
        map.clear();
        map.putSingle(TYPE_PARAM, TYPE_BSC);
        map.putSingle(NODE_PARAM, "+" + TEST_VALUE_BSC);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid CELL 
     * 
     * @throws Exception
     */
    @Test
    public void testCELLValidity() throws Exception {
        map.clear();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(NODE_PARAM, "+" + TEST_VALUE_CELL);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid SGSN 
     * 
     * @throws Exception
     */
    @Test
    public void testSGSNValidity() throws Exception {
        map.clear();
        map.putSingle(TYPE_PARAM, TYPE_SGSN);
        map.putSingle(NODE_PARAM, "+" + TEST_VALUE_SGSN);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
    }

    /**
     * This method tests the API #CauseCodeAnalysisPieChartAPI with invalid group 
     * 
     * @throws Exception
     */
    @Test
    public void testGroupNameValidity() throws Exception {
        map.clear();
        map.putSingle(GROUP_NAME_PARAM, "+" + TEST_VALUE_APN);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCausecodeList());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getCauseCodeAnalysis());
        assertJSONErrorResult(causeCodeAnalysisPieChartAPI.getSubCauseCodeAnalysis());
    }
}