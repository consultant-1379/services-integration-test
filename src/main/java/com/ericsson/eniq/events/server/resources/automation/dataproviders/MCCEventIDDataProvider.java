package com.ericsson.eniq.events.server.resources.automation.dataproviders;

import com.ericsson.eniq.events.server.test.automation.util.CombinationGenerator;
import com.ericsson.eniq.events.server.test.automation.util.CombinationGeneratorImpl;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.automation.util.CombinationUtils.convertToArrayOfMultivaluedMap;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.DEFAULT_MAX_ROWS;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.TEST_VALUE_TIMEZONE_OFFSET;

public class MCCEventIDDataProvider {

    public static Object[] provideTestData() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(TIME_QUERY_PARAM, "30").add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS)
                .add(MCC_PARAM,"310").add(EVENT_ID_PARAM,"458")
                .build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
}
