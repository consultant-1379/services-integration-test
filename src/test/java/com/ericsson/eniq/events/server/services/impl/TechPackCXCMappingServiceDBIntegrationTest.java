/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.services.impl;

import static com.ericsson.eniq.events.server.common.TechPackData.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ericsson.eniq.events.server.services.DataService;
import com.ericsson.eniq.events.server.templates.mappingengine.TemplateMappingEngine;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;
import com.ericsson.eniq.events.server.utils.techpacks.TechPackCXCMappingUtils;

/**
 * @author eemecoy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:com/ericsson/eniq/events/server/resources/BaseServiceIntegrationTest-context.xml" })
public class TechPackCXCMappingServiceDBIntegrationTest {

    private static final String _2G_LICENSE_CXC = "CXC4010923";

    private static final String _3G_LICENSE_CXC = "CXC4010924";

    private static final String _4G_LICENSE_CXC = "CXC4010933";

    private static final String NON_EXISTENT_TECH_PACK = "CXCXCXCXCX";

    @Autowired
    private final TechPackCXCMappingService techPackCXCMappingService = new TechPackCXCMappingService();

    @Autowired
    private DataService dataService;

    @Autowired
    private TemplateMappingEngine templateMappingEngine;

    @Autowired
    private TemplateUtils templateUtils;

    @Autowired
    private TechPackCXCMappingUtils techPackCXCMappingUtils;

    @Before
    public void onSetUp() {
        attachDependencies();
    }

    private void attachDependencies() {
        techPackCXCMappingService.setDataService(dataService);
        techPackCXCMappingService.setTechPackCXCMappingUtils(techPackCXCMappingUtils);
        techPackCXCMappingService.setTemplateMappingEngine(templateMappingEngine);
        techPackCXCMappingService.setTemplateUtils(templateUtils);
    }

    @Test
    public void testreadTechPackLicenseNumbers() {
        techPackCXCMappingService.readTechPackLicenseNumbersFromDB();
        final List<String> sgehCXCNumbers = techPackCXCMappingService.getTechPackCXCNumbers(EVENT_E_SGEH);

        assertThat(sgehCXCNumbers.contains(_2G_LICENSE_CXC), is(true));
        assertThat(sgehCXCNumbers.contains(_3G_LICENSE_CXC), is(true));

        final List<String> lteCXCNumber = techPackCXCMappingService.getTechPackCXCNumbers(EVENT_E_LTE);

        assertThat(lteCXCNumber.contains(_4G_LICENSE_CXC), is(true));

        assertThat(techPackCXCMappingService.getTechPackCXCNumbers(NON_EXISTENT_TECH_PACK).isEmpty(), is(true));

    }

    /**
     * @param dataService the dataService to set
     */
    public void setDataService(final DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * @param templateMappingEngine the templateMappingEngine to set
     */
    public void setTemplateMappingEngine(final TemplateMappingEngine templateMappingEngine) {
        this.templateMappingEngine = templateMappingEngine;
    }

    /**
     * @param templateUtils the templateUtils to set
     */
    public void setTemplateUtils(final TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }

    /**
     * @param techPackCXCMappingUtils the techPackCXCMappingUtils to set
     */
    public void setTechPackCXCMappingUtils(final TechPackCXCMappingUtils techPackCXCMappingUtils) {
        this.techPackCXCMappingUtils = techPackCXCMappingUtils;
    }

}
