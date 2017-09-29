// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.impl;

import java.text.ParseException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * created by talend on Nov 19, 2014 Detailled comment
 * 
 */
public class FrequencyIndicatorImplTest {

    /**
     * DOC talend Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC talend Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC talend Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 1 frequency case
     */
    @Test
    public void testGetDBNameCase1() {
        String normalStr = "str"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createFrequencyIndicator();
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is one blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING, dbName);
        // input data is two blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING, dbName);
        // input data is noraml string
        dbName = createFrequencyIndicator.getDBName(normalStr);
        Assert.assertEquals(normalStr, dbName);
        // input data is noraml string surround space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + normalStr + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + normalStr + PluginConstant.SPACE_STRING, dbName);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 2 pattern frequency indicator case
     */
    @Test
    public void testGetDBNameCase2() {
        String normalStr = "strAM132h"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createPatternFreqIndicator();
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is one blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING, dbName);
        // input data is two blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING, dbName);
        // input data is l string
        dbName = createFrequencyIndicator.getDBName(normalStr);
        Assert.assertEquals("aaaAA999a", dbName); //$NON-NLS-1$
        // input data is noraml string surround space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + normalStr + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + "aaaAA999a" + PluginConstant.SPACE_STRING, dbName); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 3 pattern low frequency indicator case
     */
    @Test
    public void testGetDBNameCase3() {
        String normalStr = "strAM132h"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createPatternLowFreqIndicator();
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is one blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING, dbName);
        // input data is two blank space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING, dbName);
        // input data is l string
        dbName = createFrequencyIndicator.getDBName(normalStr);
        Assert.assertEquals("aaaAA999a", dbName); //$NON-NLS-1$
        // input data is noraml string surround space string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.SPACE_STRING + normalStr + PluginConstant.SPACE_STRING);
        Assert.assertEquals(PluginConstant.SPACE_STRING + "aaaAA999a" + PluginConstant.SPACE_STRING, dbName); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 4 Quarter Frequency Indicator case
     * 
     * @throws ParseException
     */
    @Test
    public void testGetDBNameCase4() throws ParseException {
        String normalStr = "2008-09-20 22:12:55"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createQuarterFrequencyIndicator();
        createFrequencyIndicator.datePattern = "yyyy"; //$NON-NLS-1$
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is normal string
        java.text.SimpleDateFormat timeformat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //$NON-NLS-1$
        java.util.Date inputDate = timeformat.parse(normalStr);
        dbName = createFrequencyIndicator.getDBName(inputDate);
        Assert.assertEquals("20083", dbName); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 5 Quarter Low Frequency Indicator case
     * 
     * @throws ParseException
     */
    @Test
    public void testGetDBNameCase5() throws ParseException {
        String normalStr = "2008-09-20 22:12:55"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createQuarterLowFrequencyIndicator();
        createFrequencyIndicator.datePattern = "yyyy"; //$NON-NLS-1$
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is normal string
        java.text.SimpleDateFormat timeformat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //$NON-NLS-1$
        java.util.Date inputDate = timeformat.parse(normalStr);
        dbName = createFrequencyIndicator.getDBName(inputDate);
        Assert.assertEquals("20083", dbName); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 6 Week Frequency Indicator case
     * 
     * @throws ParseException
     */
    @Test
    public void testGetDBNameCase6() throws ParseException {
        String normalStr = "2008-09-20 22:12:55"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createWeekFrequencyIndicator();
        createFrequencyIndicator.datePattern = "yyyyMM"; //$NON-NLS-1$
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is noraml string
        java.text.SimpleDateFormat timeformat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //$NON-NLS-1$
        java.util.Date inputDate = timeformat.parse(normalStr);
        dbName = createFrequencyIndicator.getDBName(inputDate);
        Assert.assertEquals("20080938", dbName); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDBName(java.lang.Object)}
     * . case 7 Week Low Frequency Indicator case
     * 
     * @throws ParseException
     */
    @Test
    public void testGetDBNameCase7() throws ParseException {
        String normalStr = "2008-09-20 22:12:55"; //$NON-NLS-1$
        FrequencyIndicatorImpl createFrequencyIndicator = (FrequencyIndicatorImpl) IndicatorsFactory.eINSTANCE
                .createWeekLowFrequencyIndicator();
        createFrequencyIndicator.datePattern = "yyyyMM"; //$NON-NLS-1$
        // input data is null case
        String dbName = createFrequencyIndicator.getDBName(null);
        Assert.assertEquals(SpecialValueDisplay.NULL_FIELD, dbName);
        // input data is empty string
        dbName = createFrequencyIndicator.getDBName(PluginConstant.EMPTY_STRING);
        Assert.assertEquals(SpecialValueDisplay.EMPTY_FIELD, dbName);
        // input data is noraml string
        java.text.SimpleDateFormat timeformat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //$NON-NLS-1$
        java.util.Date inputDate = timeformat.parse(normalStr);
        dbName = createFrequencyIndicator.getDBName(inputDate);
        Assert.assertEquals("20080938", dbName); //$NON-NLS-1$
    }

}
