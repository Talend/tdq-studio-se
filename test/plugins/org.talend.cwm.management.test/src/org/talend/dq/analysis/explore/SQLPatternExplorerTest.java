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

import java.sql.Types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class SQLPatternExplorerTest {

    private static final String RES_INVALIED_ROWS = "SELECT *  FROM tbi.customer  WHERE (lname NOT  LIKE 'su%' OR lname IS NULL )"; //$NON-NLS-1$

    private static final String RES_VALIED_ROWS = "SELECT *  FROM tbi.customer  WHERE lname LIKE 'su%'"; //$NON-NLS-1$

    private static final String RES_INVALIED_VALUES = "SELECT lname FROM tbi.customer  WHERE (lname NOT  LIKE 'su%' OR lname IS NULL )"; //$NON-NLS-1$

    private static final String RES_VALIED_VALUES = "SELECT lname FROM tbi.customer  WHERE lname LIKE 'su%'"; //$NON-NLS-1$

    private SQLPatternExplorer sqlPatternExplorer;

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

        sqlPatternExplorer = new SQLPatternExplorer();

        // mock setEntity
        SqlPatternMatchingIndicator indicator = creatSqlPatternMatchingIndicator();

        Analysis ana = UnitTestBuildHelper.createAndInitAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(indicator);
        ana.setResults(createAnalysisResult);

        sqlPatternExplorer.setAnalysis(ana);
        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "name1", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("name1"); //$NON-NLS-1$

        sqlPatternExplorer.setEnitty(chartDataEntity);

        IndicatorDefinition definition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody("SELECT * FROM tbi.customer ");
        expression.setLanguage("SQL"); //$NON-NLS-1$
        definition.getSqlGenericExpression().add(expression);
        indicator.setIndicatorDefinition(definition);
        indicator.getInstantiatedExpressions().add(expression);
    }

    private SqlPatternMatchingIndicator creatSqlPatternMatchingIndicator() {
        // create database construction
        TdColumn column = UnitTestBuildHelper.createRealTdColumn("lname", "lname", Types.VARCHAR);

        // create indicator
        SqlPatternMatchingIndicator indicator = IndicatorsFactory.eINSTANCE.createSqlPatternMatchingIndicator();
        indicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        indicator.setParameters(indicatorParameters);

        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName("My Pattern"); //$NON-NLS-1$
        RegularExpression regularExpr = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody("'su%'");
        expression.setLanguage("SQL"); //$NON-NLS-1$
        regularExpr.setExpression(expression);
        pattern.getComponents().add(regularExpr);
        // create Domain
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        indicatorParameters.setDataValidDomain(createDomain);
        indicatorParameters.getDataValidDomain().getPatterns().add(pattern);

        return indicator;
    }

    /**
     * Mock Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidRowsStatement()}.
     */
    @Test
    public void testGetInvalidRowsStatement() {
        String strStatement = sqlPatternExplorer.getInvalidRowsStatement();
        Assert.assertEquals(RES_INVALIED_ROWS, strStatement);
    }

    /**
     * mock test method
     */
    @Test
    public void testGetValidRowsStatement() {
        String strStatement = sqlPatternExplorer.getValidRowsStatement();
        Assert.assertEquals(RES_VALIED_ROWS, strStatement);
    }

    /**
     * Mock Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidValuesStatement()}.
     */
    @Test
    public void testGetInvalidValuesStatement() {
        String strStatement = sqlPatternExplorer.getInvalidValuesStatement();
        Assert.assertEquals(RES_INVALIED_VALUES, strStatement);
    }

    /**
     * Mock Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getValidValuesStatement()}.
     */
    @Test
    public void testGetValidValuesStatement() {
        String strStatement = sqlPatternExplorer.getValidValuesStatement();
        Assert.assertEquals(RES_VALIED_VALUES, strStatement);
    }

}
