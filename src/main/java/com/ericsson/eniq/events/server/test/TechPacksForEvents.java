/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test;

import java.util.HashMap;
import java.util.Map;

import static com.ericsson.eniq.events.server.common.EventIDConstants.ACTIVATE_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.ATTACH_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.ATTACH_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.DEACTIVATE_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.DEDICATED_BEARER_ACTIVATE_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.DEDICATED_BEARER_DEACTIVATE_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.DETACH_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.DETACH_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.HANDOVER_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.ISRAU_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.PDN_CONNECT_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.PDN_DISCONNECT_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.RAU_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.SERVICE_REQUEST_IN_2G_AND_3G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.SERVICE_REQUEST_IN_4G;
import static com.ericsson.eniq.events.server.common.EventIDConstants.TAU_IN_4G;
import static com.ericsson.eniq.events.server.common.TechPackData.EVENT_E_LTE;
import static com.ericsson.eniq.events.server.common.TechPackData.EVENT_E_SGEH;

/**
 * Didn't put this class in the services-test module as that doesn't have a dependency on the services-common module,
 * which has the event id and tech pack constatns
 * @author eemecoy
 *
 */
public class TechPacksForEvents {

    private  TechPacksForEvents (){}

    private static Map<Integer, String> eventIdToTechPackMapping = new HashMap<Integer, String>();

    static {
        eventIdToTechPackMapping.put(ATTACH_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(ACTIVATE_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(DEACTIVATE_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(RAU_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(ISRAU_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(DETACH_IN_2G_AND_3G, EVENT_E_SGEH);
        eventIdToTechPackMapping.put(SERVICE_REQUEST_IN_2G_AND_3G, EVENT_E_SGEH);

        eventIdToTechPackMapping.put(ATTACH_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(DETACH_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(HANDOVER_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(TAU_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(DEDICATED_BEARER_ACTIVATE_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(DEDICATED_BEARER_DEACTIVATE_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(PDN_CONNECT_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(PDN_DISCONNECT_IN_4G, EVENT_E_LTE);
        eventIdToTechPackMapping.put(SERVICE_REQUEST_IN_4G, EVENT_E_LTE);

    }

    public static String getTechPack(final int eventId) {
        if (eventIdToTechPackMapping.containsKey(eventId)) {
            return eventIdToTechPackMapping.get(eventId);
        }
        throw new RuntimeException("No tech pack defined for event id " + eventId);
    }
}
