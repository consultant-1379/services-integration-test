/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.integritytests.imsi_msisdn;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.resources.ImsiMsisdnMappingResource;
import com.ericsson.eniq.events.server.resources.TestsWithTemporaryTablesBaseTestCase;
import com.ericsson.eniq.events.server.test.queryresults.ImsiMsisdnResult;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author ehaoswa
 * @since 2011
 *
 */
public class ImsiMsisdnMappingWithPreparedTablesTest extends TestsWithTemporaryTablesBaseTestCase<ImsiMsisdnResult> {
    private ImsiMsisdnMappingResource imsiMsisdnMappingResource;

    private static final Map<String, String> imsiMsisdnColumns = new HashMap<String, String>();

    private static final Map<String, Object> imsiMsisdnTableValues = new HashMap<String, Object>();

    private static final long TEST_MSISDN = 412030410000004L;

    private static final long TEST_MSISDN_SECOND = 512030410000004L;

    private final static long TEST_IMSI_SGEH = 312030410000004L;

    private final static String EXPECTED_TOTAL_MAPPED_RESULTS = "412030410000004,512030410000004";

    static {
        imsiMsisdnColumns.put("IMSI", "unsigned bigint");
        imsiMsisdnColumns.put("MSISDN", "unsigned bigint");
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        imsiMsisdnMappingResource = new ImsiMsisdnMappingResource();

        createTemporaryTableWithColumnTypes(TEMP_DIM_E_IMSI_MSISDN, imsiMsisdnColumns);
        populateTemporaryTables();
        attachDependencies(imsiMsisdnMappingResource);
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        DummyUriInfoImpl.setUriInfo(map, imsiMsisdnMappingResource);
    }

    /**
     * @throws SQLException 
     * 
     */
    private void populateTemporaryTables() throws SQLException {

        imsiMsisdnTableValues.put("IMSI", TEST_IMSI_SGEH);
        imsiMsisdnTableValues.put("MSISDN", TEST_MSISDN);
        insertRow(TEMP_DIM_E_IMSI_MSISDN, imsiMsisdnTableValues);
        imsiMsisdnTableValues.put("IMSI", TEST_IMSI_SGEH);
        imsiMsisdnTableValues.put("MSISDN", TEST_MSISDN_SECOND);
        insertRow(TEMP_DIM_E_IMSI_MSISDN, imsiMsisdnTableValues);

    }

    @Test
    public void testGetMappedMsisdn() throws Exception {
        final String result = getData();
        validateResults(result);
    }

    private String getData() {
        return imsiMsisdnMappingResource.getMappingResults(TYPE_IMSI, Long.toString(TEST_IMSI_SGEH));
    }

    private void validateResults(final String jsonResult) throws Exception {

        jsonAssertUtils.assertJSONSucceeds(jsonResult);

        final List<ImsiMsisdnResult> imsiMsisdnResults = getTranslator().translateResult(jsonResult,
                ImsiMsisdnResult.class);
        assertThat(imsiMsisdnResults.size(), is(1)); //1  Always 1

        for (final ImsiMsisdnResult imsiMsisdnResult : imsiMsisdnResults) {
            validateSingleResult(imsiMsisdnResult);
        }

    }

    private void validateSingleResult(final ImsiMsisdnResult imsiMsisdnResult) {

        assertThat(imsiMsisdnResult.getMsisdn(), is(EXPECTED_TOTAL_MAPPED_RESULTS));
    }
}
