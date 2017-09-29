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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.dataquality.indicators.BinFrequencyIndicator;
import org.talend.dataquality.indicators.BinLowFrequencyIndicator;
import org.talend.dataquality.indicators.DateFrequencyIndicator;
import org.talend.dataquality.indicators.DateLowFrequencyIndicator;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator;
import org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.LowFrequencyIndicator;
import org.talend.dataquality.indicators.MonthFrequencyIndicator;
import org.talend.dataquality.indicators.MonthLowFrequencyIndicator;
import org.talend.dataquality.indicators.PatternFreqIndicator;
import org.talend.dataquality.indicators.PatternLowFreqIndicator;
import org.talend.dataquality.indicators.QuarterFrequencyIndicator;
import org.talend.dataquality.indicators.QuarterLowFrequencyIndicator;
import org.talend.dataquality.indicators.SoundexFreqIndicator;
import org.talend.dataquality.indicators.SoundexLowFreqIndicator;
import org.talend.dataquality.indicators.WeekFrequencyIndicator;
import org.talend.dataquality.indicators.WeekLowFrequencyIndicator;
import org.talend.dataquality.indicators.YearFrequencyIndicator;
import org.talend.dataquality.indicators.YearLowFrequencyIndicator;
import org.talend.dataquality.service.IndicatorDefaultValueServiceUtil;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class IndicatorParametersImplTest {

    private final int FREQUENCYRESULTLIMIT = 20;

    private final int LOWFREQUENCYRESULTLIMIT = 19;

    /**
     * DOC zshen Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.IndicatorParametersImpl#eBasicSetContainer(org.eclipse.emf.ecore.InternalEObject, int)}
     * .
     */
    @Test
    public void testEBasicSetContainerInternalEObjectInt() {
        IndicatorDefaultValueServiceUtil.getIstance().getIndicatorDVService().setFrequencyLimitResult(FREQUENCYRESULTLIMIT);
        IndicatorDefaultValueServiceUtil.getIstance().getIndicatorDVService().setLowFrequencyLimitResult(LOWFREQUENCYRESULTLIMIT);
        // freqyency value indicator
        FrequencyIndicator createFrequencyIndicator = IndicatorsFactory.eINSTANCE.createFrequencyIndicator();
        createFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createFrequencyIndicator.getParameters().getTopN());
        // low freqyency value indicator
        LowFrequencyIndicator createLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createLowFrequencyIndicator();
        createLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createLowFrequencyIndicator.getParameters().getTopN());
        // date freqyency value indicator
        DateFrequencyIndicator createDateFrequencyIndicator = IndicatorsFactory.eINSTANCE.createDateFrequencyIndicator();
        createDateFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createDateFrequencyIndicator.getParameters().getTopN());
        // date low freqyency value indicator
        DateLowFrequencyIndicator createDateLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createDateLowFrequencyIndicator();
        createDateLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createDateLowFrequencyIndicator.getParameters().getTopN());
        // week freqyency value indicator
        WeekFrequencyIndicator createWeekFrequencyIndicator = IndicatorsFactory.eINSTANCE.createWeekFrequencyIndicator();
        createWeekFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createWeekFrequencyIndicator.getParameters().getTopN());
        // week low freqyency value indicator
        WeekLowFrequencyIndicator createWeekLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createWeekLowFrequencyIndicator();
        createWeekLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createWeekLowFrequencyIndicator.getParameters().getTopN());
        // Month freqyency value indicator
        MonthFrequencyIndicator createMonthFrequencyIndicator = IndicatorsFactory.eINSTANCE.createMonthFrequencyIndicator();
        createMonthFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createMonthFrequencyIndicator.getParameters().getTopN());
        // Month low freqyency value indicator
        MonthLowFrequencyIndicator createMonthLowFrequencyIndicator = IndicatorsFactory.eINSTANCE
                .createMonthLowFrequencyIndicator();
        createMonthLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createMonthLowFrequencyIndicator.getParameters().getTopN());
        // quarter freqyency value indicator
        QuarterFrequencyIndicator createQuarterFrequencyIndicator = IndicatorsFactory.eINSTANCE.createQuarterFrequencyIndicator();
        createQuarterFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createQuarterFrequencyIndicator.getParameters().getTopN());
        // quarter low freqyency value indicator
        QuarterLowFrequencyIndicator createQuarterLowFrequencyIndicator = IndicatorsFactory.eINSTANCE
                .createQuarterLowFrequencyIndicator();
        createQuarterLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createQuarterLowFrequencyIndicator.getParameters().getTopN());
        // year freqyency value indicator
        YearFrequencyIndicator createYearFrequencyIndicator = IndicatorsFactory.eINSTANCE.createYearFrequencyIndicator();
        createYearFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createYearFrequencyIndicator.getParameters().getTopN());
        // year low freqyency value indicator
        YearLowFrequencyIndicator createYearLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createYearLowFrequencyIndicator();
        createYearLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createYearLowFrequencyIndicator.getParameters().getTopN());
        // bin freqyency value indicator
        BinFrequencyIndicator createBinFrequencyIndicator = IndicatorsFactory.eINSTANCE.createBinFrequencyIndicator();
        createBinFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createBinFrequencyIndicator.getParameters().getTopN());
        // bin low freqyency value indicator
        BinLowFrequencyIndicator createBinLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createBinLowFrequencyIndicator();
        createBinLowFrequencyIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createBinLowFrequencyIndicator.getParameters().getTopN());
        // pattern freqyency value indicator
        PatternFreqIndicator createPatternFreqIndicator = IndicatorsFactory.eINSTANCE.createPatternFreqIndicator();
        createPatternFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createPatternFreqIndicator.getParameters().getTopN());
        // pattern low freqyency value indicator
        PatternLowFreqIndicator createPatternLowFreqIndicator = IndicatorsFactory.eINSTANCE.createPatternLowFreqIndicator();
        createPatternLowFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createPatternLowFreqIndicator.getParameters().getTopN());
        // east asia freqyency value indicator
        EastAsiaPatternFreqIndicator createEastAsiaPatternFreqIndicator = IndicatorsFactory.eINSTANCE
                .createEastAsiaPatternFreqIndicator();
        createEastAsiaPatternFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createEastAsiaPatternFreqIndicator.getParameters().getTopN());
        // east asia low freqyency value indicator
        EastAsiaPatternLowFreqIndicator createEastAsiaPatternLowFreqIndicator = IndicatorsFactory.eINSTANCE
                .createEastAsiaPatternLowFreqIndicator();
        createEastAsiaPatternLowFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createEastAsiaPatternLowFreqIndicator.getParameters().getTopN());
        // soundex freqyency value indicator
        SoundexFreqIndicator createSoundexFreqIndicator = IndicatorsFactory.eINSTANCE.createSoundexFreqIndicator();
        createSoundexFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createSoundexFreqIndicator.getParameters().getTopN());
        // soundex low freqyency value indicator
        SoundexLowFreqIndicator createSoundexLowFreqIndicator = IndicatorsFactory.eINSTANCE.createSoundexLowFreqIndicator();
        createSoundexLowFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(LOWFREQUENCYRESULTLIMIT, createSoundexLowFreqIndicator.getParameters().getTopN());
        // Date pattern freqyency value indicator
        DatePatternFreqIndicator createDatePatternFreqIndicator = IndicatorsFactory.eINSTANCE.createDatePatternFreqIndicator();
        createDatePatternFreqIndicator.setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        Assert.assertEquals(FREQUENCYRESULTLIMIT, createDatePatternFreqIndicator.getParameters().getTopN());

    }

}
