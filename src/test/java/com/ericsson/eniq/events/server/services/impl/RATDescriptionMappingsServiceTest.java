/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.services.impl;

import com.ericsson.eniq.events.server.resources.DataServiceBaseTestCase;
import com.ericsson.eniq.events.server.utils.RATDescriptionMappingUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author eemecoy
 */
public class RATDescriptionMappingsServiceTest extends DataServiceBaseTestCase {

    private static final String FAKE_RAT_INTEGER_VALUE = "3131";

    private static final String RAT_VALUE_FOR_GSM = "0";

    private static final String RAT_STRING_VALUE_FOR_GSM = "GSM";

    private static final String RAT_VALUE_FOR_3G = "1";

    private static final String RAT_STRING_VALUE_FOR_3G = "3G";

    private static final String FAKE_RAT_STRING_VALUE = "non existent network type";

    private RATDescriptionMappingsService objToTest;

    private RATDescriptionMappingUtils ratDescriptionMappingUtils;

    @Before
    public void onSetUp() {
        objToTest = new RATDescriptionMappingsService();
        ratDescriptionMappingUtils = new RATDescriptionMappingUtils();
        objToTest.setTemplateUtils(templateUtils);
        objToTest.setDataService(dataServiceBean);
        objToTest.ratDescriptionMappingUtils = ratDescriptionMappingUtils;
        objToTest.populateMapFromRATTableInDB();

    }

    @Test
    public void testIsARATValueReturnsTrueForGenuineRATValues() {
        assertThat(ratDescriptionMappingUtils.isaRATValue(RAT_VALUE_FOR_GSM), is(true));
        assertThat(ratDescriptionMappingUtils.isaRATValue(RAT_VALUE_FOR_3G), is(true));
    }

    @Test
    public void testIsARATValueReturnsFalseForFakeRATValues() {
        assertThat(ratDescriptionMappingUtils.isaRATValue(FAKE_RAT_INTEGER_VALUE), is(false));
    }

    @Test
    public void testGetRATIntegerValuesForGenuineRATValues() {
        assertThat(ratDescriptionMappingUtils.getRATIntegerValue(RAT_STRING_VALUE_FOR_GSM), is(RAT_VALUE_FOR_GSM));
        assertThat(ratDescriptionMappingUtils.getRATIntegerValue(RAT_STRING_VALUE_FOR_3G), is(RAT_VALUE_FOR_3G));
    }

    @Test
    public void testGetRATIntegerValuesForFakeRATValuesJustReturnsTheInputtedValue() {
        assertThat(ratDescriptionMappingUtils.getRATIntegerValue(FAKE_RAT_STRING_VALUE), is(FAKE_RAT_STRING_VALUE));
        assertThat(ratDescriptionMappingUtils.getRATIntegerValue(null), is((String) null));
    }

    @Test
    public void testGetRATDescriptionForGenuineRATValues() {
        assertThat(ratDescriptionMappingUtils.getRATDescription(RAT_VALUE_FOR_GSM), is(RAT_STRING_VALUE_FOR_GSM));
        assertThat(ratDescriptionMappingUtils.getRATDescription(RAT_VALUE_FOR_3G), is(RAT_STRING_VALUE_FOR_3G));
    }

    @Test
    public void testGetRATDescriptionForFakeRATValuesJustReturnsTheInputtedValue() {
        assertThat(ratDescriptionMappingUtils.getRATDescription(FAKE_RAT_INTEGER_VALUE), is(FAKE_RAT_INTEGER_VALUE));
    }
}
