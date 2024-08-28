package com.ericsson.eniq.events.server.integritytests.stubs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ericsson.eniq.events.server.utils.RMIEngineUtils;

/**
 * Dummy instance of the RMIEngineUtils bean, injected into resource classes when
 * running the data integrity tests to avoid having to create multiple temporary raw tables
 * 
 * Rather than querying the engine for the raw table names for a particular time range, this class just returns
 * the view name
 * 
 * (Cannot move class to services-test module due to its dependency on RMIEngineUtils)
 * 
 * @author eemecoy
 *
 */
public class RMIEngineUtilsForDataIntegrityTests extends RMIEngineUtils {

    RMIEngineUtilsForDataIntegrityTests() {
        populateKeyToViewMaps();

    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.RMIEngineUtils#getTableNames(java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
     */
    @Override
    public List<String> getTableNames(final Timestamp from, final Timestamp to, final String... viewNames) {

        return Arrays.asList(viewNames);
    }

    private List<String> createListWithOneEntry(final String viewName) {
        final ArrayList<String> listOfTablesContainingJustViewName = new ArrayList<String>();
        listOfTablesContainingJustViewName.add(viewName);
        return listOfTablesContainingJustViewName;
    }

    @Override
    public List<String> getTableNamesForMSS(final Timestamp from, final Timestamp to, final String key) {
        final String viewNameMSS = keyToMSSViewMapping.get(key);
        useDummyMethod(from, to);
        return createListWithOneEntry(viewNameMSS);
    }

    private void useDummyMethod(final Timestamp from, final Timestamp to) {
        from.toString();
        to.toString();

    }
}
