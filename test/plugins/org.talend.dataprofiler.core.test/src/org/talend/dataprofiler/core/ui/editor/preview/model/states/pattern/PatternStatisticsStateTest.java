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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnSetIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class PatternStatisticsStateTest {

    private PatternStatisticsState patternStatisticsState;

    /**
     * init the state.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();

        RegexpMatchingIndicator indicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        indicator.setName("Blank text"); //$NON-NLS-1$
        indicator.setComputed(true);

        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.AllMatchIndicatorEnum, indicator));

        patternStatisticsState = new PatternStatisticsState(units);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticsState#getCustomerDataset()}
     * 
     */
    @Test
    public void testGetCustomerDataset() {
        ICustomerDataset customerDataset = patternStatisticsState.getCustomerDataset();
        ChartDataEntity[] dataEntities = customerDataset.getDataEntities();
        Assert.assertEquals(1, dataEntities.length);
        Assert.assertEquals("Blank text", dataEntities[0].getLabel()); //$NON-NLS-1$
    }
}
