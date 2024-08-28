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
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.resources.piechart.CauseCodeAnalysisPieChartResource;
import com.ericsson.eniq.events.server.test.queryresults.PieChartCauseCodeAnalysisResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PieChartCauseCodeAnalysisWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase<PieChartCauseCodeAnalysisResult> {

    private final CauseCodeAnalysisPieChartResource causeCodeAnalysisPieChartResource = new CauseCodeAnalysisPieChartResource();

    private final PieChartCauseCodeDataValidator pieChartCauseCodeDataValidator = new PieChartCauseCodeDataValidator();

    private static final String TEST_VALUE_CAUSE_CODE_IDS = "" + causeCode_1_SGEH + "_" + causeProtTypeSgeh + "," + causeCode_1_LTE + "_" + causeProtTypeLte;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        attachDependencies(causeCodeAnalysisPieChartResource);
        final PieChartCauseCodePopulator pieChartCauseCodePopulator = new PieChartCauseCodePopulator(this.connection);
        pieChartCauseCodePopulator.createTemporaryTables();
        pieChartCauseCodePopulator.populateTemporaryTables();
    }

    @Test
    public void testGetCauseCodeAnalysisByAPN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    @Test
    public void testGetCauseCodeAnalysisByAPNWeek() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(DISPLAY_PARAM, CHART_PARAM);
        map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    @Test
    public void testGetCauseCodeAnalysisBySGSN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_SGSN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_SGSN_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    @Test
    public void testGetCauseCodeAnalysisByBSC() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_BSC);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_BSC_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    @Test
    public void testGetCauseCodeAnalysisByCell() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_CELL_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    @Test
    public void testGetCauseCodeAnalysisByCellWeek() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_CELL_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
        map.putSingle(DISPLAY_PARAM, CHART_PARAM);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsisWeek(result);
    }

    @Test
    public void testGetCauseCodeAnalysisByAPNAgg() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(CAUSE_CODE_IDS, TEST_VALUE_CAUSE_CODE_IDS);
        map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsCCAnaylsis(result);
    }

    private List<PieChartCauseCodeAnalysisResult> getResults(final String path) throws Exception {
        final String json = causeCodeAnalysisPieChartResource.getResults(path);
        System.out.println(json);
        return getTranslator().translateResult(json, PieChartCauseCodeAnalysisResult.class);
    }
}