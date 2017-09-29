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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.YearLowFrequencyIndicator;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DateFrequencyStateUtilTest {

    HashMap<Object, Long> mapVal2Freq = null;

    YearLowFrequencyIndicator yearLowFrequencyIndicator = null;

    List<IndicatorUnit> units = null;

    @Before
    public void setUp() throws Exception {
        mapVal2Freq = new HashMap<Object, Long>();
        mapVal2Freq.put(1993, 4L);
        mapVal2Freq.put(1994, 23L);
        mapVal2Freq.put(1996, 15L);
        mapVal2Freq.put(1999, 11L);
        yearLowFrequencyIndicator = IndicatorsFactory.eINSTANCE.createYearLowFrequencyIndicator();
        yearLowFrequencyIndicator.setValueToFreq(mapVal2Freq);
        yearLowFrequencyIndicator.setComputed(true);

        units = new ArrayList<IndicatorUnit>();
        ModelElementIndicator modelElementIndicator = UnitTestBuildHelper.createModelElementIndicator();
        ColumnIndicatorUnit columnIndicatorUnit = new ColumnIndicatorUnit(IndicatorEnum.YearLowFrequencyIndicatorEnum,
                yearLowFrequencyIndicator, modelElementIndicator);
        units.add(columnIndicatorUnit);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.DateFrequencyStateUtil#getDataEntity(java.util.List, int)}
     * .
     */
    @Test
    public void testGetDataEntity_low_year_freq() {

        ChartDataEntity[] dataEntity = DateFrequencyStateUtil
                .getDataEntity(units, ComparatorsFactory.LOW_FREQUENCY_COMPARATOR_ID);
        Assert.assertTrue(dataEntity.length == 4);

        Assert.assertTrue(4d == Double.parseDouble(dataEntity[0].getValue()));
        Assert.assertTrue(11d == Double.parseDouble(dataEntity[1].getValue()));
        Assert.assertTrue(15d == Double.parseDouble(dataEntity[2].getValue()));
        Assert.assertTrue(23d == Double.parseDouble(dataEntity[3].getValue()));

    }

    @Test
    public void testGetDataEntity_year_freq() {

        ChartDataEntity[] dataEntity = DateFrequencyStateUtil.getDataEntity(units, ComparatorsFactory.FREQUENCY_COMPARATOR_ID);
        Assert.assertTrue(dataEntity.length == 4);
        Double.parseDouble(dataEntity[0].getValue());
        Assert.assertTrue(23d == Double.parseDouble(dataEntity[0].getValue()));
        Assert.assertTrue(15d == Double.parseDouble(dataEntity[1].getValue()));
        Assert.assertTrue(11d == Double.parseDouble(dataEntity[2].getValue()));
        Assert.assertTrue(4d == Double.parseDouble(dataEntity[3].getValue()));

    }

}
