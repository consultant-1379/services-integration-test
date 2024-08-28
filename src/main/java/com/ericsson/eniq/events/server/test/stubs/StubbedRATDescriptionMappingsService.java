/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.eniq.events.server.services.impl.RATDescriptionMappingsService;
import com.ericsson.eniq.events.server.utils.RATDescriptionMappingUtils;

/**
 * Instead of going to the database to fetch and cache the DIM_E_SGEH_RAT table, hard coding table 
 * contents here (had some problems with this initial query closing the database connection, and need
 * the one database connection to be left open for the entire test run for the tests with temporary tables)
 *  
 * @author eemecoy
 *
 */
public class StubbedRATDescriptionMappingsService extends RATDescriptionMappingsService {

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.services.impl.RATDescriptionMappingsService#populateMapFromRATTableInDB()
     */
    @Override
    public void populateMapFromRATTableInDB() {
    }

    public void ensureRatMapIsInstanstiated() {
        final Map<String, String> ratMappings = new HashMap<String, String>();
        ratMappings.put(RAT_INTEGER_VALUE_FOR_2G, GSM);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_3G, _3G);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_4G, _4G);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_LTE, _LTE);
        ratDescriptionMappingUtils = new RATDescriptionMappingUtils();
        ratDescriptionMappingUtils.setRatMappings(ratMappings);
    }
}
