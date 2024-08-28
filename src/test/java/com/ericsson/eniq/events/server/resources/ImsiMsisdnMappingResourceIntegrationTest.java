/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author ehaoswa
 * @since 2011
 *
 */
public class ImsiMsisdnMappingResourceIntegrationTest extends DataServiceBaseTestCase {
    private ImsiMsisdnMappingResource imsiMsisdnMappingResource;

    private static final String TEST_IMSI = "123456789012345";

    private static final String TEST_MSISDN = "223456789012345";

    @Override
    public void onSetUp() throws Exception {
        imsiMsisdnMappingResource = new ImsiMsisdnMappingResource();
        attachDependencies(imsiMsisdnMappingResource);
        final MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        DummyUriInfoImpl.setUriInfo(map, imsiMsisdnMappingResource);
    }

    @Test
    public void testGetMappedMsisdn() {
        final String result = imsiMsisdnMappingResource.getMappingResults(TYPE_IMSI, TEST_IMSI);
        assertJSONSucceeds(result);
    }

    @Test
    public void testGetMappedImsi() {
        final String result = imsiMsisdnMappingResource.getMappingResults(TYPE_IMSI, TEST_MSISDN);
        assertJSONSucceeds(result);
    }

}
