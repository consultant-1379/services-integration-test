/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.services.exclusivetacs;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.eniq.events.server.resources.BaseDataIntegrityTest;
import com.ericsson.eniq.events.server.test.queryresults.QueryResult;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author EEMECOY
 *
 */
public class ExclusiveTACHandlerDataIntegrityTest extends BaseDataIntegrityTest<QueryResult> {

    private ExclusiveTACHandler exclusiveTACHandler;

    @Before
    public void setup() throws SQLException {
        exclusiveTACHandler = new ExclusiveTACHandler();
        exclusiveTACHandler.setQueryUtils(queryUtils);
        exclusiveTACHandler.setDataService(dataServiceBean);
        exclusiveTACHandler.setTemplateMappingEngine(templateMappingEngine);
        exclusiveTACHandler.setTemplateUtils(templateUtils);
    }

    @Test
    public void testIsTacInExclusiveTacGroupIsTrueForExclusiveTAC() {
        assertThat(exclusiveTACHandler.isTacInExclusiveTacGroup(Integer.toString(SAMPLE_EXCLUSIVE_TAC)), is(true));
    }

    @Test
    public void testIsTacInExclusiveTacGroupIsFalseForNormalTAC() {
        assertThat(exclusiveTACHandler.isTacInExclusiveTacGroup(Integer.toString(SAMPLE_TAC)), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedIsTrueForExclusiveTACGroup() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(GROUP_NAME_PARAM, EXCLUSIVE_TAC_GROUP);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedIsFalseForExclusiveTACGroup() {
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(SAMPLE_TAC_GROUP, null), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedIsTrueForAnExclusiveTAC() {
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(null, Integer.toString(SAMPLE_EXCLUSIVE_TAC)),
                is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedIsFalseForANormalTAC() {
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(null, Integer.toString(SAMPLE_TAC)), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedIsFalseForANonTACQuery() {
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(null, null), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsTrueForAnExlusiveTacInTacParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TAC_PARAM, SAMPLE_EXCLUSIVE_TAC_TO_STRING);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsFalseForAStandardTacInTacParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TAC_PARAM, SAMPLE_TAC_TO_STRING);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsTrueForAnExlusiveTacInNodeParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TYPE_PARAM, TYPE_TAC);
        requestParameters.putSingle(NODE_PARAM, MARKETING_NAME_FOR_SAMPLE_EXCLUSIVE_TAC + COMMA + SAMPLE_EXCLUSIVE_TAC);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsFalseForAStandardTacInNodeParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TYPE_PARAM, TYPE_TAC);
        requestParameters.putSingle(NODE_PARAM, MARKETING_NAME_FOR_SAMPLE_TAC + COMMA + SAMPLE_TAC);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsTrueForTheExlusiveTacGroupInGroupNameParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(GROUP_NAME_PARAM, EXCLUSIVE_TAC_GROUP_NAME);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsFalseForAStandardTacGroupInGroupNameParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(GROUP_NAME_PARAM, SAMPLE_TAC_GROUP);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(false));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsTrueForTheExclusiveTacGroupInNodeParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TYPE_PARAM, TYPE_TAC);
        requestParameters.putSingle(GROUP_NAME_PARAM, EXCLUSIVE_TAC_GROUP);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(true));
    }

    @Test
    public void testQueryIsExclusiveTacRelatedWithRequestParametersIsFalseForAStandardTacGroupInNodeParam() {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        requestParameters.putSingle(TYPE_PARAM, TYPE_TAC);
        requestParameters.putSingle(GROUP_NAME_PARAM, SAMPLE_TAC_GROUP);
        assertThat(exclusiveTACHandler.queryIsExclusiveTacRelated(requestParameters), is(false));
    }
}
