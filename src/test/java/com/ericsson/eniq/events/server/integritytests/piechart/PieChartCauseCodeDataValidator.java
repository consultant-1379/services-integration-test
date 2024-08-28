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
package com.ericsson.eniq.events.server.integritytests.piechart;

import static com.ericsson.eniq.events.server.integritytests.piechart.PieChartCauseCodePopulator.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import com.ericsson.eniq.events.server.test.queryresults.*;

/**
 * @since 2011
 * 
 */
public class PieChartCauseCodeDataValidator {

    private final static Map<Integer, String> causeCodeMappingSGEH = new HashMap<Integer, String>();

    private final static Map<Integer, String> causeCodeMappingLTE = new HashMap<Integer, String>();

    private final static Map<Integer, String> subCauseCodeMappingSGEH = new HashMap<Integer, String>();

    private final static Map<Integer, String> subCauseCodeMappingLTE = new HashMap<Integer, String>();

    private final static Map<Integer, String> cptMappingSGEH = new HashMap<Integer, String>();

    private final static Map<Integer, String> cptMappingLTE = new HashMap<Integer, String>();

    public static final String ONE_WEEK = "10080";

    static {
        // Populate SGEH cause codes/descriptions
        causeCodeMappingSGEH.put(causeCode_1_SGEH, causeCode_1_desc_SGEH);
        causeCodeMappingSGEH.put(causeCode_2_SGEH, causeCode_2_desc_SGEH);
        causeCodeMappingSGEH.put(causeCode_3_SGEH, causeCode_2_desc_SGEH);

        // Populate LTE cause codes/descriptions
        causeCodeMappingLTE.put(causeCode_1_LTE, causeCode_1_desc_LTE);
        causeCodeMappingLTE.put(causeCode_2_LTE, causeCode_2_desc_LTE);
        causeCodeMappingLTE.put(causeCode_3_LTE, causeCode_2_desc_LTE);

        // Populate SGEH sub cause codes/descriptions
        subCauseCodeMappingSGEH.put(subCauseCode_1_SGEH, subCauseCode_1_desc_SGEH);
        subCauseCodeMappingSGEH.put(subCauseCode_2_SGEH, subCauseCode_2_desc_SGEH);

        // Populate LTE sub cause codes/descriptions
        subCauseCodeMappingLTE.put(subCauseCode_1_LTE, subCauseCode_1_desc_LTE);
        subCauseCodeMappingLTE.put(subCauseCode_2_LTE, subCauseCode_2_desc_LTE);

        // Populate SGEH sub cause prototypes /descriptions
        cptMappingSGEH.put(causeProtTypeSgeh, causeProtType_desc_SGEH);

        // Populate LTE sub cause prototypes /descriptions
        cptMappingLTE.put(causeProtTypeLte, causeProtType_desc_LTE);
    }

    public void makeAssertionsCCList(final List<PieChartCauseCodeListResult> summaryResult, String time) {
        assertThat(summaryResult.size(), is(4));


        for (PieChartCauseCodeListResult result : summaryResult) {
            if (result.getCauseCodeID().equals(Integer.toString(causeCode_1_SGEH))) {
                assertThat(result.getCauseCodeID(), is(Integer.toString(causeCode_1_SGEH)));
                assertThat(result.getCauseCode(), is(causeCodeMappingSGEH.get(causeCode_1_SGEH)));
            } else if (result.getCauseCodeID().equals(Integer.toString(causeCode_2_SGEH))) {
                assertThat(result.getCauseCodeID(), is(Integer.toString(causeCode_2_SGEH)));
                assertThat(result.getCauseCode(), is(causeCodeMappingSGEH.get(causeCode_2_SGEH)));
            } else if (result.getCauseCodeID().equals(Integer.toString(causeCode_1_LTE))) {
                assertThat(result.getCauseCodeID(), is(Integer.toString(causeCode_1_LTE)));
                assertThat(result.getCauseCode(), is(causeCodeMappingLTE.get(causeCode_1_LTE)));
            } else if (result.getCauseCodeID().equals(Integer.toString(causeCode_2_LTE))) {
                assertThat(result.getCauseCodeID(), is(Integer.toString(causeCode_2_LTE)));
                assertThat(result.getCauseCode(), is(causeCodeMappingLTE.get(causeCode_2_LTE)));
            } else {
                fail("Incorrect cause code");
            }
        }

    }

