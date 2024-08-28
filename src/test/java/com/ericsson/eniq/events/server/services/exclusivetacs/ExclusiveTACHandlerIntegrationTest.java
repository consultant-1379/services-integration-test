/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.services.exclusivetacs;

import static com.ericsson.eniq.events.server.test.common.ApplicationTestConstants.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ericsson.eniq.events.server.services.DataService;
import com.ericsson.eniq.events.server.templates.mappingengine.TemplateMappingEngine;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;
import com.ericsson.eniq.events.server.utils.QueryUtils;

/**
 * @author EEMECOY
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:com/ericsson/eniq/events/server/resources/DataServiceBaseTestCase-context.xml" })
public class ExclusiveTACHandlerIntegrationTest {

    private ExclusiveTACHandler exclusiveTACHandler;

    @Autowired
    private QueryUtils queryUtils;

    @Autowired
    private DataService dataService;

    @Autowired
    private TemplateMappingEngine templateMappingEngine;

    @Before
    public void setup() {
        exclusiveTACHandler = new ExclusiveTACHandler();
        exclusiveTACHandler.setQueryUtils(queryUtils);
        exclusiveTACHandler.setDataService(dataService);
        exclusiveTACHandler.setTemplateMappingEngine(templateMappingEngine);
        final TemplateUtils templateUtils = new TemplateUtils();
        exclusiveTACHandler.setTemplateUtils(templateUtils);
    }

    @Test
    public void testQueryRunsWithoutError() {
        exclusiveTACHandler.isTacInExclusiveTacGroup(Integer.toString(SAMPLE_EXCLUSIVE_TAC));
    }

}
