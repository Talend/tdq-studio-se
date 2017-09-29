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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NumberUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class BenfordLawFrequencyStateTest {

    private BenfordLawFrequencyState benState;

    /**
     * init the state.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();

        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        BenfordLawFrequencyIndicator benfordLawFrequencyIndicator = IndicatorsFactory.eINSTANCE
                .createBenfordLawFrequencyIndicator();
        List<IRepositoryViewObject> all = ProxyRepositoryFactory.getInstance().getAll(
                ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION);
        Assert.assertEquals(1, all.size());
        IRepositoryViewObject iRepositoryViewObject = all.get(0);
        Assert.assertNotNull(iRepositoryViewObject);
        TDQIndicatorDefinitionItem benfordItem = (TDQIndicatorDefinitionItem) iRepositoryViewObject.getProperty().getItem();
        benfordLawFrequencyIndicator.setIndicatorDefinition(benfordItem.getIndicatorDefinition());
        ModelElementIndicator modelElementIndicator = UnitTestBuildHelper.createModelElementIndicator();
        ColumnIndicatorUnit columnIndicatorUnit = new ColumnIndicatorUnit(IndicatorEnum.BenfordLawFrequencyIndicatorEnum,
                benfordLawFrequencyIndicator, modelElementIndicator);

        // IndicatorUnit unit = mock(IndicatorUnit.class);
        units.add(columnIndicatorUnit);
        for (int i = 0; i < 10; i++) {
            benfordLawFrequencyIndicator.handle(i);
        }
        benState = new BenfordLawFrequencyState(units);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BenfordLawFrequencyState#setValueToDataset(org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultCategoryDataset, org.talend.dq.indicators.ext.FrequencyExt, java.lang.String)}
     * .
     * 
     */
    @Test
    public void testSetValueToDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        FrequencyExt fre = new FrequencyExt();
        fre.setKey(2);
        fre.setValue(3l);
        fre.setFrequency(0.33);
        benState.setValueToDataset(customerdataset, fre, "2"); //$NON-NLS-1$
        Number n = TOPChartUtils.getInstance().getValue(customerdataset.getDataset(), 0, 0);
        Assert.assertEquals(0.33, n);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BenfordLawFrequencyState#sortIndicator(org.talend.dq.indicators.ext.FrequencyExt[])}
     * . just test normal cases: contains 1~9
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testSortIndicator() {
        FrequencyExt[] frequencyExt = new FrequencyExt[9];
        Long value = 100l;
        for (int i = 0; i < 9; i++) {
            frequencyExt[i] = new FrequencyExt();
            frequencyExt[i].setKey(i + 1);
            frequencyExt[i].setValue(value);

            if (i > 4) {
                value = value * 2;
            } else {
                value = value / 2;
            }
        }
        benState.sortIndicator(frequencyExt);
        for (int i = 0; i < 8; i++) {
            // Assert.assertTrue(frequencyExt[i].getValue() > frequencyExt[i + 1].getValue());
            Assert.assertTrue(NumberUtils.stringToInt(frequencyExt[i].getKey().toString()) < NumberUtils
                    .stringToInt(frequencyExt[i + 1].getKey().toString()));
        }
    }

    /**
     * test for : when there are some missing numbers, it should be added
     * 
     * @Test public void testSortIndicator_2() { FrequencyExt[] freExt = new FrequencyExt[7]; Long value = 100l; for
     * (int i = 0; i < 7; i++) { freExt[i] = new FrequencyExt(); freExt[i].setKey(i + 1); freExt[i].setValue(value);
     * 
     * if (i > 4) { value = value * 2; } else { value = value / 2; } } benState.sortIndicator(freExt);
     * 
     * Assert.assertEquals(9, freExt.length); Assert.assertTrue(0L == freExt[7].getValue()); Assert.assertTrue(0L ==
     * freExt[8].getValue()); }
     */

}
