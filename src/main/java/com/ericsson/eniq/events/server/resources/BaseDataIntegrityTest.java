package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.common.utils.SqlUtils.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static com.ericsson.eniq.events.server.test.temptables.TempTableNames.*;
import static org.junit.Assert.*;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ericsson.eniq.events.server.datasource.DBConnectionManager;
import com.ericsson.eniq.events.server.datasource.loadbalancing.LoadBalancingPolicyFactory;
import com.ericsson.eniq.events.server.integritytests.stubs.InterceptedDBConnectionManager;
import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.logging.performance.ServicePerformanceTraceLogger;
import com.ericsson.eniq.events.server.query.DataServiceQueryExecutor;
import com.ericsson.eniq.events.server.query.QueryGenerator;
import com.ericsson.eniq.events.server.serviceprovider.Service;
import com.ericsson.eniq.events.server.serviceprovider.impl.GenericService;
import com.ericsson.eniq.events.server.serviceprovider.impl.GenericSimpleService;
import com.ericsson.eniq.events.server.services.StreamingDataService;
import com.ericsson.eniq.events.server.services.datatiering.DataTieringHandler;
import com.ericsson.eniq.events.server.services.exclusivetacs.ExclusiveTACHandler;
import com.ericsson.eniq.events.server.services.impl.DataServiceBean;
import com.ericsson.eniq.events.server.templates.mappingengine.TemplateMappingEngine;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;
import com.ericsson.eniq.events.server.test.JNDIPropertiesForTest;
import com.ericsson.eniq.events.server.test.QueryCommand;
import com.ericsson.eniq.events.server.test.database.DatabaseConnectionHelper;
import com.ericsson.eniq.events.server.test.populator.LookupTechPackPopulator;
import com.ericsson.eniq.events.server.test.queryresults.QueryResult;
import com.ericsson.eniq.events.server.test.queryresults.ResultTranslator;
import com.ericsson.eniq.events.server.test.queryresults.validator.QueryResultValidator;
import com.ericsson.eniq.events.server.test.schema.*;
import com.ericsson.eniq.events.server.test.sql.SQLCommand;
import com.ericsson.eniq.events.server.test.sql.SQLExecutor;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;
import com.ericsson.eniq.events.server.utils.*;
import com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager;
import com.ericsson.eniq.events.server.utils.datetime.DateTimeHelper;
import com.ericsson.eniq.events.server.utils.parameterchecking.ParameterChecker;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackLicensingService;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackListFactory;

/**
 * Base test case class for all data service tests that use temporary tables created and populated before test, and that are torn down after the test
 *
 * Have left this class in this package so that it can access the protected methods in the Resource classes
 *
 *
 */
//Use Spring JUnit 4 for integration testing
@RunWith(SpringJUnit4ClassRunner.class)
//Context file location
@ContextConfiguration(locations = { "classpath:com/ericsson/eniq/events/server/resources/BaseDataIntegrityTest-context.xml" })
public abstract class BaseDataIntegrityTest<T extends QueryResult> implements ITableController {

    @Autowired
    protected DataServiceBean dataServiceBean;

    protected InterceptedDBConnectionManager interceptedDbConnectionManager;

    @Autowired
    protected ApplicationConfigManager applicationConfigManager;

    @Autowired
    protected StreamingDataService streamingDataService;

    @Autowired
    protected QueryUtils queryUtils;

    @Qualifier("templateUtils")
    @Autowired
    protected TemplateUtils templateUtils;

    @Autowired
    protected QueryCommand queryCommand;

    @Autowired
    protected ServicePerformanceTraceLogger servicePerformanceTraceLogger;

    @Autowired
    protected LoadBalancingPolicyFactory loadBalancingPolicyFactory;

    protected Connection connection;

    ResultTranslator<T> resultTranslator;

    @Autowired
    protected DBConnectionManager dbConnectionManager;

    @Autowired
    protected AuditService auditService;

    @Autowired
    protected LoadBalancingPolicyService loadBalancingPolicyService;

    @Autowired
    protected TechPackListFactory techPackListFactory;

    @Autowired
    private ParameterChecker parameterChecker;

    @Autowired
    protected DateTimeHelper dateTimeHelper;

    @Autowired
    private QueryGenerator queryGenerator;

    @Autowired
    protected MediaTypeHandler mediaTypeHandler;

    @Autowired
    protected TemplateMappingEngine templateMappingEngine;

    @Autowired
    private DataServiceQueryExecutor queryExecutor;

    @Autowired
    protected ExclusiveTACHandler exclusiveTACHandler;

    @Autowired
    protected JSONAssertUtils jsonAssertUtils;

    @Autowired
    private TechPackLicensingService techPackLicensingService;

    @Autowired
    protected DataTieringHandler dataTieringHandler;

