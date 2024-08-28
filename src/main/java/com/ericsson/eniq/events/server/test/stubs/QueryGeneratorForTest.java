/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import java.io.IOException;

import com.ericsson.eniq.events.server.query.QueryGenerator;
import com.ericsson.eniq.events.server.query.QueryGeneratorParameters;
import com.ericsson.eniq.events.server.test.common.TestProperties;

/**
 * A QueryGenerator for use in the integration tests - it will prepend the options for query plan generation if 
 * configured in jdbc.properties
 * 
 * To configure query plan generation set the following properties in jdbc.propertes
 * query_plan=true
 * query_plan_directory=/eniq/home/dcuser/another/query_plans (Note that the query_plan_directory folder must exist)
 * query_plan_name=my_query
 * 
 * @author eemecoy
 *
 */
public class QueryGeneratorForTest extends QueryGenerator {

    private static final String QUERY_PLAN = "query_plan";

    private static final String NEW_LINE = "\n";

    private static final String QUERY_PLAN_DIRECTORY = "query_plan_directory";

    private static final String DEFAULT_QUERY_PLAN_DIRECTORY = "/eniq/home/dcuser/";

    private static final String QUERY_PLAN_NAME = "query_plan_name";

    private static final String DEFAULT_QUERY_PLAN_NAME = "query";

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.query.QueryGenerator#getQuery(com.ericsson.eniq.events.server.query.QueryGeneratorParameters)
     */
    @Override
    public String getQuery(final QueryGeneratorParameters queryGeneratorParameters) {
        final String query = super.getQuery(queryGeneratorParameters);
        if (isQueryPlanConfigured()) {
            return prependQueryPlanParameters(query);
        }
        return query;
    }

    private String prependQueryPlanParameters(final String query) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getQueryPlanSettings());
        stringBuilder.append(query);
        return stringBuilder.toString();
    }

    private String getQueryPlanSettings() {
        final String queryPlanDirectory = getQueryPlanDirectory();
        System.out.println("Will write query plans to " + queryPlanDirectory);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("set temporary option query_plan_as_html = 'on';");
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("set temporary option query_plan_as_html_directory = '" + queryPlanDirectory + "';");
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("set temporary option query_detail = 'on';");
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("set temporary option query_timing = 'on';");
        stringBuilder.append(NEW_LINE);
        final String queryPlanName = getQueryPlanName();
        stringBuilder.append("set temporary option query_name = '" + queryPlanName + "';");
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("set temporary option query_plan_after_run = 'on';");
        stringBuilder.append(NEW_LINE);
        return stringBuilder.toString();
    }

    private String getQueryPlanName() {
        try {
            return new TestProperties().getProperty(QUERY_PLAN_NAME, DEFAULT_QUERY_PLAN_NAME);
        } catch (final IOException e) {
            e.printStackTrace();
            return DEFAULT_QUERY_PLAN_DIRECTORY;
        }
    }

    private String getQueryPlanDirectory() {
        try {
            return new TestProperties().getProperty(QUERY_PLAN_DIRECTORY, DEFAULT_QUERY_PLAN_DIRECTORY);
        } catch (final IOException e) {
            e.printStackTrace();
            return DEFAULT_QUERY_PLAN_DIRECTORY;
        }

    }

    private boolean isQueryPlanConfigured() {
        try {
            final String queryPlanProperty = new TestProperties().getProperty(QUERY_PLAN);
            return Boolean.valueOf(queryPlanProperty);
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
