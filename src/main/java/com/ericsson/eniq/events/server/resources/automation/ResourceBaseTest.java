package com.ericsson.eniq.events.server.resources.automation;

import java.util.logging.Level;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;

@ContextConfiguration(locations = {
        "classpath:com/ericsson/eniq/events/server/resources/DataServiceBaseTestCase-context.xml",
        "classpath:com/ericsson/eniq/events/server/resources/automation/test-resource-context.xml" })
public abstract class ResourceBaseTest {

    protected JSONAssertUtils jsonAssertUtils = new JSONAssertUtils();

    private TestContextManager testContextManager;

    public ResourceBaseTest() {
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

}
