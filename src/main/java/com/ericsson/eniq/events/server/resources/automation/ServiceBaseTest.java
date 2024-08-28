package com.ericsson.eniq.events.server.resources.automation;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;
import static org.junit.Assert.*;

import java.util.logging.Level;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import com.ericsson.eniq.events.server.json.JSONException;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.serviceprovider.Service;
import com.ericsson.eniq.events.server.serviceprovider.impl.GenericService;
import com.ericsson.eniq.events.server.test.jsonformatter.FormattedJSONObject;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;

@ContextConfiguration(locations = { "classpath:com/ericsson/eniq/events/server/resources/BaseServiceIntegrationTest-context.xml" })
public abstract class ServiceBaseTest {

    @Autowired
    protected JSONAssertUtils jsonAssertUtils;

    private TestContextManager testContextManager;

    public ServiceBaseTest() {
        System.setProperty(ServicesLogger.SERVICES_LOGGER_NAME + ".stdout", "true");
        System.setProperty(ServicesLogger.TRACE_MAX_MESSAGE_LENGTH, "100000");
        ServicesLogger.setLevel(Level.FINE);
        ServicesLogger.initializePropertiesAndLoggers();
    }

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    protected String runQuery(final MultivaluedMap<String, String> parameters, final Service service) {
        final String json = runQueryWithoutAssesrtingJsonSucceeds(parameters, service);
        try {
            System.out.println(new FormattedJSONObject(json));
        } catch (final JSONException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        jsonAssertUtils.assertJSONSucceeds(json);
        return json;
    }

    protected String runQueryWithoutAssesrtingJsonSucceeds(final MultivaluedMap<String, String> parameters,
            final Service service) {
        addURITypeParameters(parameters);
        return service.getData(parameters);
    }

    private void addURITypeParameters(final MultivaluedMap<String, String> parameters) {
        parameters.add(REQUEST_URI, DUMMY_URI);
        parameters.add(REQUEST_ID, SAMPLE_REQUEST_ID);
    }

    protected Response runQueryForCSV(final MultivaluedMap<String, String> parameters, final GenericService service) {
        addURITypeParameters(parameters);
        return service.getDataAsCSV(parameters, null);
    }
}
