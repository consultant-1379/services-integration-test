package com.ericsson.eniq.events.server.resources;

import java.net.URISyntaxException;
import javax.ws.rs.core.MultivaluedMap;

import com.ericsson.eniq.events.server.json.JSONArray;
import com.ericsson.eniq.events.server.json.JSONException;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.ericsson.eniq.events.server.test.util.JSONTestUtils;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Test;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.GROUP_NAME_PARAM;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TYPE_PARAM;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.SAMPLE_TAC_GROUP;

public class GroupMgtResourceIntegrationTest extends DataServiceBaseTestCase {
    private GroupMgtResource groupMgtResource;

    @Override
    public void onSetUp() throws Exception {
        groupMgtResource = new GroupMgtResource();
        attachDependencies(groupMgtResource);

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        DummyUriInfoImpl.setUriInfo(map, groupMgtResource);
    }

    @Test
    public void testGetApn() {
        final String result = groupMgtResource.getGroupDetails("APN");
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetImsi() {
        final String result = groupMgtResource.getGroupDetails("imsi");
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetMCCMNCGroup() throws URISyntaxException {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM,"MCC_MNC_WITH_NAMES");
        DummyUriInfoImpl.setUriInfo(map, groupMgtResource);
        final String result = groupMgtResource.getGroupDetails("MCC_MNC");
        System.out.println(result);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetMCCMNCGroup_groupName() throws URISyntaxException {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(TYPE_PARAM,"MCC_MNC_WITH_NAMES");
        map.putSingle(GROUP_NAME_PARAM,"g1");
        DummyUriInfoImpl.setUriInfo(map, groupMgtResource);
        final String result = groupMgtResource.getGroupDetails("MCC_MNC");
        System.out.println(result);
        assertJSONSucceeds(result);
    }


    @Test
    public void testGetTac() {
        final String result = groupMgtResource.getGroupDetails("TaC");
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetBSC() {
        final String result = groupMgtResource.getGroupDetails("RAT_VEND_HIER3");
        System.out.println(result);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetCELL() {
        final String result = groupMgtResource.getGroupDetails("RAT_VEND_HIER321");
        System.out.println(result);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetSGSN() {
        final String result = groupMgtResource.getGroupDetails("EVNTSRC");
        System.out.println(result);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetTRAC() {
        final String result = groupMgtResource.getGroupDetails("TRAC");
        System.out.println(result);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetUndefinedGroupReturnsAnEmptyDataArray() throws JSONException {
        final String result = groupMgtResource.getGroupDetails("not_existing");
        // should return an empty data array
        assertJSONSucceeds(result);
        JSONTestUtils.hasValidResultFormat(result);
        final JSONArray data = (JSONArray) JSONTestUtils.getField(result, "data");
        assertEquals(0, data.length());
    }

    @Test
    public void testGetSpecificTacGroup() throws Exception {
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(GROUP_NAME_PARAM, SAMPLE_TAC_GROUP);
        DummyUriInfoImpl.setUriInfo(map, groupMgtResource);

        final String result = groupMgtResource.getGroupDetails("TAC");
        assertJSONSucceeds(result);
    }

}
