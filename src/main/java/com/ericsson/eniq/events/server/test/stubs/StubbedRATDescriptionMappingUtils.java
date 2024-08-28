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

import com.ericsson.eniq.events.server.utils.RATDescriptionMappingUtils;

/**
 * @author eemecoy
 *
 */
public class StubbedRATDescriptionMappingUtils extends RATDescriptionMappingUtils {

    public StubbedRATDescriptionMappingUtils() {
        final Map<String, String> ratMappings = new HashMap<String, String>();
        ratMappings.put(RAT_INTEGER_VALUE_FOR_2G, GSM);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_3G, _3G);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_4G, _4G);
        ratMappings.put(RAT_INTEGER_VALUE_FOR_LTE, _LTE);
        setRatMappings(ratMappings);
    }

}
