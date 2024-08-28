package com.ericsson.eniq.events.server.test.stubs;

import java.util.logging.Level;

import javax.annotation.PostConstruct;

import com.ericsson.eniq.events.server.logging.performance.ServicePeformanceContextInformation;
import com.ericsson.eniq.events.server.logging.performance.ServicePerformanceTraceLogger;

/**
 * Dummy instance of the ServicePerformanceTraceLogger class. It has been found that the logging is
 * adding greatly to CI testing time, and therefore the logging methods are overridden below
 * to offer a performance boost for test purposes only.
 * 
 * 
 * @author echchik
 *
 */
public class DummyServicePerformanceTraceLogger extends ServicePerformanceTraceLogger {

    @PostConstruct
    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void detailed(final Level logLevel, final ServicePeformanceContextInformation serContxt) {
        // do nothing
    }
}
