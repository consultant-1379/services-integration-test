/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import com.ericsson.eniq.events.server.common.exception.CannotAccessLicensingServiceException;
import com.ericsson.eniq.events.server.licensing.LicensingService;
import com.ericsson.eniq.licensing.cache.LicenseInformation;

import java.util.Vector;

/**
 * @author EEMECOY
 *
 */
public class StubbedLicensingService implements LicensingService {

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.licensing.LicensingService#hasLicense(java.lang.String)
     */
    @Override
    public boolean hasLicense(final String licenseCXC) throws CannotAccessLicensingServiceException {
        return true;
    }

    @Override
    public Vector<LicenseInformation> getLicenseInformation() throws CannotAccessLicensingServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
