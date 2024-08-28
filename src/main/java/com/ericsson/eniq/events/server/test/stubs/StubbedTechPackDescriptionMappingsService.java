/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.events.server.utils.techpacks.TechPackDescriptionMappingsService;

/**
 * Stubbed version of class that overrides the production class's reading non existent flat file during test
 * @author EEMECOY
 *
 */
public class StubbedTechPackDescriptionMappingsService extends TechPackDescriptionMappingsService {

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.techpacks.TechPackDescriptionMappingsService#readAndCacheFeatureDescriptions()
     */
    @Override
    public void readAndCacheFeatureDescriptions() throws IOException {

    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.techpacks.TechPackDescriptionMappingsService#getFeatureDescriptionsForTechPacks(java.util.List)
     */
    @Override
    public List<String> getFeatureDescriptionsForTechPacks(final List<String> techPackNames) {
        return new ArrayList<String>();
    }

}