    @Autowired
    protected JNDIPropertiesForTest jndiProperties;

    private static List<String> groupColumns = new ArrayList<String>();

    static {
        groupColumns.add("GROUP_NAME");
        groupColumns.add(TAC);
    }

    @Before
    public void setUpForTest() throws Exception {
        connection = getDWHDataSourceConnection();
        setUpInterceptedDbConnectionManager();
        createTemporaryTable(TEMP_GROUP_TYPE_E_TAC, groupColumns);
        populateGroupTableWithExclusiveTacs();
        initializeTracingAndTracingProperties();
    }

    @After
    public void onTearDown() throws Exception {
        ReplaceTablesWithTempTablesTemplateUtils.removeExtraKeysForTempTables();
    }

    private void initializeTracingAndTracingProperties() {
        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
    }

    protected void provideDefaultValuesForColumns(final Map<String, Object> values, final List<String> columnsInTable) {
        for (final String column : columnsInTable) {
            if (!values.containsKey(column)) {
                values.put(column, ColumnTypes.getDefaultValueForColumnType(column));
            }
        }

    }

    /**
     * Timestamp data received should be presented to user in local time This transforms the timestamp used in the database into the local time which
     * the services layer should return
     *
     * @param timestampInDB
     *        timestamp used in the DATETIME_ID field in the database eg 2012-01-17 11:05:13
     * @param timeZoneOffset
     *        time zone offset parameter from URL
     *
     * @return timestamp converted to local time
     */
    protected String convertToLocalTime(final String timestampInDB, final String timeZoneOffset) {
        final String timestampWithMillisecondsAppended = timestampInDB + ".0";
        final String localTIme = DateTimeUtils.getLocalTime(timestampWithMillisecondsAppended, timeZoneOffset, RECEIVED_DATE_FORMAT);
        return localTIme;
    }

    /**
     * Run the getData() method on the service class, and return response
     *
     * @param service
     *        service to execute getData() on
     * @param map
     *        request parameters for request
     *
     * @return JSON response from query
     */
    protected String getData(final GenericService service, final MultivaluedMap<String, String> map) {
        addURITypeParameters(map);
        return service.getData(map);
    }

    protected ResultTranslator<T> getTranslator() {
        if (resultTranslator == null) {
            resultTranslator = new ResultTranslator<T>();
        }
        return resultTranslator;
    }

    protected void attachDependencies(final GenericService baseService) {
        baseService.setAuditService(auditService);
        baseService.setPerformanceTrace(servicePerformanceTraceLogger);
        baseService.setParameterChecker(parameterChecker);
        baseService.setDateTimeHelper(dateTimeHelper);
        baseService.setLoadBalancingPolicyService(loadBalancingPolicyService);
        baseService.setDataService(dataServiceBean);
        baseService.setQueryGenerator(queryGenerator);
        baseService.setQueryUtils(queryUtils);
        baseService.setTechPackListFactory(techPackListFactory);
        baseService.setMediaTypeHandler(mediaTypeHandler);
        baseService.setApplicationConfigManager(applicationConfigManager);
        baseService.setExclusiveTACHandler(exclusiveTACHandler);
        baseService.setTechPackLicensingService(techPackLicensingService);
        baseService.setDataTieringHandler(dataTieringHandler);
    }

    protected void attachDependenciesForSimpleService(final GenericSimpleService baseService) {
        baseService.setAuditService(auditService);
        baseService.setLoadBalancingPolicyService(loadBalancingPolicyService);
        baseService.setDataService(dataServiceBean);
        baseService.setMediaTypeHandler(mediaTypeHandler);
        baseService.setTemplateMappingEngine(templateMappingEngine);
        baseService.setTemplateUtils(templateUtils);
    }

    /**
     * Create and populate the temporary static DIM look up table required See javadoc on LookupTechPackPopulator for more information
     *
     * @param lookupTableRequired
     *        name of temporary table
     */
    public void createAndPopulateLookupTable(final String lookupTableRequired) throws Exception {
        new LookupTechPackPopulator().createAndPopulateLookupTable(connection, lookupTableRequired);
    }

    /**
     * Create and populate the temporary static DIM look up table required the name of the look up table does not begin with # mark
     *
     * @param lookupTableRequired
     *        name of temporary table
     */
    protected void createAndPopulateTempLookupTable(final String lookupTableRequired) throws Exception {
        createAndPopulateLookupTable(COMMENT_PREFIX + lookupTableRequired);
    }

    private void populateGroupTableWithExclusiveTacs() throws SQLException {
        populateTacGroupTable(EXCLUSIVE_TAC_GROUP, SAMPLE_EXCLUSIVE_TAC);
        populateTacGroupTable(EXCLUSIVE_TAC_GROUP, SAMPLE_EXCLUSIVE_TAC_2);
    }

