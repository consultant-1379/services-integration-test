/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import com.ericsson.eniq.events.server.utils.techpacks.TechPackLicensingService;

/**
 * Class to bypass querying the database for the tech pack license numbers, and querying the ENIQ licensing service
 * for license details
 * @author eemecoy
 *
 */
public class StubbedTechPackLicensingService extends TechPackLicensingService {

    /* (non-Javadoc)
    * @see com.ericsson.eniq.events.server.utils.techpacks.TechPackLicensingService#isTechPackLicensed(java.lang.String)
    */
    @Override
    public boolean isTechPackLicensed(final String techPackName) {
        return true;
    }

}
