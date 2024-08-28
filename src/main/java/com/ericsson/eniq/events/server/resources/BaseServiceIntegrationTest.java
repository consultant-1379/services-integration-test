/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ericsson.eniq.events.server.integritytests.stubs.ReplaceTablesWithTempTablesTemplateUtils;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.logging.performance.ServicePerformanceTraceLogger;
import com.ericsson.eniq.events.server.query.QueryGenerator;
import com.ericsson.eniq.events.server.serviceprovider.impl.GenericService;
import com.ericsson.eniq.events.server.services.DataService;
import com.ericsson.eniq.events.server.services.StreamingDataService;
import com.ericsson.eniq.events.server.services.datatiering.DataTieringHandler;
import com.ericsson.eniq.events.server.services.exclusivetacs.ExclusiveTACHandler;
import com.ericsson.eniq.events.server.templates.mappingengine.TemplateMappingEngine;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;
import com.ericsson.eniq.events.server.test.stubs.DummyHttpServletResponse;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;
import com.ericsson.eniq.events.server.utils.*;
import com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager;
import com.ericsson.eniq.events.server.utils.datetime.DateTimeHelper;
import com.ericsson.eniq.events.server.utils.parameterchecking.ParameterChecker;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackLicensingService;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackListFactory;

/**
 * Base class for tests for the Service class implementations This class will populate the dependencies for the Service class.
 *
 * @author eemecoy
 * 
 *         15/08/2011
 * @author ehaoswa Updated with Spring 3 integration test framework
 */