    protected void populateTacGroupTable(final String tacGroup, final int tac) throws SQLException {
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put(GROUP_NAME, tacGroup);
        values.put(TAC, tac);
        insertRow(TEMP_GROUP_TYPE_E_TAC, values);
    }

    protected long calculateHashedId(final String rat, final String controller, final String vendor) {
        return queryUtils.createHashIDForController(rat, controller, vendor);
    }

    protected long calculateHashedIdForCell(final String rat, final String controller, final String cell, final String vendor) {
        return queryUtils.createHashIDFor3GCell(rat, controller, cell, vendor);
    }

    public void setDataServiceBean(final DataServiceBean dataServiceBean) {
        this.dataServiceBean = dataServiceBean;
    }

    public void setApplicationConfigManager(final ApplicationConfigManager applicationConfigManager) {
        this.applicationConfigManager = applicationConfigManager;
    }

    public void setStreamingDataService(final StreamingDataService streamingDataService) {
        this.streamingDataService = streamingDataService;
    }

    public void setStreamingServiceBean(final StreamingDataService dataServiceBean) {
        this.streamingDataService = dataServiceBean;
    }

    public void setQueryUtils(final QueryUtils queryUtils) {
        this.queryUtils = queryUtils;
    }

    public void setQueryCommand(final QueryCommand command) {
        this.queryCommand = command;
    }

    public void setTemplateUtils(final TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }

    public void setQueryGenerator(final QueryGenerator queryGenerator) {
        this.queryGenerator = queryGenerator;

    }

    public void setServicePerformanceTraceLogger(final ServicePerformanceTraceLogger servicePerformanceTraceLogger) {
        this.servicePerformanceTraceLogger = servicePerformanceTraceLogger;
    }

    public void setDbConnectionManager(final DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    public void setAuditService(final AuditService auditService) {
        this.auditService = auditService;
    }

    public void setLoadBalancingPolicyFactory(final LoadBalancingPolicyFactory loadBalancingPolicyFactory) {
        this.loadBalancingPolicyFactory = loadBalancingPolicyFactory;
    }

    protected boolean isSucTable(final String tempTableName) {
        return tempTableName.contains(KEY_TYPE_SUC);
    }

    /**
     * Not able to use the connection from the dwhDataSource bean defined in the application context.xml file- tried this, but as each query in the
     * services layer closes the connection when it finishes the query, and the dwhDataSource seems to be defined/created only once in each JVM - was
     * getting connection closed errors when tried to run a second test with this same query
     *
     * Instead, creating the connection the old fashioned way, and recreating it for every test
     *
     * @return
     * @throws Exception
     */
    protected Connection getDWHDataSourceConnection() throws Exception {
        return DatabaseConnectionHelper.getDBConnection();
    }

    protected void setUpInterceptedDbConnectionManager() {
        //haven't set this in the context.xml file as would need connection, and we only obtain that here
        interceptedDbConnectionManager = new InterceptedDBConnectionManager(connection);
        interceptedDbConnectionManager.setDwhrepDataSource(dbConnectionManager.getDwhrepDataSource());
        queryExecutor.setDbConnectionManager(interceptedDbConnectionManager);
    }

    protected void closeSQLExector(final SQLExecutor sqlExecutor) throws SQLException {
        if (sqlExecutor != null) {
            sqlExecutor.close();
        }
    }

    @Override
    public void createTemporaryTable(final String table, final Collection<String> columns) throws Exception {
        new SQLCommand(connection).createTemporaryTable(table, columns);
    }

    @Override
    public void createTemporaryTableWithColumnTypes(final String table, final Map<String, String> columns) throws Exception {
        new SQLCommand(connection).createTemporaryTable(table, columns);
    }

    @Override
    public void createTemporaryTable(final String table, final Map<String, Nullable> rawTableColumns) throws SQLException {
        new SQLCommand(connection).createTemporaryTableWithNullableInfo(table, rawTableColumns);
    }

    protected void createTemporaryTable(final String table, final List<ColumnDetails> columns) throws SQLException {
        new SQLCommand(connection).createTemporaryTableWithColumns(table, columns);
    }

    /**
     * @param realTableName
     *        The real table name that the temporary table will be based off.
     * @param columnNames
     *        The list of column names whose structure will be copied.
     * @return returns the temporaryTables name
     */
    @Override
    public String createTemporaryFromRealTableAndUseInTemplates(final String realTableName, final String... columnNames) {
        final String tempTableName = ReplaceTablesWithTempTablesTemplateUtils.useTemporaryTableFor(realTableName);
        try {
            new SQLCommand(connection).createTemporaryFromRealTable(realTableName, tempTableName, columnNames);
        } catch (final SQLException e) {
            printStackTraceWithDetails(e);

            fail("FAIL: Couldn't insert create the temporary table for \"" + realTableName + "\". Some columns to be copied may not exist. "
                    + getSqlDetails(e) + ", columnNames=" + Arrays.toString(columnNames));
        }
        return tempTableName;
    }

    /**
     * @param temporaryTableName
     *        Is the table that was created in createTemporaryTableFromRealTable()
     * @param columnValues
     *        The user will have to add the columnValues varargs in the same order as the columnNames varargs in createTemporaryTableFromRealTable()
     *        The columnValues will have to contain the correct format for the data in a String, e.g. timestamp should be "09 Mar 2012 10:45"
     */
    @Override
    public void insertRowIntoTemporaryTable(final String temporaryTableName, final String... columnValues) {
        try {
            new SQLCommand(connection).insertRowIntoTemporaryTable(temporaryTableName, columnValues);
        } catch (final SQLException e) {
            printStackTraceWithDetails(e);

            fail("FAIL: Couldn't insert row into temporary table \"" + temporaryTableName + "\". May be due to an incorrect number of columns. "
                    + getSqlDetails(e) + ", columnValues: " + Arrays.toString(columnValues));
        }
    }

    @Override
    public void insertRow(final String table, final Map<String, Object> values) throws SQLException {
        insertRowOnSpecificConnection(connection, table, values);
    }

    protected void insertRowOnSpecificConnection(final Connection conn, final String table, final Map<String, Object> values) throws SQLException {
        new SQLCommand(conn).insertRow(table, values);
    }

    protected boolean isRawTable(final String table) {
        return table.contains("RAW");
    }

    protected long getRandomIMSI() {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 15; i++) {
            sb.append((char) ('0' + random.nextInt(10)));
        }
        System.out.println(sb.toString());
        return Long.parseLong(sb.toString());
    }

