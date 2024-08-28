/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import com.ericsson.eniq.events.server.services.exclusivetacs.ExclusiveTACHandler;

import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.SAMPLE_EXCLUSIVE_TAC_2_TO_STRING;
import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.SAMPLE_EXCLUSIVE_TAC_TO_STRING;

/**
 * Bypassing the real ExclusiveTACHandler as this queries the actual database, and didn't want
 * another database call in the data integrity tests (this adds complexity to the connection management where the
 * one connection must be kept open for the duration of the test)
 * 
 * @author eemecoy
 *
 */
public class StubbedExclusiveTACHandler extends ExclusiveTACHandler {

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.exclusivetacs.ExclusiveTACHandler#isTacInExclusiveTacGroup(java.lang.String)
     */
    @Override
    public boolean isTacInExclusiveTacGroup(final String tac) {
        return SAMPLE_EXCLUSIVE_TAC_TO_STRING.equals(tac) || SAMPLE_EXCLUSIVE_TAC_2_TO_STRING.equals(tac);
    }

}