//Use Spring JUnit 4 for integration testing
@RunWith(SpringJUnit4ClassRunner.class)
//Context file location
@ContextConfiguration(locations = { "classpath:com/ericsson/eniq/events/server/resources/BaseServiceIntegrationTest-context.xml" })
@Ignore
public class BaseServiceIntegrationTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ServicePerformanceTraceLogger servicePerformanceTraceLogger;

    @Autowired
    private ParameterChecker parameterChecker;

    @Autowired
    private DateTimeHelper dateTimeHelper;

    @Autowired
    private LoadBalancingPolicyService loadBalancingPolicyService;

    @Autowired
    private DataService dataService;

    @Autowired
    private QueryGenerator queryGenerator;

    @Autowired
    private QueryUtils queryUtils;

    @Autowired
    private TechPackListFactory techPackListFactory;

    @Autowired
    private MediaTypeHandler mediaTypeHandler;

    @Autowired
    private TemplateUtils templateUtils;

    @Autowired
    private CSVResponseBuilder csvResponseBuilder;

    @Autowired
    private StreamingDataService streamingDataService;

    @Autowired
    private TemplateMappingEngine templateMappingEngine;

    @Autowired
    private ApplicationConfigManager applicationConfigManager;

    @Autowired
    private ExclusiveTACHandler exclusiveTACHandler;

    @Autowired
    protected JSONAssertUtils jsonAssertUtils;

    @Autowired
    private TechPackLicensingService techPackLicensingService;

    @Autowired
    private DataTieringHandler dataTieringHandler;

    @Before
    public void initializeLoggingProperties() {
        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
    }

    @After
    public void onTearDown() throws Exception {
        ReplaceTablesWithTempTablesTemplateUtils.removeExtraKeysForTempTables();
    }

    /**
     * Attach dependencies.
     *
     * @param service
     *        the service
     */
    protected void attachDependencies(final GenericService service) {
        service.setAuditService(auditService);
        service.setPerformanceTrace(servicePerformanceTraceLogger);
        service.setParameterChecker(parameterChecker);
        service.setDateTimeHelper(dateTimeHelper);
        service.setLoadBalancingPolicyService(loadBalancingPolicyService);
        service.setDataService(dataService);
        service.setParameterChecker(parameterChecker);
        service.setQueryGenerator(queryGenerator);
        service.setQueryUtils(queryUtils);
        service.setTechPackListFactory(techPackListFactory);
        service.setMediaTypeHandler(mediaTypeHandler);
        service.setCsvResponseBuilder(csvResponseBuilder);
        service.setStreamingDataService(streamingDataService);
        service.setApplicationConfigManager(applicationConfigManager);
        service.setExclusiveTACHandler(exclusiveTACHandler);
        service.setTechPackLicensingService(techPackLicensingService);
        service.setDataTieringHandler(dataTieringHandler);
    }

    /**
     * Sets the audit service.
     *
     * @param auditService
     *        the auditService to set
     */

    public void setAuditService(final AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Sets the data service.
     *
     * @param dataService
     *        the dataService to set
     */

    public void setDataService(final DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Sets the date time helper.
     *
     * @param dateTimeHelper
     *        the dateTimeHelper to set
     */

    public void setDateTimeHelper(final DateTimeHelper dateTimeHelper) {
        this.dateTimeHelper = dateTimeHelper;
    }

    /**
     * Sets the load balancing policy service.
     *
     * @param loadBalancingPolicyService
     *        the loadBalancingPolicyService to set
     */

    public void setLoadBalancingPolicyService(final LoadBalancingPolicyService loadBalancingPolicyService) {
        this.loadBalancingPolicyService = loadBalancingPolicyService;
    }

    /**
     * Sets the parameter checker.
     *
     * @param parameterChecker
     *        the parameterChecker to set
     */

    public void setParameterChecker(final ParameterChecker parameterChecker) {
        this.parameterChecker = parameterChecker;
    }

    /**
     * Sets the service performance trace logger.
     *
     * @param servicePerformanceTraceLogger
     *        the servicePerformanceTraceLogger to set
     */

    public void setServicePerformanceTraceLogger(final ServicePerformanceTraceLogger servicePerformanceTraceLogger) {
        this.servicePerformanceTraceLogger = servicePerformanceTraceLogger;
    }

    /**
     * Sets the query generator.
     *
     * @param queryGenerator
     *        the new query generator
     */
    public void setQueryGenerator(final QueryGenerator queryGenerator) {
        this.queryGenerator = queryGenerator;

    }

    /**
     * Sets the query utils.
     *
     * @param queryUtils
     *        the new query utils
     */
    public void setQueryUtils(final QueryUtils queryUtils) {
        this.queryUtils = queryUtils;
    }

    /**
     * Assert json succeeds.
     *
     * @param testString
     *        the test string
     */
    protected void assertJSONSucceeds(final String testString) {
        logTestResult(testString);
        jsonAssertUtils.assertJSONSucceeds(testString, getClass().getName());
    }

    /**
     * Assert json error result.
     *
     * @param testString
     *        the test string
     */
    protected void assertJSONErrorResult(final String testString) {
        logTestResult(testString);
        jsonAssertUtils.assertJSONErrorResult(testString, getClass().getName());
    }

    /**
     * Assert no such display.
     *
     * @param jsonMessage
     *        the json message
     * @param displayParameter
     *        the display parameter
     */
    protected void assertNoSuchDisplay(final String jsonMessage, final String displayParameter) {
        jsonAssertUtils.assertNoSuchDisplay(jsonMessage, displayParameter, getClass().getName());
    }

    /**
     * Assert result contains.
     *
     * @param testString
     *        the test string
     * @param matchString
     *        the match string
     */
    protected void assertResultContains(final String testString, final String matchString) {
        logTestResult(testString);
        assertThat("Result reported failure: " + testString, testString.contains(matchString), is(true));
    }

    /**
     * Assert data is not in json format.
     *
     * @param result
     *        the result
     */
    protected void assertDataIsNotInJSONFormat(final String result) {
        jsonAssertUtils.assertDataIsNotInJSONFormat(result, getClass().getName());
    }

    /**
     * Log test result.
     *
     * @param testString
     *        the test string
     */
    private void logTestResult(final String testString) {
        ServicesLogger.detailed(getClass().getName(), "assertResultContains", "JSON = " + testString);
    }

    /**
     * Run query.
     *
     * @param requestParameters
     *        the request parameters
     * @param service
     *        the service
     * @return the string
     */
    protected String runQueryAndAssertJsonSucceeds(final MultivaluedMap<String, String> requestParameters, final GenericService service) {
        final String result = runQuery(requestParameters, service);
        assertJSONSucceeds(result);
        return result;
    }

    protected void runQueryForCSV(final MultivaluedMap<String, String> parameters, final GenericService service) {
        final HttpServletResponse response = new DummyHttpServletResponse();
        addURITypeParameters(parameters);
        final Response result = service.getDataAsCSV(parameters, response);
        System.out.println(result);
    }

    protected String runQuery(final MultivaluedMap<String, String> requestParameters, final GenericService service) {
        addURITypeParameters(requestParameters);
        final String result = service.getData(requestParameters);
        System.out.println(result);
        return result;
    }

    private void addURITypeParameters(final MultivaluedMap<String, String> parameters) {
        parameters.add(REQUEST_URI, DUMMY_URI);
        parameters.add(REQUEST_ID, SAMPLE_REQUEST_ID);
    }

    /**
     * Sets the tech pack list factory.
     *
     * @param techPackListFactory
     *        the new tech pack list factory
     */
    public void setTechPackListFactory(final TechPackListFactory techPackListFactory) {
        this.techPackListFactory = techPackListFactory;
    }

    /**
     * Sets the media type handler.
     *
     * @param mediaTypeHandler
     *        the new media type handler
     */
    public void setMediaTypeHandler(final MediaTypeHandler mediaTypeHandler) {
        this.mediaTypeHandler = mediaTypeHandler;
    }

    /**
     * Gets the data service.
     *
     * @return the data service
     */
    protected DataService getDataService() {
        return dataService;
    }

    /**
     * Gets the service performance trace logger.
     *
     * @return the service performance trace logger
     */
    public ServicePerformanceTraceLogger getServicePerformanceTraceLogger() {
        return servicePerformanceTraceLogger;
    }

    /**
     * Sets the template utils.
     *
     * @param templateUtils
     *        the new template utils
     */
    public void setTemplateUtils(final TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }

    /**
     * Gets the template utils.
     *
     * @return the template utils
     */
    protected TemplateUtils getTemplateUtils() {
        return templateUtils;
    }

    /**
     * Sets the csv response builder.
     *
     * @param csvResponseBuilder
     *        the new csv response builder
     */
    public void setCsvResponseBuilder(final CSVResponseBuilder csvResponseBuilder) {
        this.csvResponseBuilder = csvResponseBuilder;
    }

    /**
     * Sets the streaming data service.
     *
     * @param streamingDataService
     *        the new streaming data service
     */
    public void setStreamingDataService(final StreamingDataService streamingDataService) {
        this.streamingDataService = streamingDataService;
    }

    /**
     * @param templateMappingEngine
     *        the templateMappingEngine to set
     */
    public void setTemplateMappingEngine(final TemplateMappingEngine templateMappingEngine) {
        this.templateMappingEngine = templateMappingEngine;
    }

    public void setDataTieringHandler(final DataTieringHandler dataTieringHandler) {
        this.dataTieringHandler = dataTieringHandler;
    }

    protected TemplateMappingEngine getTemplateMappingEngine() {
        return templateMappingEngine;
    }

    /**
     * @return the queryUtils
     */
    protected QueryUtils getQueryUtils() {
        return queryUtils;
    }
}
