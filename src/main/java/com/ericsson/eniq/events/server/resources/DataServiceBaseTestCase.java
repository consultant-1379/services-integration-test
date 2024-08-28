package com.ericsson.eniq.events.server.resources;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;

import org.junit.After;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.ericsson.eniq.events.server.datasource.loadbalancing.LoadBalancingPolicyFactory;
import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.kpi.KPIQueryfactory;
import com.ericsson.eniq.events.server.kpi.MssKpiQueryFactoryHelper;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.logging.performance.ServicePerformanceTraceLogger;
import com.ericsson.eniq.events.server.query.*;
import com.ericsson.eniq.events.server.services.StreamingDataService;
import com.ericsson.eniq.events.server.services.datatiering.DataTieringHandler;
import com.ericsson.eniq.events.server.services.exclusivetacs.ExclusiveTACHandler;
import com.ericsson.eniq.events.server.services.impl.DataServiceBean;
import com.ericsson.eniq.events.server.services.impl.TechPackCXCMappingService;
import com.ericsson.eniq.events.server.templates.mappingengine.TemplateMappingEngine;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;
import com.ericsson.eniq.events.server.test.JNDIPropertiesForTest;
import com.ericsson.eniq.events.server.test.QueryCommand;
import com.ericsson.eniq.events.server.test.stubs.DummyHttpHeaders;
import com.ericsson.eniq.events.server.test.stubs.DummyUriInfoImpl;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;
import com.ericsson.eniq.events.server.utils.*;
import com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager;
import com.ericsson.eniq.events.server.utils.config.latency.TechPackTechnologies;
import com.ericsson.eniq.events.server.utils.datetime.DateTimeHelper;
import com.ericsson.eniq.events.server.utils.techpacks.RawTableFetcher;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackListFactory;

/**
 * Base test case class for all data service tests
 *
 * @author edeccox
 */
public abstract class DataServiceBaseTestCase extends AbstractDependencyInjectionSpringContextTests {

    protected TechPackTechnologies techPackTechnologies;

    protected RequestIdMappingService requestIdMappingService;

    protected DataServiceBean dataServiceBean;

    protected ApplicationConfigManager applicationConfigManager;

    protected StreamingDataService streamingDataService;

    protected QueryUtils queryUtils;

    protected TemplateUtils templateUtils;

    protected QueryCommand queryCommand;

    protected KPIQueryfactory lteQueryBuilder;

    protected LoadBalancingPolicyFactory loadBalancingPolicyFactory;

    protected ServicePerformanceTraceLogger servicePerformanceTraceLogger;

    protected MssKpiQueryFactoryHelper mssQueryFactoryHelper;

    // eemecoy do not remove - used to set up the JDNI properties for test
    private JNDIPropertiesForTest jnidProperties;

    protected DummyUriInfoImpl testUri;

    protected TemplateMappingEngine templateMappingEngine;

    private AuditService auditService;

    private MediaTypeHandler mediaTypeHandler;

    private LoadBalancingPolicyService loadBalancingPolicyService;

    private CSVResponseBuilder csvResponseBuilder;

    private TechPackListFactory techPackListFactory;

    private RawTableFetcher rawTableFetcher;

    protected RMIEngineUtils rmiEngineUtils;

    private ExclusiveTACHandler exclusiveTACHandler;

    protected TechPackCXCMappingService techPackCXCMappingService;

    protected DataServiceQueryExecutor dataServiceQueryExecutor;

    protected JSONAssertUtils jsonAssertUtils;

    protected TimeRangeSelector timeRangeSelector;

    protected DateTimeHelper dateTimeHelper;

    protected DataTieringHandler dataTieringHandler;

    public DataServiceBaseTestCase() {
        super();

        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
        setUpUriInfo();
    }

    @SuppressWarnings("deprecation")
    @Override
    @After
    public void onTearDown() throws Exception {
        super.onTearDown();
        ReplaceTablesWithTempTablesTemplateUtils.removeExtraKeysForTempTables();
    }

