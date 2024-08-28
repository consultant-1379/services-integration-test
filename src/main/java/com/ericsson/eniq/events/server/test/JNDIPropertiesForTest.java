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
package com.ericsson.eniq.events.server.test;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager.*;

import java.util.*;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import com.ericsson.eniq.events.server.common.ApplicationConfigConstants;
import com.ericsson.eniq.events.server.datasource.DataSourceManager;

/**
 * Perform set up for DB Integration tests This wires in the default eniq (dwh) data source into JNDI
 * 
 * eemecoy
 * 
 * @since January 2014
 * 
 */
@Ignore
public class JNDIPropertiesForTest {

    @Resource
    private DataSource dwhDataSource;

    @Resource(name = ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES)
    private Properties eniqEventsProperties;

    private final static Map<Object, Object> defaultMap = new HashMap<Object, Object>();

    static {
        defaultMap.put(DataSourceManager.DEFAULT_ENIQ_DATA_SOURCE_PROPERTY_NAME, "jdbc/eniqPool");
        defaultMap.put(DataSourceManager.EXPORT_CSV_ENIQ_DATA_SOURCE_PROPERTY_NAME, "jdbc/eniqPool");
        defaultMap.put(ENIQ_EVENTS_MAX_JSON_RESULT_SIZE, "50");
        defaultMap.put(ENIQ_EVENTS_DT_USE_TIME_DELAY, "false");
        defaultMap.put(ENIQ_EVENTS_DATA_TIERING, "false");
    }

    /**
     * called from DataServiceBaseTestCase-context.xml
     * 
     * @throws Exception
     */
    public void setUpJNDIPropertiesForTest() throws Exception {
        final SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final String defaultENIQDataSourceName = (String) eniqEventsProperties.get(DataSourceManager.DEFAULT_ENIQ_DATA_SOURCE_PROPERTY_NAME);
        builder.bind(defaultENIQDataSourceName, dwhDataSource);
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        builder.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }

    public void setDwhDataSource(final DataSource dwhDataSource) {
        this.dwhDataSource = dwhDataSource;
    }

    public void setEniqEventsProperties(final Properties eniqEventsProperties) {
        this.eniqEventsProperties = eniqEventsProperties;
    }

    public void setUpDataTieringJNDIProperty() throws Exception {
        final SimpleNamingContextBuilder context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        eniqEventProperties.put(ENIQ_EVENTS_DATA_TIERING, TRUE);
        eniqEventProperties.put(ENIQ_EVENTS_TIME_DELAY_TO_BE_USED, "0");
        context.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }

    public void useSucRawJNDIProperty() throws Exception {
        final SimpleNamingContextBuilder context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        eniqEventProperties.put(ENIQ_EVENTS_SUC_RAW, TRUE);
        context.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }

    public void disableSucRawJNDIProperty() throws Exception {
        final SimpleNamingContextBuilder context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final String defaultENIQDataSourceName = (String) eniqEventsProperties.get(DataSourceManager.DEFAULT_ENIQ_DATA_SOURCE_PROPERTY_NAME);
        context.bind(defaultENIQDataSourceName, dwhDataSource);
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        eniqEventProperties.put(ENIQ_EVENTS_SUC_RAW, "false");
        context.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }

    public void setupDataTieringAndSucRawJNDIProperties() throws Exception {
        final SimpleNamingContextBuilder context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        eniqEventProperties.put(ENIQ_EVENTS_DATA_TIERING, TRUE);
        eniqEventProperties.put(ENIQ_EVENTS_TIME_DELAY_TO_BE_USED, "0");
        eniqEventProperties.put(ENIQ_EVENTS_SUC_RAW, TRUE);
        context.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }

    public void setupDataTieringAndDisableSucRawJNDIProperties() throws Exception {
        final SimpleNamingContextBuilder context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final Properties eniqEventProperties = new Properties();
        eniqEventProperties.putAll(defaultMap);
        eniqEventProperties.put(ENIQ_EVENTS_DATA_TIERING, TRUE);
        eniqEventProperties.put(ENIQ_EVENTS_TIME_DELAY_TO_BE_USED, "0");
        eniqEventProperties.put(ENIQ_EVENTS_SUC_RAW, "false");
        context.bind(ApplicationConfigConstants.ENIQ_EVENT_PROPERTIES, eniqEventProperties);
    }
}
