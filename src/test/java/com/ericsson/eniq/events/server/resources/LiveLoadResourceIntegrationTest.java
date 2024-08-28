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
package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.json.JSONException;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.ericsson.eniq.events.server.test.util.JSONTestUtils;
import com.ericsson.eniq.events.server.utils.HashUtilities;
import com.ericsson.eniq.events.server.utils.parameterchecking.ParameterChecker;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class LiveLoadResourceIntegrationTest extends DataServiceBaseTestCase {

    private LiveLoadResource liveLoadResource = null;

    private MultivaluedMap<String, String> map;

    private HashUtilities hashUtilities;

    @Override
    public void onSetUp() throws Exception {
        final ParameterChecker parameterChecker = new ParameterChecker();
        super.onSetUp();
        liveLoadResource = new MockLiveLoadResource();
        attachDependencies(liveLoadResource);
        liveLoadResource.setHashUtilities(hashUtilities);
        map = new MultivaluedMapImpl();
    }

    @Test
    public void testLiveLoadPagingSecondPage() throws Exception {
        final String transId = "transId76";
        map.putSingle("callback", transId);
        final int pagingLimit = 10;
        map.putSingle(PAGING_LIMIT_KEY, Integer.toString(pagingLimit));
        map.putSingle(PAGING_START_KEY, "10");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes("CELL");

        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadPagingFirstPage() throws Exception {
        final String transId = "transId76";
        map.putSingle("callback", transId);
        map.putSingle("offset", "0");
        final int pagingLimit = 10;
        map.putSingle(PAGING_LIMIT_KEY, Integer.toString(pagingLimit));
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes("CELL");
        verifyLiveLoadJson(json, transId, true);

    }

    @Test
    public void testGetLiveLoadHandset() throws Exception {
        final String transId = "transId76";
        map.putSingle("callback", transId);
        map.putSingle("id", "Motorola Inc.");
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadHandsetMakes();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testGetLiveLoadHandsetWitFilter() throws Exception {
        final String transId = "transId76";
        map.putSingle("callback", transId);
        map.putSingle("id", "Motorola Inc.");
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "abc");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadHandsetMakes();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testGetLiveLoadHandsetMakesMetaUI() throws Exception {

        final String servicesURL = "http://localhost:8080/EniqEventsServices/";
        final String handsetPath = "LIVELOAD/HANDSET_MAKES";
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource, servicesURL, handsetPath);
        final String json = liveLoadResource.getLiveLoadHandsetMakes();
        assertTrue(JSONTestUtils.isValidJson(json));
        assertEquals("true", JSONTestUtils.getField(json, "success"));
    }

    @Test
    public void testGetData() {
        try {
            liveLoadResource.getData("requestID", map);
            fail("UnsupportedOperationException should have been thrown");
        } catch (final UnsupportedOperationException e) {
            // expected this one
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLiveLoadNodeNoCallback() throws Exception {
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes("vv");
        verifyLiveLoadJson(json, null, false);
    }

    @Test
    public void testLiveLoadApnNoCallback() throws Exception {
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadApns();
        verifyLiveLoadJson(json, null, false);
    }

    @Test
    public void testLiveLoadUnknownType() throws Exception {
        checkLiveLoadNode("mesa", false);
    }

    @Test
    public void testLiveLoadCELL() throws Exception {
        final String result = checkLiveLoadNode("CELL", true);
        System.out.println(result);
    }

    @Test
    public void testLiveLoadCELLPartialCellProvidedThatDoesntExistLocally() throws Exception {
        final String result = checkLiveLoadNode("CELL", true, "LTEXX");
        System.out.println(result);
    }

    @Test
    public void testLiveLoadCELLPartialCellProvided() throws Exception {
        final String result = checkLiveLoadNode("CELL", true, "LTE");
        System.out.println(result);
    }

    private String checkLiveLoadNode(final String nodeType, final boolean success, final String filter)
            throws URISyntaxException, JSONException {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        if (filter != null) {
            map.putSingle(LIVELOAD_QUERY_PARAM, filter);
        }
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(nodeType);
        verifyLiveLoadJson(json, transId, success);
        return json;
    }

    @Test
    public void testLiveLoadController() throws Exception {
        final String nodeType = "BSC";
        final String result = checkLiveLoadNode(nodeType, true);
        System.out.println(result);
    }

    @Test
    public void testLiveLoadControllerPartialControllerProvidedThatDoesntExistLocally() throws Exception {
        final String nodeType = "BSC";
        final String result = checkLiveLoadNode(nodeType, true, "ERBSXX");
        System.out.println(result);
    }

    @Test
    public void testLiveLoadControllerPartialControllerProvided() throws Exception {
        final String nodeType = "BSC";
        final String result = checkLiveLoadNode(nodeType, true, "ERBS");
        System.out.println(result);
    }

    @Test
    public void testLiveLoadSGSN() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadSgsn();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadSGSNWitFilter() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "abc");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadSgsn();
        verifyLiveLoadJson(json, transId, true);
    }

    //    @Ignore
    //    //ignore for non MSS techpack testing
    //    public void testLiveLoadMSC() throws Exception {
    //        final String transId = "transId0";
    //        map.putSingle("callback", transId);
    //        map.putSingle(PAGING_START_KEY, "0");
    //        map.putSingle(PAGING_LIMIT_KEY, "20");
    //        map.putSingle(TZ_OFFSET, "+0000");
    //        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
    //        final String json = liveLoadResource.getLiveLoadMsc();
    //        verifyLiveLoadJson(json, transId, true);
    //    }
    //
    //    @Ignore
    //    //ignore for non MSS techpack testing
    //    public void testLiveLoadMSCWitFilter() throws Exception {
    //        final String transId = "transId0";
    //        map.putSingle("callback", transId);
    //        map.putSingle(PAGING_START_KEY, "0");
    //        map.putSingle(PAGING_LIMIT_KEY, "20");
    //        map.putSingle(LIVELOAD_QUERY_PARAM, "abc");
    //        map.putSingle(TZ_OFFSET, "+0000");
    //        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
    //        final String json = liveLoadResource.getLiveLoadMsc();
    //        verifyLiveLoadJson(json, transId, true);
    //    }

    @Test
    public void testLiveLoadTRAC() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadTrac();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadTRACWitFilter() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "1");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadTrac();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadSBR_CELL() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(NODE_PARAM, "smartone_R:RNC10:RNC10,Ericsson,3G");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadSbrCell();
        System.out.println(json);
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadKpi_RNC() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadRncs();
        System.out.println(json);
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadKpi_RNCWithFilter() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "rnc02%");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadRncs();
        System.out.println(json);
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadSBR_CELLWitFilter() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(LIVELOAD_QUERY_PARAM, "abc%");
        map.putSingle(NODE_PARAM, "bsc1,ERICSSON,3G");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadSbrCell();
        verifyLiveLoadJson(json, transId, true);
    }

    public void testLiveLoadFilter() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(LIVELOAD_QUERY_PARAM, "abc");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json_filter_1 = liveLoadResource.getLiveLoadApns();

        map.putSingle("query", "abc%");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json_filter_2 = liveLoadResource.getLiveLoadApns();
        assertEquals(json_filter_1, json_filter_2);

    }

    private String checkLiveLoadNode(final String nodeType, final boolean success) throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadNodes(nodeType);
        verifyLiveLoadJson(json, transId, success);
        return json;
    }

    @Test
    public void testLiveLoadApnLimit10() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(PAGING_START_KEY, "0");
        map.putSingle(PAGING_LIMIT_KEY, "20");
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadApns();
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadCountry() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadCountry();
        System.out.println(json);
        verifyLiveLoadJson(json, transId, true);
    }

    @Test
    public void testLiveLoadOperator() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(TZ_OFFSET, "+0000");
        map.putSingle(MCC_PARAM, "405");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadOperator();
        System.out.println(json);
        verifyLiveLoadJson(json, transId, true);

    }

    @Test
    public void testLiveLoadOperator_MCCParamNotFound() throws Exception {
        final String transId = "transId0";
        map.putSingle("callback", transId);
        map.putSingle(TZ_OFFSET, "+0000");
        DummyUriInfoImpl.setUriInfoLiveLoad(map, liveLoadResource);
        final String json = liveLoadResource.getLiveLoadOperator();
        System.out.println(json);
        assertEquals("false", JSONTestUtils.getField(json, "success").toString());
    }

    private void verifyLiveLoadJson(final String json, final String callbackId, final boolean success)
            throws JSONException {
        if (success) {
            checkForError(json);
            assertTrue("Callback ID not returned not the same as what was passed in ", json.contains(callbackId));
            assertTrue(json.contains("totalCount\":"));
        } else {
            assertEquals("false", JSONTestUtils.getField(json, "success").toString());
        }
    }

    private void checkForError(final String json) {
        if (JSONTestUtils.isValidJson(json)) {
            // the LiveSearch isnt valid json according to net.sf.json.JSONObject
            // but the error json is, so if isValidJson() == true --> error!
            fail(json);
        }
    }

    public void setHashUtilities(final HashUtilities hashUtilities) {
        this.hashUtilities = hashUtilities;
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
