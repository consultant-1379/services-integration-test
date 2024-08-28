package com.ericsson.eniq.events.server.integritytests.groups;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.resources.GroupMgtResource;
import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.test.queryresults.GroupResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class GroupsWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase<GroupResult> {

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
    public void testGetGroups() throws Exception {

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();

        final String json = getData(map);
        System.out.println(json);
        validateResultTACGroupsDataSet(json);
    }

    @Test
    public void testGetSpecificGroup_EXCLUSIVE_TAC_noData() throws Exception {

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(GROUP_NAME_PARAM, EXCLUSIVE_TAC_GROUP);

        final String json = getData(map);
        validateResultTACGroupsDataSetIsEmpty(json);
    }

    @Test
    public void testGetSpecificGroup_EXCLUSIVE_TAC_withData() throws Exception {

        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.putSingle(GROUP_NAME_PARAM, EXCLUSIVE_TAC_GROUP);

        populateTACGroupTableWithExclusiveTACGroup();

        final String json = getData(map);
        validateResultExclusiveTACGroupDataSet(json);
    }

    private String getData(final MultivaluedMap<String, String> map) throws URISyntaxException {
        DummyUriInfoImpl.setUriInfo(map, groupMgtResource);

        final String json = groupMgtResource.getGroupDetails(TAC);
        return json;
    }

    private void populateMCCMNCDIMTable() throws SQLException {
        insertRowIntoMCCMNCDIMTable("Afghanistan", "AWCC", "412", "01");
        insertRowIntoMCCMNCDIMTable("Afghanistan", "Roshan", "412", "20");
        insertRowIntoMCCMNCDIMTable("USA", "AT&T Mobility", "310", "410");
        insertRowIntoMCCMNCDIMTable("USA", "AT&T Mobility", "310", "150");
        insertRowIntoMCCMNCDIMTable("USA", "AT&T Mobility", "310", "380");

    }

    private void insertRowIntoMCCMNCDIMTable(final String country, final String operator, final String mcc,
            final String mnc) throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(COUNTRY, country);
        values.put(OPERATOR, operator);
        values.put(MCC, mcc);
        values.put(MNC, mnc);
        insertRow(TEMP_DIM_E_SGEH_MCCMNC, values);
    }

    private void populateTACGroupTable() throws SQLException {
        insertRowIntoTACGroupTable(SONY_ERICSSON_TAC_GROUP, SONY_ERICSSON_TAC);
        insertRowIntoTACGroupTable(SONY_ERICSSON_TAC_GROUP, SAMPLE_TAC);
        insertRowIntoTACGroupTable(SAMPLE_TAC_GROUP, SAMPLE_TAC);
    }

    private void populateTACGroupTableWithExclusiveTACGroup() throws SQLException {
        insertRowIntoTACGroupTable(EXCLUSIVE_TAC_GROUP, SONY_ERICSSON_TAC);
    }

    private void insertRowIntoTACGroupTable(final String groupName, final int TACvalue) throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(GROUP_NAME, groupName);
        values.put(TAC, TACvalue);
        insertRow(TEMP_GROUP_TYPE_E_TAC, values);
    }

    private void validateResultTACGroupsDataSet(final String json) throws Exception {
        final List<GroupResult> summaryResult = getTranslator().translateResult(json, GroupResult.class);
        assertThat(summaryResult.size(), is(3)); // there are 2 groups

        final GroupResult firstGroup = summaryResult.get(1);
        assertThat(firstGroup.getGroupName(), is(SAMPLE_TAC_GROUP));
        final List<String> firstGroupMembers = firstGroup.getGroupMembers();
        assertThat(firstGroupMembers.size(), is(1)); // there is 1 TAC in this group
        assertThat(firstGroupMembers.get(0), is(Integer.toString(SAMPLE_TAC)));

        final GroupResult secondGroup = summaryResult.get(2);
        assertThat(secondGroup.getGroupName(), is(SONY_ERICSSON_TAC_GROUP));
        final List<String> secondGroupMembers = secondGroup.getGroupMembers();
        assertThat(secondGroupMembers.size(), is(2)); // there are 2 TACs in this group
        assertThat(secondGroupMembers.get(0), is(Integer.toString(SONY_ERICSSON_TAC)));
        assertThat(secondGroupMembers.get(1), is(Integer.toString(SAMPLE_TAC)));

    }

    private void validateResultTACGroupsDataSetIsEmpty(final String json) throws Exception {
        final List<GroupResult> summaryResult = getTranslator().translateResult(json, GroupResult.class);
        assertThat(summaryResult.size(), is(1)); // there are no groups
    }

    private void validateResultExclusiveTACGroupDataSet(final String json) throws Exception {
        final List<GroupResult> summaryResult = getTranslator().translateResult(json, GroupResult.class);
        assertThat(summaryResult.size(), is(1)); // there is 1 group

        final GroupResult firstGroup = summaryResult.get(0);
        assertThat(firstGroup.getGroupName(), is(EXCLUSIVE_TAC_GROUP));
        final List<String> firstGroupMembers = firstGroup.getGroupMembers();
        assertThat(firstGroupMembers.size(), is(3)); // there is 1 TAC in this group
        assertThat(firstGroupMembers.get(0), is(Integer.toString(SONY_ERICSSON_TAC)));
    }

}
