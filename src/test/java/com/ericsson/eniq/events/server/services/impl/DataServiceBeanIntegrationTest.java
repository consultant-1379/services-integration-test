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
package com.ericsson.eniq.events.server.services.impl;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ericsson.eniq.events.server.json.JSONArray;
import com.ericsson.eniq.events.server.query.QueryParameter;
import com.ericsson.eniq.events.server.query.resultsettransformers.ResultSetTransformerFactory;
import com.ericsson.eniq.events.server.resources.DataServiceBaseTestCase;
import com.ericsson.eniq.events.server.test.QueryCommand;
import com.ericsson.eniq.events.server.test.common.ApplicationTestConstants;
import com.ericsson.eniq.events.server.test.util.JSONTestUtils;

public class DataServiceBeanIntegrationTest extends DataServiceBaseTestCase {

    @Test
    public void testInit() {
        assertNotNull(queryUtils);
        assertNotNull(dataServiceBean);
    }

    /**
     * Sample query test - illustrates all the required pieces for
     * simple execution of services query.
     *
     * @throws Exception
     */
    @Test
    public void test_sample_query() throws Exception {

        // Step 1. template parameters
        final Map<String, String> templateParameters = new HashMap<String, String>();
        templateParameters.put("columns", "EVENT_SOURCE_NAME,IMSI");
        templateParameters.put("table", "EVENT_E_SGEH_ERR_RAW");

        // Step 2. create named query parameters
        final Map<String, QueryParameter> map = new HashMap<String, QueryParameter>();
        map.put(DATE_FROM, QueryParameter.createStringParameter("2010-05-26 15:38:30"));
        map.put(DATE_TO, QueryParameter.createStringParameter("2010-05-26 15:39:30"));

        // Step 3. create and execute command object
        // requires:
        // 1. template name (must be in classpath)
        // 2. template parameters
        // 3. query parameters if any
        //
        final QueryCommand command = (QueryCommand) this.applicationContext.getBean("queryCommand");
        command.setTemplateName("q_test.vm");
        command.setTemplateParameters(templateParameters);
        command.setQueryParameters(map);

        final String json = command.execute();


        // Step 4. validate results
        assertNotNull(json);
        assertTrue(json.length() > 0);

        // Step 5. transform to JSON and validate
        assertTrue(JSONTestUtils.isValidJson(json));
        assertTrue(JSONTestUtils.hasValidResultFormat(json));
        assertEquals("true", JSONTestUtils.getField(json, "success"));
        assertEquals("", JSONTestUtils.getField(json, "errorDescription"));

        final JSONArray data = (JSONArray) JSONTestUtils.getField(json, "data");
        assertNotNull(data);

        // data rows can be extracted as JSON objects
        for (int i = 0; i < data.length(); i++) {
            data.getJSONObject(i);
        }
    }

    @Test
    public void testGetDataAsCSV() throws Exception {
        final String query = "select top 10 MCC, MNC, LAC, CELL_ID, IMSI from EVENT_E_SGEH_ERR_RAW";
        final String csv = dataServiceBean.getData("requestID", query, null,
                ResultSetTransformerFactory.getCSVTransformer(null, null));
        assertNotNull(csv);
        assertTrue(csv.contains("\"MCC\",\"MNC\",\"LAC\",\"CELL_ID\",\"IMSI\""));
    }

    @Test
    public void testGetCSVDataWrappedWithQuotations() throws Exception {
        final String query = "select top 1 TAC,MANUFACTURER,BAND,CHAR_LENGTH(BAND) as str_len from DIM_E_SGEH_TAC order by str_len DESC";
        String csv = dataServiceBean.getData("CANCEL_REQUEST_NOT_SUPPORTED", query, null,
                ResultSetTransformerFactory.getCSVTransformer(null, null));
        assertNotNull(csv);

        csv = csv.split("\n")[0];
        int startIndex = csv.indexOf("\"");
        int endIndex = csv.indexOf("\"", startIndex + 1);
        int numberOfFieldsWrappedWithQuotations = 0;

        while (startIndex >= 0 && endIndex >= 0) {
            numberOfFieldsWrappedWithQuotations++;
            csv = csv.replace(csv.substring(startIndex, endIndex + 1), "String with no commas");
            startIndex = csv.indexOf("\"");
            endIndex = csv.indexOf("\"", startIndex + 1);
        }

        assertTrue("Number of fields wrapped with quotations does not match expected value",
                numberOfFieldsWrappedWithQuotations == 4);
        assertFalse("Result contains excess quotations", csv.contains("\""));
    }

    @Test
    public void test_sample_query_weeklyOverride() throws Exception {

        final Map<String, String> templateParameters = new HashMap<String, String>();
        templateParameters.put("columns", "EVENT_SOURCE_NAME,IMSI");
        templateParameters.put("table", "EVENT_E_SGEH_ERR_RAW");

        final Map<String, QueryParameter> map = new HashMap<String, QueryParameter>();
        map.put(DATE_FROM, QueryParameter.createStringParameter("2014-09-07 00:00:00"));
        map.put(DATE_TO, QueryParameter.createStringParameter("2014-09-16 00:00:00"));
        map.put(ApplicationTestConstants.IS_WEEK_OVERRIDE_APPLIED, QueryParameter.createStringParameter("true"));

        final QueryCommand command = (QueryCommand) this.applicationContext.getBean("queryCommand");
        command.setTemplateName("q_test.vm");
        command.setTemplateParameters(templateParameters);
        command.setQueryParameters(map);

        final String json = command.execute();

        assertNotNull(json);
        assertTrue(json.length() > 0);

        assertTrue(JSONTestUtils.isValidJson(json));
        assertTrue(JSONTestUtils.hasValidResultFormat(json));
        assertEquals("true", JSONTestUtils.getField(json, "success"));
        assertEquals("", JSONTestUtils.getField(json, "errorDescription"));

        final JSONArray data = (JSONArray) JSONTestUtils.getField(json, "data");
        assertEquals("1410048000000", JSONTestUtils.getField(json, "dataTimeFrom"));
        assertEquals("1410825600000", JSONTestUtils.getField(json, "dataTimeTo"));
        assertNotNull(data);

        for (int i = 0; i < data.length(); i++) {
            data.getJSONObject(i);
        }
    }
}
