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

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.resources.piechart.CauseCodeAnalysisPieChartResource;
import com.ericsson.eniq.events.server.test.queryresults.PieChartCauseCodeListResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Test;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.CAUSE_CODE_PIE_CHART;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.CC_LIST;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.NODE_PARAM;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.PATH_SEPARATOR;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TIME_QUERY_PARAM;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_APN;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_BSC;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_CELL;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_PARAM;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_SGSN;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TZ_OFFSET;
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.TEST_APN_1;
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.TEST_BSC_1;
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.TEST_CELL_1;
import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.TEST_SGSN_1;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.THIRTY_MINUTES;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.ONE_WEEK;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR;

public class PieChartCauseCodeListWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase<PieChartCauseCodeListResult> {

    private final CauseCodeAnalysisPieChartResource causeCodeAnalysisPieChartResource = new CauseCodeAnalysisPieChartResource();

    private final PieChartCauseCodeDataValidator pieChartCauseCodeDataValidator = new PieChartCauseCodeDataValidator();

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        attachDependencies(causeCodeAnalysisPieChartResource);
        final PieChartCauseCodePopulator pieChartCauseCodePopulator = new PieChartCauseCodePopulator(this.connection);
        pieChartCauseCodePopulator.createTemporaryTables();
        pieChartCauseCodePopulator.populateTemporaryTables();
    }

    @Test
    public void testGetCauseCodeListByAPN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,THIRTY_MINUTES);
    }

    @Test
    public void testGetCauseCodeListByAPNWeek() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_APN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_APN_1);
        map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,ONE_WEEK);
    }

    @Test
    public void testGetCauseCodeListBySGSN() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_SGSN);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_SGSN_1);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,THIRTY_MINUTES);
    }

    @Test
    public void testGetCauseCodeListByBSC() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_BSC);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_BSC_1);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,THIRTY_MINUTES);
    }

    @Test
    public void testGetCauseCodeListByCell() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_CELL_1);
        map.putSingle(TIME_QUERY_PARAM, THIRTY_MINUTES);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,THIRTY_MINUTES);
    }

    @Test
    public void testGetCauseCodeListByCellWeek() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM, TYPE_CELL);
        map.putSingle(TZ_OFFSET, TIME_ZONE_OFFSET_OF_PLUS_ONE_HOUR);
        map.putSingle(NODE_PARAM, TEST_CELL_1);
        map.putSingle(TIME_QUERY_PARAM, ONE_WEEK);
        DummyUriInfoImpl.setUriInfo(map, causeCodeAnalysisPieChartResource);

        final List<PieChartCauseCodeListResult> result = getResults(CAUSE_CODE_PIE_CHART + PATH_SEPARATOR + CC_LIST);
        pieChartCauseCodeDataValidator.makeAssertionsCCList(result,ONE_WEEK);
    }

    private List<PieChartCauseCodeListResult> getResults(final String path) throws Exception {
        final String json = causeCodeAnalysisPieChartResource.getResults(path);
        System.out.println(json);
        return getTranslator().translateResult(json, PieChartCauseCodeListResult.class);
    }
}