    public void makeAssertionsCCAnaylsis(final List<PieChartCauseCodeAnalysisResult> summaryResult) {
        assertThat(summaryResult.size(), is(2));

        final PieChartCauseCodeAnalysisResult firstResult = summaryResult.get(0);
        assertThat(firstResult.getCauseCodeID(), is(Integer.toString(causeCode_1_SGEH)));
        assertThat(firstResult.getCauseCode(), is(causeCodeMappingSGEH.get(causeCode_1_SGEH) + " (" + cptMappingSGEH.get(causeProtTypeSgeh) + ")"));
        assertThat(firstResult.getCauseProtType(), is(Integer.toString(causeProtTypeSgeh)));
        assertThat(firstResult.getNumberOfErrors(), is(2));
        assertThat(firstResult.getNumberOfOccurences(), is(2));

        final PieChartCauseCodeAnalysisResult secondResult = summaryResult.get(1);
        assertThat(secondResult.getCauseCodeID(), is(Integer.toString(causeCode_1_LTE)));
        assertThat(secondResult.getCauseCode(), is(causeCodeMappingLTE.get(causeCode_1_LTE) + " (" + cptMappingLTE.get(causeProtTypeLte) + ")"));
        assertThat(secondResult.getCauseProtType(), is(Integer.toString(causeProtTypeLte)));
        assertThat(secondResult.getNumberOfErrors(), is(2));
        assertThat(secondResult.getNumberOfOccurences(), is(1));
    }

    public void makeAssertionsCCAnaylsisWeek(final List<PieChartCauseCodeAnalysisResult> summaryResult) {
        assertThat(summaryResult.size(), is(2));

        final PieChartCauseCodeAnalysisResult firstResult = summaryResult.get(0);
        assertThat(firstResult.getCauseCodeID(), is(Integer.toString(causeCode_1_SGEH)));
        assertThat(firstResult.getCauseCode(), is(causeCodeMappingSGEH.get(causeCode_1_SGEH) + " (" + cptMappingSGEH.get(causeProtTypeSgeh) + ")"));
        assertThat(firstResult.getCauseProtType(), is(Integer.toString(causeProtTypeSgeh)));
        assertThat(firstResult.getNumberOfErrors(), is(4));
        assertThat(firstResult.getNumberOfOccurences(), is(2));

        final PieChartCauseCodeAnalysisResult secondResult = summaryResult.get(1);
        assertThat(secondResult.getCauseCodeID(), is(Integer.toString(causeCode_1_LTE)));
        assertThat(secondResult.getCauseCode(), is(causeCodeMappingLTE.get(causeCode_1_LTE) + " (" + cptMappingLTE.get(causeProtTypeLte) + ")"));
        assertThat(secondResult.getCauseProtType(), is(Integer.toString(causeProtTypeLte)));
        assertThat(secondResult.getNumberOfErrors(), is(3));
        assertThat(secondResult.getNumberOfOccurences(), is(1));
    }

    public void makeAssertionsSCCAnaylsis(final List<PieChartSubCauseCodeAnalysisResult> summaryResult) {
        assertThat(summaryResult.size(), is(1));

        final PieChartSubCauseCodeAnalysisResult firstResult = summaryResult.get(0);
        assertThat(firstResult.getCauseCodeID(), is(causeCode_1_SGEH));
        assertThat(firstResult.getSubCauseCodeID(), is(subCauseCode_1_SGEH));
        assertThat(firstResult.getSubCauseCode(), is(subCauseCodeMappingSGEH.get(subCauseCode_1_SGEH) + " (" + cptMappingSGEH.get(causeProtTypeSgeh)
                + ")"));
        assertThat(firstResult.getNumberOfErrors(), is(2));
        assertThat(firstResult.getNumberOfOccurences(), is(2));
    }
}