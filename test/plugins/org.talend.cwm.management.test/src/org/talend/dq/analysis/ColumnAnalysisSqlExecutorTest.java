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
package org.talend.dq.analysis;

import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.UnitTestBuildHelper;

import junit.framework.Assert;
import orgomg.cwm.resource.relational.Catalog;

/**
 * created by zshen on Mar 6, 2014 Detailled comment
 *
 */
public class ColumnAnalysisSqlExecutorTest {

    Analysis testAnalysis = null;

    FrequencyIndicator testFrequencyIndicator = null;

    IndicatorDefinition testIndicatorDefinition = null;

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
        // Init TDQ structor
        UnitTestBuildHelper.initProjectStructure();
        // create analysis element
        TdColumn testTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        testTdColumn.setName("columnName"); //$NON-NLS-1$
        // testAnalysisResult.setModelElement(testTdColumn);

        // create sqlDataType
        TdSqlDataType testTdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        testTdColumn.setSqlDataType(testTdSqlDataType);
        testTdSqlDataType.setJavaDataType(DataminingType.NOMINAL_VALUE);

        // create TdTable
        TdTable testTdTable = RelationalFactory.eINSTANCE.createTdTable();
        testTdColumn.setNamespace(testTdTable);
        testTdTable.setName("generateTable"); //$NON-NLS-1$

        // create connection
        DatabaseConnection testDatabaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper
                .setTaggedValue(testDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                        SupportDBUrlType.MYSQLDEFAULTURL.getDBKey());
        TaggedValueHelper.setTaggedValue(testDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "5.0"); //$NON-NLS-1$
        // create catalog
        Catalog testCatalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        testCatalog.setName("TBI"); //$NON-NLS-1$
        testTdTable.setNamespace(testCatalog);
        testCatalog.setNamespace(testDatabaseConnection);
        testCatalog.getDataManager().add(testDatabaseConnection);

        // create analysis
        testAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();

        // create Analaysis result
        AnalysisResult testAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        testAnalysis.setResults(testAnalysisResult);

        // create analysis parameter
        AnalysisParameters testAnalysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        testAnalysis.setParameters(testAnalysisParameters);

        // create analysis Content
        AnalysisContext testAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        testAnalysis.setContext(testAnalysisContext);
        testAnalysisContext.setConnection(testDatabaseConnection);

        // create row count indicatorDefinition
        testIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        testIndicatorDefinition.setLabel("Pattern Frequency Table"); //$NON-NLS-1$

