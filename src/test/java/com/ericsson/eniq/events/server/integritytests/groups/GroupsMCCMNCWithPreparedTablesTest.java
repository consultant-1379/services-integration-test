package com.ericsson.eniq.events.server.integritytests.groups;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.resources.GroupMgtResource;
import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.test.queryresults.GroupMCCMNCResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Test;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.COUNTRY;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.GROUP_NAME_PARAM;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.OPERATOR;
import static com.ericsson.eniq.events.server.common.ApplicationConstants.TAC;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.DIM_E_SGEH_MCCMNC;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.GROUP_NAME;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.MCC;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.MCCMNC_GROUP;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.MNC;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.SAMPLE_TAC;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.SONY_ERICSSON_TAC;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.TEMP_DIM_E_SGEH_MCCMNC;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.TEMP_GROUP_TYPE_E_MCC_MNC;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.TEMP_GROUP_TYPE_E_TAC;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GroupsMCCMNCWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase<GroupMCCMNCResult> {

    GroupMgtResource groupMgtResource = new GroupMgtResource();

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        ReplaceTablesWithTempTablesTemplateUtils.useTemporaryTableFor(DIM_E_SGEH_MCCMNC);
        attachDependencies(groupMgtResource);
        populateTACGroupTable();
        populateMCCMNCDIMTable();
    }


    @Test
    public void testGetSpecificGroup_MCCMNC_withGroupName() throws Exception {

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(GROUP_NAME_PARAM, "g2");
        map.putSingle("type","MCC_MNC_WITH_NAMES");
        DummyUriInfoImpl.setUriInfo(map,groupMgtResource);
        populdateMCCMNCGroupTable();
        final String json = groupMgtResource.getGroupDetails(MCCMNC_GROUP);
        System.out.println(json);
        final List<GroupMCCMNCResult> summaryResult = getTranslator().translateResult(json, GroupMCCMNCResult.class);
        assertThat(summaryResult.get(0).getGroupName(),is("g2"));
        assertThat(summaryResult.size(),is(1));
        assertThat(summaryResult.get(0).getGroupMembers().get(0).get(0),is("Afghanistan"));
         assertThat(summaryResult.get(0).getGroupMembers().get(0).get(1),is("412"));
         assertThat(summaryResult.get(0).getGroupMembers().get(0).get(2),is("AWCC"));
         assertThat(summaryResult.get(0).getGroupMembers().get(0).get(3),is("01"));
    }

    @Test
    public void testGetSpecificGroup_MCCMNC_AllGroup() throws Exception {

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle("type","MCC_MNC_WITH_NAMES");
        DummyUriInfoImpl.setUriInfo(map,groupMgtResource);
        populdateMCCMNCGroupTable();
        final String json = groupMgtResource.getGroupDetails(MCCMNC_GROUP);
        final List<GroupMCCMNCResult> summaryResult = getTranslator().translateResult(json, GroupMCCMNCResult.class);
        System.out.println(json);
        assertThat(summaryResult.size(),is(2));
        assertThat(summaryResult.get(0).getGroupName(),is("g1"));
       assertThat(summaryResult.get(0).getGroupMembers().get(0).get(0),is("USA"));
        assertThat(summaryResult.get(0).getGroupMembers().get(0).get(1),is("310"));
        assertThat(summaryResult.get(0).getGroupMembers().get(0).get(2),is("AT&T Mobility"));
        assertThat(summaryResult.get(0).getGroupMembers().get(0).get(3),is("150"));

        assertThat(summaryResult.get(0).getGroupMembers().get(1).get(0),is("USA"));
         assertThat(summaryResult.get(0).getGroupMembers().get(1).get(1),is("310"));
         assertThat(summaryResult.get(0).getGroupMembers().get(1).get(2),is("AT&T Mobility"));
         assertThat(summaryResult.get(0).getGroupMembers().get(1).get(3),is("410"));


        assertThat(summaryResult.get(1).getGroupName(),is("g2"));

        assertThat(summaryResult.get(1).getGroupMembers().get(0).get(0),is("Afghanistan"));
         assertThat(summaryResult.get(1).getGroupMembers().get(0).get(1),is("412"));
         assertThat(summaryResult.get(1).getGroupMembers().get(0).get(2),is("AWCC"));
         assertThat(summaryResult.get(1).getGroupMembers().get(0).get(3),is("01"));


    }



    private  void  populateMCCMNCDIMTable () throws SQLException {
        insertRowIntoMCCMNCDIMTable("Afghanistan","AWCC","412","01");
        insertRowIntoMCCMNCDIMTable("Afghanistan","Roshan","412","20");
        insertRowIntoMCCMNCDIMTable("USA","AT&T Mobility","310","410");
        insertRowIntoMCCMNCDIMTable("USA","AT&T Mobility","310","150");
        insertRowIntoMCCMNCDIMTable("USA","AT&T Mobility","310","380");

    }

    private void insertRowIntoMCCMNCDIMTable(final String country,final String operator,final String mcc,final String mnc) throws SQLException {
        final Map<String,Object> values = new HashMap<String, Object>();
        values.put(COUNTRY,country);
        values.put(OPERATOR,operator);
        values.put(MCC,mcc);
        values.put(MNC,mnc);
        insertRow(TEMP_DIM_E_SGEH_MCCMNC,values);
    }

    private void populateTACGroupTable() throws SQLException {
        insertRowIntoTACGroupTable("a", SONY_ERICSSON_TAC);
        insertRowIntoTACGroupTable("a",SAMPLE_TAC);
        insertRowIntoTACGroupTable("b",SONY_ERICSSON_TAC);
        //insertRowIntoTACGroupTable(SONY_ERICSSON_TAC_GROUP, SAMPLE_TAC);
    }


    private void populdateMCCMNCGroupTable ()throws SQLException
    {
        insertRowIntoMCCMNCGroupTable("g1","310","410");
        insertRowIntoMCCMNCGroupTable("g1","310","150");
        insertRowIntoMCCMNCGroupTable("g2","412","01");
        insertRowIntoMCCMNCGroupTable("g2","412","20");
    }

    private void insertRowIntoMCCMNCGroupTable(final String groupName,final String mcc,final String mnc) throws SQLException {
        final Map<String,Object> values = new HashMap<String, Object>();
        values.put(GROUP_NAME,groupName);
        values.put(MCC,mcc);
        values.put(MNC,mnc);
        insertRow(TEMP_GROUP_TYPE_E_MCC_MNC,values);

    }


    private void insertRowIntoTACGroupTable(final String groupName, final int TACvalue) throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(GROUP_NAME, groupName);
        values.put(TAC, TACvalue);
        insertRow(TEMP_GROUP_TYPE_E_TAC, values);
    }


}
