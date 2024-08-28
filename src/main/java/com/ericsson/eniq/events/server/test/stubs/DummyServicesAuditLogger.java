package com.ericsson.eniq.events.server.test.stubs;

import java.util.logging.Level;

import javax.annotation.PostConstruct;

import com.ericsson.eniq.events.server.logging.audit.ServicesAuditLogger;

/**
 * Dummy instance of the ServicesAuditLogger class. It has been found that the audit logging is
 * adding greatly to CI testing time, and therefore the logging methods are overridden below
 * to offer a performance boost for test purposes only.
 * 
 * 
 * @author eriwals
 *
 */
public class DummyServicesAuditLogger extends ServicesAuditLogger {

    @PostConstruct
    @Override
    public void staticInit() {
        // do nothing
    }

    @Override
    public void detailed(final Level logLevel, final Object... info) {
        // do nothing
    }

}
