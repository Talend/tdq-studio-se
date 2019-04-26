// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis.explore;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.SumIndicator;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

import junit.framework.Assert;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class SummaryStastictisExplorerTest {

    private SummaryStastictisExplorer sumExp;

    private SumIndicator indicator;

    private Analysis analysis;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }
        sumExp = new SummaryStastictisExplorer();

        indicator = IndicatorsFactory.eINSTANCE.createSumIndicator();
        TdColumn column = UnitTestBuildHelper.createRealTdColumn("INTERVAL_MONTH", "INTERVAL_MONTH", Types.VARCHAR);
        indicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        indicator.setParameters(indicatorParameters);

        analysis = UnitTestBuildHelper.createAndInitAnalysis();
        TaggedValueHelper
                .setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Teradata");
        TaggedValueHelper
                .setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        sumExp.setAnalysis(analysis);

        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "1", "1"); //$NON-NLS-1$ //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("1"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        sumExp.setEnitty(chartDataEntity);
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.explore.SummaryStastictisExplorer#getAnalyzedElementName(org.talend.dataquality.indicators.Indicator)}
     * . test for teradata interval_xx type
     */
    @Test
    public void testGetAnalyzedElementName() {
        String intervalColumn = sumExp.getAnalyzedElementName(indicator);
        Assert.assertEquals("cast(INTERVAL_MONTH AS REAL)", intervalColumn);
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.explore.SummaryStastictisExplorer#getAnalyzedElementName(org.talend.dataquality.indicators.Indicator)}
     * . test for normal type, not teradata interval_xx type
     */
    @Test
    public void testGetAnalyzedElementName_2() {
        TaggedValueHelper
                .setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "MySql");
        TaggedValueHelper
                .setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "5.0");
        sumExp.setAnalysis(analysis);
        String intervalColumn = this.sumExp.getAnalyzedElementName(indicator);
        Assert.assertEquals("`INTERVAL_MONTH`", intervalColumn);
    }

}