    protected void attachDependencies(final BaseResource baseResource) {
        baseResource.setTemplateUtils(templateUtils);
        baseResource.setDataService(dataServiceBean);
        baseResource.setQueryUtils(queryUtils);

        baseResource.setApplicationConfigManager(applicationConfigManager);
        baseResource.setHttpHeaders(new DummyHttpHeaders());

        baseResource.templateMappingEngine = this.templateMappingEngine;
        baseResource.performanceTrace = this.servicePerformanceTraceLogger;
        baseResource.setAuditService(auditService);
        baseResource.setMediaTypeHandler(mediaTypeHandler);
        loadBalancingPolicyService.setEniqEventsProperties(new Properties());
        baseResource.setLoadBalancingPolicyService(loadBalancingPolicyService);
        baseResource.setCsvResponseBuilder(csvResponseBuilder);
        baseResource.setTechPackListFactory(techPackListFactory);
        baseResource.setRawTableFetcher(rawTableFetcher);
        baseResource.setExclusiveTACHandler(exclusiveTACHandler);
        baseResource.setTimeRangeSelector(timeRangeSelector);
        baseResource.setDateTimeHelper(dateTimeHelper);
        baseResource.setDataTieringHandler(dataTieringHandler);
        baseResource.uriInfo = testUri;
    }

    public void setDataServiceBean(final DataServiceBean dataServiceBean) {
        this.dataServiceBean = dataServiceBean;
    }

    public void setJsonAssertUtils(final JSONAssertUtils jsonAssertUtils) {
        this.jsonAssertUtils = jsonAssertUtils;
    }

    public void setRequestIdMappingService(final RequestIdMappingService requestIdMappingService) {
        this.requestIdMappingService = requestIdMappingService;
    }

    public void setApplicationConfigManager(final ApplicationConfigManager applicationConfigManager) {
        this.applicationConfigManager = applicationConfigManager;
    }

    public void setStreamingDataService(final StreamingDataService streamingDataService) {
        this.streamingDataService = streamingDataService;
    }

    public void setJnidProperties(final JNDIPropertiesForTest jnidProperties) {
        this.jnidProperties = jnidProperties;
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

    /**
     * @param lteQueryBuilder
     *        the lteQueryBuilder to set
     */
    public void setLteQueryBuilder(final KPIQueryfactory lteQueryBuilder) {
        this.lteQueryBuilder = lteQueryBuilder;
    }

    public void setMssQueryFactoryHelper(final MssKpiQueryFactoryHelper mssQueryFactoryHelper) {
        this.mssQueryFactoryHelper = mssQueryFactoryHelper;
    }

    public void setTemplateMappingEngine(final TemplateMappingEngine templateMappingEngine) {
        this.templateMappingEngine = templateMappingEngine;
    }

    /**
     * used to set up the JDNI properties for test
     *
     * @param bean
     */
    public void setJndiProperties(final JNDIPropertiesForTest bean) {
        this.jnidProperties = bean;
    }

    // specifies the Spring configuration to load for this test fixture
    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath:com/ericsson/eniq/events/server/resources/DataServiceBaseTestCase-context.xml" };
    }

    protected void assertJSONSucceeds(final String testString) {
        jsonAssertUtils.assertJSONSucceeds(testString, getClass().getName());
    }

    private void logTestResult(final String testString) {
        ServicesLogger.detailed(getClass().getName(), "assertResultContains", "JSON = " + testString);
    }

    protected void assertJSONErrorResult(final String testString) {
        jsonAssertUtils.assertJSONErrorResult(testString, getClass().getName());
    }

    protected void assertNoSuchDisplay(final String jsonMessage, final String displayParameter) {
        jsonAssertUtils.assertNoSuchDisplay(jsonMessage, displayParameter, getClass().getName());
    }

    protected void assertResultContains(final String testString, final String matchString) {
        logTestResult(testString);
        assertThat("Result reported failure: " + testString, testString.contains(matchString), is(true));
    }

    protected void assertDataIsNotInJSONFormat(final String result) {
        jsonAssertUtils.assertDataIsNotInJSONFormat(result, getClass().getName());
    }

