/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.utils.techpacks.timerangequeries.impl;

import static com.ericsson.eniq.events.server.common.TechPackData.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ericsson.eniq.events.server.resources.DataServiceBaseTestCase;
import com.ericsson.eniq.events.server.utils.DateTimeRange;
import com.ericsson.eniq.events.server.utils.FormattedDateTimeRange;

public class EventsTechPackTimerangeQuerierIntegrationTest extends DataServiceBaseTestCase {

    private final static List<String> volumeBasedViews = new ArrayList<String>();

    private final static List<String> timeBasedViews = new ArrayList<String>();

    private EventsTechPackTimerangeQuerier timerangeQuerier;

    static {
        volumeBasedViews.add(EVENT_E_LTE_ERR_RAW);
        volumeBasedViews.add(EVENT_E_LTE_SUC_RAW);
        volumeBasedViews.add(EVENT_E_SGEH_ERR_RAW);
        volumeBasedViews.add(EVENT_E_SGEH_SUC_RAW);
        volumeBasedViews.add(EVENT_E_RAN_CFA_RAW);
        volumeBasedViews.add(EVENT_E_DVTP_DT_RAW);
        timeBasedViews.add(DC_Z_ALARM_INFO_RAW);
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        timerangeQuerier = new EventsTechPackTimerangeQuerier();
        timerangeQuerier.setTemplateMappingEngine(templateMappingEngine);
        timerangeQuerier.setTemplateUtils(templateUtils);
        timerangeQuerier.setQueryUtils(queryUtils);
        timerangeQuerier.setDataServiceQueryExecutor(dataServiceQueryExecutor);
        timerangeQuerier.setLoadBalancingPolicyFactory(loadBalancingPolicyFactory);
        timerangeQuerier.setTechPackTechnologies(techPackTechnologies);
    }

    @Test
    public void testQueryVolumeBasedViewsForRawTableNames() {

        final FormattedDateTimeRange dateTimeRange = DateTimeRange.getDailyAggregationTimeRangebyLocalTime(ONE_WEEK, 0, 0, 0);
        for (final String view : volumeBasedViews) {
            timerangeQuerier.getRAWTablesUsingQuery(dateTimeRange, view);
        }
    }

    @Test
    public void testQueryTimeRangeViewsForRawTableNamesAndLatestTime() {
        for (final String view : volumeBasedViews) {
            timerangeQuerier.getLatestTablesUsingQuery(view);
        }
    }

    @Test
    public void testQueryTimeBasedViewsForRawTableNames() {

        final FormattedDateTimeRange dateTimeRange = DateTimeRange.getDailyAggregationTimeRangebyLocalTime(ONE_WEEK, 0, 0, 0);
        for (final String view : timeBasedViews) {
            timerangeQuerier.getRAWTablesUsingQuery(dateTimeRange, view);
        }
    }

    @Test
    public void testQueryTimeBasedViewsForRawTableNamesAndLatestTime() {

        for (final String view : timeBasedViews) {
            timerangeQuerier.getLatestTablesUsingQuery(view);
        }
    }

}
