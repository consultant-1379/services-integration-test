package com.ericsson.eniq.events.server.resources.automation.dataproviders;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.automation.util.CombinationUtils.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import com.ericsson.eniq.events.server.test.automation.util.CombinationGenerator;
import com.ericsson.eniq.events.server.test.automation.util.CombinationGeneratorImpl;

public class NetworkEventAnalysisTestDataProvider {

    public static Object[] provideTestDataForAPN() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_APN).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(NODE_PARAM, TEST_BLACKBERRY_NODE)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForApnGroup() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_APN).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(GROUP_NAME_PARAM, SAMPLE_APN_GROUP)//"DG_GroupNameAPN_1")
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }

    public static Object[] provideTestDataForBsc() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_BSC).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(NODE_PARAM, TEST_BSC1_NODE)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForBscGroup() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_BSC).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(GROUP_NAME_PARAM, TEST_VALUE_BSC_GROUP)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForSGSN() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_SGSN).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(NODE_PARAM, "MME1")
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForSGSNGroup() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_SGSN).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(GROUP_NAME_PARAM, TEST_VALUE_SGSN_GROUP)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES, ONE_DAY, ONE_WEEK)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForCell() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_CELL).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(NODE_PARAM, TEST_VALUE_CELL)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForCellGroup() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_CELL).add(KEY_PARAM, KEY_TYPE_SUM)
                .add(GROUP_NAME_PARAM, TEST_VALUE_CELL_GROUP)
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES)
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForTAC() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_TAC).add(KEY_PARAM, KEY_TYPE_ERR)
                .add(TAC_PARAM, "35347103")
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES)
                .add(EVENT_ID_PARAM, "2,3,28")
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
    
    public static Object[] provideTestDataForTACGroup() {
        final CombinationGenerator<String> combinationGenerator = new CombinationGeneratorImpl.Builder<String>()
                .add(DISPLAY_PARAM, GRID_PARAM).add(TYPE_PARAM, TYPE_TAC).add(KEY_PARAM, KEY_TYPE_ERR)
                .add(GROUP_NAME_PARAM, "myTacGrou")
                .add(TIME_QUERY_PARAM, FIVE_MINUTES, THIRTY_MINUTES)
                .add(EVENT_ID_PARAM, "1")
                .add(TZ_OFFSET, TEST_VALUE_TIMEZONE_OFFSET).add(MAX_ROWS, DEFAULT_MAX_ROWS, NO_MAX_ROWS).build();
        return convertToArrayOfMultivaluedMap(combinationGenerator.getAllCombinations());
    }
}
