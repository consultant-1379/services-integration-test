/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.eniq.events.server.test.stubs;

import static com.ericsson.eniq.events.server.utils.config.ApplicationConfigManager.*;

import com.ericsson.eniq.events.server.utils.config.PropertyStore;

public class StubbedPropertyStore extends PropertyStore {
    private boolean isSuccessRawEnabled = true;

    @Override
    public boolean getBooleanPropertyValue(final String key, final boolean defaultValue) {
        if (ENIQ_EVENTS_SUC_RAW.equals(key)) {
            return isSuccessRawEnabled;
        }
        return super.getBooleanPropertyValue(key, defaultValue);
    }

    public void setIsSuccessRawEnabled(final boolean isSucRawEnabled) {
        isSuccessRawEnabled = isSucRawEnabled;
    }
}