        // create row count indicator
        testFrequencyIndicator = IndicatorsFactory.eINSTANCE.createPatternFreqIndicator();
        testAnalysisResult.getIndicators().add(testFrequencyIndicator);
        testFrequencyIndicator.setAnalyzedElement(testTdColumn);
        testFrequencyIndicator.setIndicatorDefinition(testIndicatorDefinition);
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
     * {@link org.talend.dq.analysis.ColumnAnalysisSqlExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)}
     * . case 1:Pattern Frequency Indicator normal case
     */
    @Test
    public void testCreateSqlStatementCase1() {
        String expectResult =
                "SELECT BINARY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`columnName`,'a','a'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'ç','a'),'â','a'),'ê','a'),'î','a'),'ô','a'),'û','a'),'é','a'),'è','a'),'ù','a'),'ï','a'),'ö','a'),'ü','a'),'A','A'),'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'Ç','A'),'Â','A'),'Ê','A'),'Î','A'),'Ô','A'),'Û','A'),'É','A'),'È','A'),'Ù','A'),'Ï','A'),'Ö','A'),'Ü','A'),'0','9'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'9','9'), COUNT(*) c FROM `TBI`.`generateTable` t  GROUP BY BINARY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`columnName`,'a','a'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'ç','a'),'â','a'),'ê','a'),'î','a'),'ô','a'),'û','a'),'é','a'),'è','a'),'ù','a'),'ï','a'),'ö','a'),'ü','a'),'A','A'),'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'Ç','A'),'Â','A'),'Ê','A'),'Î','A'),'Ô','A'),'Û','A'),'É','A'),'È','A'),'Ù','A'),'Ï','A'),'Ö','A'),'Ü','A'),'0','9'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'9','9') ORDER BY c DESC LIMIT 10"; //$NON-NLS-1$

        ColumnAnalysisSqlExecutor columnAnalysisSqlExecutor = new ColumnAnalysisSqlExecutor();
        String actualSqlStatement = columnAnalysisSqlExecutor.createSqlStatement(testAnalysis);
        Assert.assertEquals(StringUtils.EMPTY, actualSqlStatement);
        Assert.assertNotNull(testFrequencyIndicator.getInstantiatedExpressions());
        Assert.assertEquals(expectResult, testFrequencyIndicator.getInstantiatedExpressions().get(0).getBody());
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ColumnAnalysisSqlExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)}
     * . case 2:Pattern Frequency Indicator no expression case so that return value should be default one
     */
    @Test
    public void testCreateSqlStatementCase2() {
        String expectResult =
                "SELECT REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`columnName`,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9'), COUNT(*) c FROM `TBI`.`generateTable` t  GROUP BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`columnName`,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9') ORDER BY c DESC LIMIT 10"; //$NON-NLS-1$
        // keep lable attribute be empty so that will not update indicatorDefinition by lable again
        testIndicatorDefinition.setLabel(StringUtils.EMPTY);
        String sqlGenericExpressionBody =
                "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c DESC"; //$NON-NLS-1$
        // create TdExpression
        TdExpression createTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createTdExpression.setBody(sqlGenericExpressionBody);
        createTdExpression.setLanguage(SupportDBUrlType.MYSQLDEFAULTURL.getDBKey());

        testIndicatorDefinition.getSqlGenericExpression().add(createTdExpression);
        ColumnAnalysisSqlExecutor columnAnalysisSqlExecutor = new ColumnAnalysisSqlExecutor();
        String actualSqlStatement = columnAnalysisSqlExecutor.createSqlStatement(testAnalysis);
        Assert.assertEquals(StringUtils.EMPTY, actualSqlStatement);
        Assert.assertNotNull(testFrequencyIndicator.getInstantiatedExpressions());
        Assert.assertEquals(expectResult, testFrequencyIndicator.getInstantiatedExpressions().get(0).getBody());
    }

    @SuppressWarnings({ "deprecation", "nls" })
    @Test
    public void testGetFinalDefaultValue() {
        ColumnAnalysisSqlExecutor columnAnalysisSqlExecutor = new ColumnAnalysisSqlExecutor();
        // case1 TDQ-1554 add single quotation '' for mysql date type
        String finalDefaultValue =
                columnAnalysisSqlExecutor
                        .getFinalDefaultValue(Types.DATE, "MySQL", "`tbi`.`defalutvalueTest`", "CURRENT_TIMESTAMP");
        Assert.assertEquals("\'" + "CURRENT_TIMESTAMP" + "\'", finalDefaultValue);

        // case2 TDQ-10783: varchar type but with number content, we should add single quotation ''
        finalDefaultValue = columnAnalysisSqlExecutor
                .getFinalDefaultValue(Types.VARCHAR, "MySQL", "`tbi`.`defalutvalueTest`", "12");
        Assert.assertEquals("\'12\'", finalDefaultValue);

        // case3 INTEGER type still return a number
        finalDefaultValue =
                columnAnalysisSqlExecutor.getFinalDefaultValue(Types.INTEGER, "MySQL", "`tbi`.`defalutvalueTest`", "1");
        Assert.assertEquals("1", finalDefaultValue);

        // case4 varchar type with a string
        finalDefaultValue = columnAnalysisSqlExecutor
                .getFinalDefaultValue(Types.VARCHAR, "MySQL", "`tbi`.`defalutvalueTest`", "literal");
        Assert.assertEquals("\'literal\'", finalDefaultValue);

        // case5 varchar type with a string with ''
        finalDefaultValue = columnAnalysisSqlExecutor
                .getFinalDefaultValue(Types.VARCHAR, "MySQL", "`tbi`.`defalutvalueTest`", "\'literal\'");
        Assert.assertEquals("\'literal\'", finalDefaultValue);

        // case6 Snowflake varchar type with a string
        finalDefaultValue = columnAnalysisSqlExecutor
                .getFinalDefaultValue(Types.VARCHAR, "Snowflake", "`tbi`.`defalutvalueTest`", "literal");
        Assert.assertEquals("\'literal\'", finalDefaultValue);

        // case7 Snowflake varchar type with a string with ''
        finalDefaultValue = columnAnalysisSqlExecutor
                .getFinalDefaultValue(Types.VARCHAR, "Snowflake", "`tbi`.`defalutvalueTest`", "\'literal\'");
        Assert.assertEquals("\'literal\'", finalDefaultValue);
    }

}
