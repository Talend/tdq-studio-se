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
package org.talend.dq.analysis.explore;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.LowFrequencyIndicator;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UserdefineFactory;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.dates.DateUtils;

/**
 * test for class FrequencyStatisticsExplorer.
 */
public class FrequencyStatisticsExplorerTest {

    private FrequencyStatisticsExplorer freqExp;

    private Analysis ana;

    DbmsLanguage dbLanguage = DbmsLanguageFactory.createDbmsLanguage("MySQL", "5.0.2"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * DOC msjian Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }
        ana = UnitTestBuildHelper.createAndInitAnalysis();
    }

    /**
     * DOC msjian Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FrequencyStatisticsExplorer#getFreqRowsStatement()} case_1:
     * for the column javaType is Date
     */
    @Test
    public void testGetFreqRowsStatement_1() {

        LowFrequencyIndicator indicator = creatFrenquceIndicator("CAL_DATE", "DATE", Types.DATE);

        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "2012-06-05", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("2012-06-05"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        freqExp = new FrequencyStatisticsExplorer();
        freqExp.setAnalysis(ana);
        freqExp.setEnitty(chartDataEntity);

        String clause = freqExp.getFreqRowsStatement();

        assertEquals("SELECT * FROM TDQ_CALENDAR WHERE  (CAL_DATE = '2012-06-05') ", clause); //$NON-NLS-1$
    }

    /**
     * DOC yyin Comment method "creatFrenquceIndicator".
     * 
     * @return
     */
    private LowFrequencyIndicator creatFrenquceIndicator(String columnName, String tdSqlName, int javaType) {
        // create database construction
        TdColumn column = UnitTestBuildHelper.createRealTdColumn(columnName, tdSqlName, javaType);

        // create indicator
        LowFrequencyIndicator indicator = IndicatorsFactory.eINSTANCE.createLowFrequencyIndicator();
        indicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        indicator.setParameters(indicatorParameters);
        return indicator;
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FrequencyStatisticsExplorer#getFreqRowsStatement()} case_2:
     * for the column javaType is Text
     */
    @Test
    public void testGetFreqRowsStatement_2() {
        LowFrequencyIndicator indicator = creatFrenquceIndicator("CAL_TEXT", "TEXT", Types.LONGNVARCHAR);

        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "it is a long text", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("it is a long text"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        freqExp = new FrequencyStatisticsExplorer();
        freqExp.setAnalysis(ana);
        freqExp.setEnitty(chartDataEntity);

        String clause = freqExp.getFreqRowsStatement();

        assertEquals("SELECT * FROM TDQ_CALENDAR WHERE  (CAL_TEXT = 'it is a long text') ", clause); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FrequencyStatisticsExplorer#getFreqRowsStatement()} case_3:
     * for for the column javaType is Number
     */
    @Test
    public void testGetFreqRowsStatement_3() {
        LowFrequencyIndicator indicator = creatFrenquceIndicator("CAL_NUMBER", "NUMBER", Types.BIGINT);

        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "12345", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("12345"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        freqExp = new FrequencyStatisticsExplorer();
        freqExp.setAnalysis(ana);
        freqExp.setEnitty(chartDataEntity);

        String clause = freqExp.getFreqRowsStatement();

        assertEquals("SELECT * FROM TDQ_CALENDAR WHERE  (CAL_NUMBER = '12345') ", clause); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FrequencyStatisticsExplorer#getFreqRowsStatement()} case_3:
     * for UDIndicatorDefinition
     */
    @Test
    public void testGetFreqRowsStatement_4() {
        // create database construction
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

        // create indicator
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        UDIndicatorDefinition indicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName("user define count"); //$NON-NLS-1$
        userDefIndicator.setName(indicatorDefinition.getName());
        userDefIndicator.setIndicatorDefinition(indicatorDefinition);

        TdExpression newTdExp = BooleanExpressionHelper.createTdExpression("SQL", //$NON-NLS-1$
                "SELECT * FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>", null); //$NON-NLS-1$
        newTdExp.setModificationDate(DateUtils.getCurrentDate(DateUtils.PATTERN_5));
        indicatorDefinition.getViewRowsExpression().add(newTdExp);

        ChartDataEntity chartDataEntity = new ChartDataEntity(userDefIndicator, "2012-06-05", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("2012-06-05"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        userDefIndicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        userDefIndicator.setParameters(indicatorParameters);
        assertNull(indicatorParameters.getDateParameters());

        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(userDefIndicator);
        userDefIndicator.setAnalyzedElement(column);
        ana.setResults(createAnalysisResult);

        freqExp = new FrequencyStatisticsExplorer();
        freqExp.setAnalysis(ana);
        freqExp.setEnitty(chartDataEntity);

        String clause = freqExp.getFreqRowsStatement();

        assertEquals("SELECT * FROM TDQ_CALENDAR ", clause); //$NON-NLS-1$
    }
}