    private void setUpUriInfo() {
        try {
            testUri = new DummyUriInfoImpl(null, "baseURI", "somePath");
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setServicePerformanceTraceLogger(final ServicePerformanceTraceLogger servicePerformanceTraceLogger) {
        this.servicePerformanceTraceLogger = servicePerformanceTraceLogger;
    }

    public void setLoadBalancingPolicyFactory(final LoadBalancingPolicyFactory loadBalancingPolicyFactory) {
        this.loadBalancingPolicyFactory = loadBalancingPolicyFactory;
    }

    public void setAuditSerivce(final AuditService auditService) {
        this.auditService = auditService;
    }

    public void setMediaTypeHandler(final MediaTypeHandler mediaTypeHandler) {
        this.mediaTypeHandler = mediaTypeHandler;
    }

    public void setLoadBalancingPolicyService(final LoadBalancingPolicyService loadBalancingPolicyService) {
        this.loadBalancingPolicyService = loadBalancingPolicyService;
    }

    public void setCsvResponseBuilder(final CSVResponseBuilder csvResponseBuilder) {
        this.csvResponseBuilder = csvResponseBuilder;
    }

    public void setTechPackListFactory(final TechPackListFactory techPackTableFactory) {
        this.techPackListFactory = techPackTableFactory;
    }

    /**
     * @return the requestIdMappingService
     */
    public RequestIdMappingService getRequestIdMappingService() {
        return requestIdMappingService;
    }

    /**
     * @return the dataServiceBean
     */
    public DataServiceBean getDataServiceBean() {
        return dataServiceBean;
    }

    /**
     * @return the applicationConfigManager
     */
    public ApplicationConfigManager getApplicationConfigManager() {
        return applicationConfigManager;
    }

    /**
     * @return the streamingDataService
     */
    public StreamingDataService getStreamingDataService() {
        return streamingDataService;
    }

    /**
     * @return the queryUtils
     */
    public QueryUtils getQueryUtils() {
        return queryUtils;
    }

    /**
     * @return the templateUtils
     */
    public TemplateUtils getTemplateUtils() {
        return templateUtils;
    }

    /**
     * @return the queryCommand
     */
    public QueryCommand getQueryCommand() {
        return queryCommand;
    }

    /**
     * @return the lteQueryBuilder
     */
    public KPIQueryfactory getLteQueryBuilder() {
        return lteQueryBuilder;
    }

    /**
     * @return the loadBalancingPolicyFactory
     */
    public LoadBalancingPolicyFactory getLoadBalancingPolicyFactory() {
        return loadBalancingPolicyFactory;
    }

    /**
     * @return the servicePerformanceTraceLogger
     */
    public ServicePerformanceTraceLogger getServicePerformanceTraceLogger() {
        return servicePerformanceTraceLogger;
    }

    /**
     * @return the mssQueryFactoryHelper
     */
    public MssKpiQueryFactoryHelper getMssQueryFactoryHelper() {
        return mssQueryFactoryHelper;
    }

    /**
     * @return the jndiProperties
     */
    public JNDIPropertiesForTest getJnidProperties() {
        return jnidProperties;
    }

    /**
     * @return the testUri
     */
    public DummyUriInfoImpl getTestUri() {
        return testUri;
    }

    /**
     * @return the templateMappingEngine
     */
    public TemplateMappingEngine getTemplateMappingEngine() {
        return templateMappingEngine;
    }

    public void setRawTableFetcher(final RawTableFetcher rawTableFetcher) {
        this.rawTableFetcher = rawTableFetcher;
    }

    public void setRMIEngineUtils(final RMIEngineUtils rmiEngineUtils) {
        this.rmiEngineUtils = rmiEngineUtils;

    }

    /**
     * @param exclusiveTACHandler
     *        the exclusiveTACHandler to set
     */
    public void setExclusiveTACHandler(final ExclusiveTACHandler exclusiveTACHandler) {
        this.exclusiveTACHandler = exclusiveTACHandler;
    }

    /**
     * @return the dataServiceQueryExecutor
     */
    public DataServiceQueryExecutor getDataServiceQueryExecutor() {
        return dataServiceQueryExecutor;
    }

    /**
     * @param dataServiceQueryExecutor
     *        the dataServiceQueryExecutor to set
     */
    public void setDataServiceQueryExecutor(final DataServiceQueryExecutor dataServiceQueryExecutor) {
        this.dataServiceQueryExecutor = dataServiceQueryExecutor;
    }

    /**
     *
     * @param techPackTechnologies
     */
    public void setTechPackTechnologies(final TechPackTechnologies techPackTechnologies) {
        this.techPackTechnologies = techPackTechnologies;
    }

    /**
     *
     * @return techPackTechnologies
     */
    public TechPackTechnologies getTechPackTechnologies() {
        return techPackTechnologies;
    }

    /**
     *
     * @param techPackCXCMappingService
     */
    public void setTechPackCXCMappingService(final TechPackCXCMappingService techPackCXCMappingService) {
        this.techPackCXCMappingService = techPackCXCMappingService;
    }

    /**
     *
     * @return techPackCXCMappingService
     */
    public TechPackCXCMappingService getTechPackCXCMappingService() {
        return techPackCXCMappingService;
    }

    public void setTimeRangeSelector(final TimeRangeSelector timeRangeSelector) {
        this.timeRangeSelector = timeRangeSelector;
    }

    public void setDateTimeHelper(final DateTimeHelper dateTimeHelper) {
        this.dateTimeHelper = dateTimeHelper;
    }

    public void setDataTieringHandler(final DataTieringHandler dataTieringHandler) {
        this.dataTieringHandler = dataTieringHandler;
    }

}