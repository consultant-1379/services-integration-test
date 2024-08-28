/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.integritytests.piechart;

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.resources.piechart.CauseCodeAnalysisPieChartResource;
import com.ericsson.eniq.events.server.test.queryresults.PieChartSubCauseCodeAnalysisResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Test;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

public class PieChartSubCauseCodeAnalysisWithPreparedTablesTest extends
        TestsWithTemporaryTablesBaseTestCase<PieChartSubCauseCodeAnalysisResult> {

    private final CauseCodeAnalysisPieChartResource causeCodeAnalysisPieChartResource = new CauseCodeAnalysisPieChartResource();

    private final PieChartCauseCodeDataValidator pieChartCauseCodeDataValidator = new PieChartCauseCodeDataValidator();

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase#onSetUp()
     */
    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        attachDependencies(causeCodeAnalysisPieChartResource);
        final PieChartCauseCodePopulator pieChartCauseCodePopulator = new PieChartCauseCodePopulator(this.connection);
        pieChartCauseCodePopulator.createTemporaryTables();
        pieChartCauseCodePopulator.populateTemporaryTables();
    }

    @Test
    public void testGetSubCauseCodeAnalysisByAPN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(CAUSE_CODE_PARAM, "" + causeCode_1_SGEH);
        map.putSingle(CAUSE_PROT_TYPE_PARAM, Integer.toString(causeProtTypeSgeh));
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartSubCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR
                + SUB_CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsSCCAnaylsis(result);
    }

    @Test
    public void testGetSubCauseCodeAnalysisBySGSN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_SGSN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_SGSN_1);
        map.putSingle(CAUSE_CODE_PARAM, "" + causeCode_1_SGEH);
        map.putSingle(CAUSE_PROT_TYPE_PARAM, Integer.toString(causeProtTypeSgeh));
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartSubCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR
                + SUB_CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsSCCAnaylsis(result);
    }

    @Test
    public void testGetSubCauseCodeAnalysisByBSC() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_BSC);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_BSC_1);
        map.putSingle(CAUSE_CODE_PARAM, "" + causeCode_1_SGEH);
        map.putSingle(CAUSE_PROT_TYPE_PARAM, Integer.toString(causeProtTypeSgeh));
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartSubCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR
                + SUB_CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsSCCAnaylsis(result);
    }

    @Test
    public void testGetSubCauseCodeAnalysisByCell() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_CELL_1);
        map.putSingle(CAUSE_CODE_PARAM, "" + causeCode_1_SGEH);
        map.putSingle(CAUSE_PROT_TYPE_PARAM, Integer.toString(causeProtTypeSgeh));
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartSubCauseCodeAnalysisResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR
                + SUB_CAUSE_CODE_ANALYSIS);
        pieChartCauseCodeDataValidator.makeAssertionsSCCAnaylsis(result);
    }

    private List<PieChartSubCauseCodeAnalysisResult> getResults(final String path) throws Exception {
        final String json = causeCodeAnalysisPieChartResource.getResults(path);
        System.out.println(json);
        return getTranslator().translateResult(json, PieChartSubCauseCodeAnalysisResult.class);
    }
}