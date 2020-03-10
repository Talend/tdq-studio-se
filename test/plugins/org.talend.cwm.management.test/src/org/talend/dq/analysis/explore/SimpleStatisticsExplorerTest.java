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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Types;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UserdefineFactory;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.dates.DateUtils;

/**
 * created by msjian on 2013-3-2 Detailled comment
 *
 */
public class SimpleStatisticsExplorerTest {

    private static final String body = "SELECT * FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.SimpleStatisticsExplorer#getQueryMap()}.
     */
    @Test
    public void testGetQueryMap() {
        Analysis ana = UnitTestBuildHelper.createAndInitAnalysis();

        TdTable table = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdTable();
        table.setName("TDQ_CALENDAR"); //$NON-NLS-1$
        TdColumn column = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdColumn();
        column.setName("CAL_DATE"); //$NON-NLS-1$
        TdSqlDataType tdsql = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdsql.setName("DATE"); //$NON-NLS-1$
        tdsql.setJavaDataType(Types.DATE);
        column.setSqlDataType(tdsql);
        table.getOwnedElement().add(column);
        column.setOwner(table);

        // create user define indicator
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        UDIndicatorDefinition indicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName("user define"); //$NON-NLS-1$
        userDefIndicator.setName(indicatorDefinition.getName());
        userDefIndicator.setIndicatorDefinition(indicatorDefinition);

        TdExpression newTdExp = BooleanExpressionHelper.createTdExpression("SQL", body, null);//$NON-NLS-1$
        newTdExp.setModificationDate(DateUtils.getCurrentDate(DateUtils.PATTERN_5));
        indicatorDefinition.getViewRowsExpression().add(newTdExp);

        ChartDataEntity chartDataEntity = new ChartDataEntity(userDefIndicator, "2012-06-05", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("2012-06-05"); //$NON-NLS-1$

        userDefIndicator.setAnalyzedElement(column);

        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(userDefIndicator);
        userDefIndicator.setAnalyzedElement(column);
        ana.setResults(createAnalysisResult);

        SimpleStatisticsExplorer simpleStatisticsExplorer = new SimpleStatisticsExplorer();
        Assert.assertTrue(simpleStatisticsExplorer.setAnalysis(ana));
        simpleStatisticsExplorer.setEnitty(chartDataEntity);

        Map<String, String> queryMap = simpleStatisticsExplorer.getQueryMap();
        assertFalse(queryMap.isEmpty());
        assertEquals(1, queryMap.size());
        assertEquals(
                "-- Analysis: anaA ;\n" + "-- Type of Analysis: Column Analysis ;\n" + "-- Purpose:  ;\n" + "-- Description:  ;\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + "-- AnalyzedElement: CAL_DATE ;\n" + "-- Indicator: user define ;\n" + "-- Showing: View rows ;\n"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + "SELECT * FROM TDQ_CALENDAR ", queryMap.get("View rows")); //$NON-NLS-1$ //$NON-NLS-2$

        // test when is not sql engine
        ana.getParameters().setExecutionLanguage(ExecutionLanguage.JAVA);
        Map<String, String> queryMap_java = simpleStatisticsExplorer.getQueryMap();
        assertFalse(queryMap_java.isEmpty());
        assertEquals(1, queryMap_java.size());
        assertEquals(null, queryMap_java.get("View rows")); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.SimpleStatisticsExplorer#getQueryMap()}.
     */
    @Test
    public void testGetQueryMap2() {
        // this case test for TDQ-18118: fix drilldown error when column name is "count"
        Analysis ana = UnitTestBuildHelper.createAndInitAnalysis();
        TaggedValueHelper
                .setTaggedValue(ana.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "PostgreSQL"); //$NON-NLS-1$
        TaggedValueHelper
                .setTaggedValue(ana.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "12.1"); //$NON-NLS-1$

        TdColumn column = UnitTestBuildHelper.createRealTdColumn("count", "INT4", Types.INTEGER); //$NON-NLS-1$ //$NON-NLS-2$

        // create unique Count Indicator
        Indicator uniqueCountIndicator = IndicatorsFactory.eINSTANCE.createUniqueCountIndicator();
        uniqueCountIndicator.setName("Unique Count"); //$NON-NLS-1$

        // create indicator definition
        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        indicatorDefinition.setLabel("Unique Count"); //$NON-NLS-1$

        // create TdExpression
        TdExpression createTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        String sqlGenericExpressionBody =
                "SELECT COUNT(*) FROM (SELECT \"count\", COUNT(*) FROM TDQ_CALENDAR GROUP BY \"count\" HAVING mycount = 1) AS myquery"; //$NON-NLS-1$
        createTdExpression.setBody(sqlGenericExpressionBody);
        createTdExpression.setLanguage("PostgreSQL"); //$NON-NLS-1$
        uniqueCountIndicator.getInstantiatedExpressions().add(createTdExpression);
        uniqueCountIndicator.setIndicatorDefinition(indicatorDefinition);

        ChartDataEntity chartDataEntity = new ChartDataEntity(uniqueCountIndicator, "Unique Count", "2"); //$NON-NLS-1$ //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);

        uniqueCountIndicator.setAnalyzedElement(column);

        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(uniqueCountIndicator);
        ana.setResults(createAnalysisResult);

        SimpleStatisticsExplorer simpleStatisticsExplorer = new SimpleStatisticsExplorer();
        Assert.assertTrue(simpleStatisticsExplorer.setAnalysis(ana));
        simpleStatisticsExplorer.setEnitty(chartDataEntity);

        Map<String, String> queryMap = simpleStatisticsExplorer.getQueryMap();
        assertFalse(queryMap.isEmpty());
        assertEquals(2, queryMap.size());
        assertTrue(queryMap.get("View rows").contains("COUNT(*) as mycount FROM")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(queryMap.get("View values").contains("COUNT(*) as mycount FROM")); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
