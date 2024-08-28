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
package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.query.TimeRangeSelector;
import com.ericsson.eniq.events.server.services.exclusivetacs.ExclusiveTACHandler;
import com.ericsson.eniq.events.server.services.impl.TechPackCXCMappingService;
import com.ericsson.eniq.events.server.test.queryresults.QueryResult;
import com.ericsson.eniq.events.server.test.queryresults.ResultTranslator;
import com.ericsson.eniq.events.server.test.schema.ColumnTypes;
import com.ericsson.eniq.events.server.test.sql.SQLCommand;
import com.ericsson.eniq.events.server.test.stubs.DummyHttpHeaders;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;
import com.ericsson.eniq.events.server.utils.RMIEngineUtils;
import com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager;
import com.ericsson.eniq.events.server.utils.techpacks.RawTableFetcher;

/**
 * Base test case class for all data service tests that use temporary tables created and populated before test, and that are torn down after the test
 * 
 * Have left this class in this package so that it can access the protected methods in the Resource classes
 * 
 * 
 */

public class TestsWithTemporaryTablesBaseTestCase<T extends QueryResult> extends BaseDataIntegrityTest {

    private UriInfo testUri = null; // NOPMD eemecoy

    protected ResultTranslator<T> resultTranslator;

    @Autowired
    private RawTableFetcher rawTableFetcher;

    @Autowired
    protected RMIEngineUtils rmiEngineUtils;

    @Autowired
    protected TimeRangeSelector timeRangeSelector;

    protected static Map<String, String> groupColumns = new HashMap<String, String>();

    protected static Map<String, String> groupColumnsMCCMNC = new HashMap<String, String>();

    @Autowired
    protected TechPackCXCMappingService techPackCXCMappingService;

    protected JSONAssertUtils jsonAssertUtils = new JSONAssertUtils();

    protected static Map<String, String> groupColumnsDIMMCCMNC = new HashMap<String, String>();

    static {
        groupColumns.put("GROUP_NAME", "varchar(64)");
        groupColumns.put(TAC, ColumnTypes.getColumnType(TAC));

        groupColumnsMCCMNC.put("GROUP_NAME", "varchar(64)");
        groupColumnsMCCMNC.put(MCC_PARAM, ColumnTypes.getColumnType(MCC_PARAM));
        groupColumnsMCCMNC.put(MNC_PARAM, ColumnTypes.getColumnType(MNC_PARAM));

        groupColumnsDIMMCCMNC.put(OPERATOR, ColumnTypes.getColumnType(OPERATOR));
        groupColumnsDIMMCCMNC.put(COUNTRY, ColumnTypes.getColumnType(COUNTRY));
        groupColumnsDIMMCCMNC.put(MCC_PARAM, ColumnTypes.getColumnType(MCC_PARAM));
        groupColumnsDIMMCCMNC.put(MNC_PARAM, ColumnTypes.getColumnType(MNC_PARAM));
    }

    public TestsWithTemporaryTablesBaseTestCase() {
        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
        try {
            testUri = new DummyUriInfoImpl(null, "baseURI", "somePath");
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ResultTranslator<T> getTranslator() {
        if (resultTranslator == null) {
            resultTranslator = new ResultTranslator<T>();
        }
        return resultTranslator;
    }

    protected void attachDependencies(final BaseResource baseResource) {
        baseResource.setTemplateUtils(templateUtils);
        baseResource.setDataService(dataServiceBean);
        baseResource.setQueryUtils(queryUtils);
        baseResource.setApplicationConfigManager(applicationConfigManager);
        baseResource.setHttpHeaders(new DummyHttpHeaders());
        baseResource.templateMappingEngine = templateMappingEngine;
        baseResource.performanceTrace = servicePerformanceTraceLogger;
        baseResource.setAuditService(auditService);
        baseResource.setMediaTypeHandler(mediaTypeHandler);
        loadBalancingPolicyService.setEniqEventsProperties(new Properties());
        baseResource.setLoadBalancingPolicyService(loadBalancingPolicyService);
        baseResource.setTechPackListFactory(techPackListFactory);
        baseResource.setRawTableFetcher(rawTableFetcher);
        baseResource.setExclusiveTACHandler(exclusiveTACHandler);
        baseResource.setTimeRangeSelector(timeRangeSelector);
        baseResource.setDateTimeHelper(dateTimeHelper);
        baseResource.setDataTieringHandler(dataTieringHandler);
        baseResource.uriInfo = this.testUri;
    }

    protected String runGetDataQuery(final BaseResource baseResource, final MultivaluedMap<String, String> requestParameters)
            throws URISyntaxException {
        baseResource.setUriInfo(new DummyUriInfoImpl(requestParameters));
        final String json = baseResource.getData();
        System.out.println(json);
        return json;
    }

    @Before
    public void onSetUp() throws Exception {
        createTemporaryTableWithColumnTypes(TEMP_GROUP_TYPE_E_MCC_MNC, groupColumnsMCCMNC);
        createTemporaryTableWithColumnTypes(TEMP_DIM_E_SGEH_MCCMNC, groupColumnsDIMMCCMNC);
    }

    public void createTemporaryTableOnSpecificConnection(final Connection conn, final String table, final Collection<String> columns)
            throws Exception {
        final Map<String, String> columnsWithTypes = getColumnTypes(columns);
        createTemporaryTableOnSpecificConnection(conn, table, columnsWithTypes);
    }

    private Map<String, String> getColumnTypes(final Collection<String> columns) {
        final Map<String, String> columnTypes = new HashMap<String, String>();
        for (final String column : columns) {
            columnTypes.put(column, ColumnTypes.getColumnType(column));
        }
        return columnTypes;
    }

    protected void createTemporaryTableOnSpecificConnection(final Connection conn, final String tableName, final Map<String, String> columns)
            throws SQLException {
        new SQLCommand(conn).createTemporaryTable(tableName, columns);
    }

    public void setRawTableFetcher(final RawTableFetcher rawTableFetcher) {
        this.rawTableFetcher = rawTableFetcher;
    }

    /**
     * @param rmiEngineUtils
     *        the rmiEngineUtils to set
     */
    public void setRmiEngineUtils(final RMIEngineUtils rmiEngineUtils) {
        this.rmiEngineUtils = rmiEngineUtils;
    }

    /**
     * @param exclusiveTACHandler
     *        the exclusiveTACHandler to set
     */
    public void setExclusiveTACHandler(final ExclusiveTACHandler exclusiveTACHandler) {
        this.exclusiveTACHandler = exclusiveTACHandler;
    }

    public void setTimeRangeSelector(final TimeRangeSelector timeRangeSelector) {
        this.timeRangeSelector = timeRangeSelector;
    }
}