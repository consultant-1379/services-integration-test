package com.ericsson.eniq.events.server.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ericsson.eniq.events.server.utils.RMIEngineUtils;

/**
 * Dummy instance of the RMIEngineUtils bean, injected into resource classes when
 * running the XXXIntegrationTests 
 * 
 * This class will query the ENIQ engine for the raw tables for a particular time range
 * If the ENIQ engine returns an empty list for this query - ie if there is no data for that particular time range loaded
 * on this server - this class returns a list containing the view name
 * 
 * These view names can still be injected successfully into the queries.
 * 
 * This is to ensure that tests run even when there is no data on the server (if there are no raw tables for a particular timerange
 * the services layer returns without executing the full query)
 * 
 * (Cannot move class to services-test module due to its dependency on RMIEngineUtils)
 * 
 * @author eemecoy
 *
 */
public class RMIEngineUtilsForIntegrationTests extends RMIEngineUtils {

    private final List<String> viewNamesThatAreAggregatedViews;

    RMIEngineUtilsForIntegrationTests() {
        populateKeyToViewMaps();
        viewNamesThatAreAggregatedViews = new ArrayList<String>();
        viewNamesThatAreAggregatedViews.add("EVENT_E_LTE_RAW");
        viewNamesThatAreAggregatedViews.add("EVENT_E_SGEH_RAW");
    }

    private String getJdbcURLFromPropertiesFile() {
        final InputStream jdbcPropertiesStream = ClassLoader.getSystemClassLoader().getResourceAsStream(
                "com/ericsson/eniq/events/server/test/jdbc.properties");
        final Properties propertiesInFile = new Properties();
        try {
            propertiesInFile.load(jdbcPropertiesStream);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return propertiesInFile.getProperty("engine.url");
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.RMIEngineUtils#getEngineHostName()
     */
    @Override
    protected String getEngineHostName() {
        return getJdbcURLFromPropertiesFile();
    }

    private List<String> createTwoTableNames(final String viewName) {
        String tablePrefix;
        if (viewNamesThatAreAggregatedViews.contains(viewName)) {
            tablePrefix = getTechPackNamefromViewName(viewName) + "_ERR_RAW";
        } else {
            tablePrefix = viewName;
        }
        final ArrayList<String> listOfTablesContainingTwoTableNames = new ArrayList<String>();
        listOfTablesContainingTwoTableNames.add(tablePrefix + "_01");
        listOfTablesContainingTwoTableNames.add(tablePrefix + "_02");
        return listOfTablesContainingTwoTableNames;
    }

    private String getTechPackNamefromViewName(final String viewName) {
        return viewName.substring(0, viewName.indexOf("_RAW"));

    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.RMIEngineUtils#getTableNames(java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
     */
    @Override
    public List<String> getTableNames(final Timestamp from, final Timestamp to, final String... viewNames) {
        final List<String> resultFromEngine = super.getTableNames(from, to, viewNames);
        if (resultFromEngine.isEmpty()) {
            return createTwoTableNames(viewNames[0]);
        }
        return resultFromEngine;
    }

    @Override
    public List<String> getTableNamesForMSS(final Timestamp from, final Timestamp to, final String key) {
        final String viewNameMSS = keyToMSSViewMapping.get(key);
        useDummyMethod(from, to);
        return createTwoTableNames(viewNameMSS);
    }

    private void useDummyMethod(final Timestamp from, final Timestamp to) {
        from.toString();
        to.toString();
    }

}