    protected void assertJSONSucceeds(final String json) {
        jsonAssertUtils.assertJSONSucceeds(json);
    }

    protected List<T> runQueryAssertJSONStringTransform(final Service service, final MultivaluedMap<String, String> parameters) throws Exception {
        final String json = runQuery(service, parameters);
        final ResultTranslator<T> resultTranslator1 = getTranslator();
        return resultTranslator1.translateResult(json,
                (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
    }

    protected String runQuery(final Service service, final MultivaluedMap<String, String> parameters) {
        final String json = runQueryWithoutAssertingJsonIsSuccessful(service, parameters);
        assertJSONSucceeds(json);
        return json;
    }

    protected String runQueryWithoutAssertingJsonIsSuccessful(final Service service, final MultivaluedMap<String, String> parameters) {
        addURITypeParameters(parameters);
        final String json = service.getData(parameters);
        System.out.println(json);
        return json;
    }

    private void addURITypeParameters(final MultivaluedMap<String, String> parameters) {
        parameters.add(REQUEST_URI, DUMMY_URI);
        parameters.add(REQUEST_ID, SAMPLE_REQUEST_ID);
    }

    /**
     * validate the JSON result from a query against the schema defined in the UIMetaData.json file
     *
     * @param json
     *        result from SQL query
     * @param gridID
     *        id of the grid for this data in the UIMetaData.json file
     */
    protected void validateAgainstGridDefinition(final String json, final String gridID) throws Exception {
        new QueryResultValidator().validateAgainstGridDefinition(json, gridID);
    }

    public void setLoadBalancingPolicyService(final LoadBalancingPolicyService loadBalancingPolicyService) {
        this.loadBalancingPolicyService = loadBalancingPolicyService;
    }

    public void setTechPackListFactory(final TechPackListFactory techPackListFactory) {
        this.techPackListFactory = techPackListFactory;
    }

    /**
     * @param dateTimeHelper
     *        the dateTimeHelper to set
     */
    public void setDateTimeHelper(final DateTimeHelper dateTimeHelper) {
        this.dateTimeHelper = dateTimeHelper;
    }

    /**
     * @param parameterChecker
     *        the parameterChecker to set
     */
    public void setParameterChecker(final ParameterChecker parameterChecker) {
        this.parameterChecker = parameterChecker;
    }

    public void setMediaTypeHandler(final MediaTypeHandler mediaTypeHandler) {
        this.mediaTypeHandler = mediaTypeHandler;
    }

    /**
     * @param queryExecutor
     *        the queryExecutor to set
     */
    public void setQueryExecutor(final DataServiceQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    /**
     * Event times returned from a query contain a '.0' millisecond This method allows specified expected event time values to be appended with the
     * '.0'
     *
     * @param inputString
     *        - the input string to which ".0" will be appended
     */
    public String appendZeroMilliseconds(final String inputString) {
        return inputString + ".0";
    }

    /**
     * @param templateMappingEngine
     *        the templateMappingEngine to set
     */
    protected void setTemplateMappingEngine(final TemplateMappingEngine templateMappingEngine) {
        this.templateMappingEngine = templateMappingEngine;
    }
}