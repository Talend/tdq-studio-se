// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.AverageLengthIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by qiongli on Sep 5, 2012 Detailled comment
 *
 */
public class TextStatisticsExplorerTest {

    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }

    }

    /**
     * Test method for Text indicators with Hive connection,should don't have any menu items.
     */
    @Test
    public void testGetQueryMap() {
        AverageLengthIndicator averageLengthIndicator = IndicatorsFactory.eINSTANCE.createAverageLengthIndicator();
        TdColumn column = UnitTestBuildHelper.createRealTdColumn("NAME", "NAME", Types.VARCHAR);
        averageLengthIndicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        averageLengthIndicator.setParameters(indicatorParameters);
        IndicatorDefinition  averageLengthIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        averageLengthIndicatorDef.setName("Average Length");
        averageLengthIndicator.setIndicatorDefinition(averageLengthIndicatorDef);


        Analysis analysis = UnitTestBuildHelper.createAndInitAnalysis();
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Teradata");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");

        ChartDataEntity chartDataEntity = new ChartDataEntity(averageLengthIndicator, "1", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("1"); //$NON-NLS-1$

        TextStatisticsExplorer textStatisticsExplorer = new TextStatisticsExplorer();
        textStatisticsExplorer.setAnalysis(analysis);
        textStatisticsExplorer.setEnitty(chartDataEntity);

        Map<String, String> queryMap = textStatisticsExplorer.getQueryMap();
        assertFalse(queryMap.isEmpty());
        assertEquals(
                "-- Analysis: anaA ;\n"
                        + "-- Type of Analysis: Column Analysis ;\n"
                        + "-- Purpose:  ;\n"
                        + "-- Description:  ;\n"
                        + "-- AnalyzedElement: NAME ;\n"
                        + "-- Indicator: Average Length ;\n"
                        + "-- Showing: View rows ;\n"
                        + "SELECT t.* FROM(SELECT CAST(SUM(CHARACTER_LENGTH(NAME)) / (COUNT(NAME)*1.00)+0.99 as int) c, CAST(SUM(CHARACTER_LENGTH(NAME)) / (COUNT(NAME)*1.00) as int) f FROM TDQ_CALENDAR) e, TDQ_CALENDAR t where character_length(NAME) between f and c",
                queryMap.get("View rows"));
    }

}
