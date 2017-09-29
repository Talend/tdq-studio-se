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

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * created by talend on Nov 25, 2014 Detailled comment
 * 
 */
public class FunctionFrequencyStatExplorerTest {

    /**
     * DOC talend Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC talend Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC talend Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FunctionFrequencyStatExplorer#getFreqRowsStatement()}.
     */
    @Test
    public void testGetFreqRowsStatement() {
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.FunctionFrequencyStatExplorer#getInstantiatedClause()}.
     */
    @Test
    public void testGetInstantiatedClause() {
        FunctionFrequencyStatExplorer explorer = new FunctionFrequencyStatExplorer();
        // init explorer instance
        Indicator patternFreqIndicator = IndicatorsFactory.eINSTANCE.createPatternFreqIndicator();
        ChartDataEntity entity = new ChartDataEntity(patternFreqIndicator, "entityName", "entityValue");
        entity.setKey("entityKey");
        Expression tdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression.setLanguage("MySQL"); //$NON-NLS-1$
        tdExpression
                .setBody("SELECT REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`name`,'a','a'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'ç','a'),'â','a'),'ê','a'),'î','a'),'ô','a'),'û','a'),'é','a'),'è','a'),'ù','a'),'ï','a'),'ö','a'),'ü','a'),'A','A'),'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'Ç','A'),'Â','A'),'Ê','A'),'Î','A'),'Ô','A'),'Û','A'),'É','A'),'È','A'),'Ù','A'),'Ï','A'),'Ö','A'),'Ü','A'),'0','9'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'9','9'), COUNT(*) c FROM `tbi`.`generatetable` t  GROUP BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`name`,'a','a'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'ç','a'),'â','a'),'ê','a'),'î','a'),'ô','a'),'û','a'),'é','a'),'è','a'),'ù','a'),'ï','a'),'ö','a'),'ü','a'),'A','A'),'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'Ç','A'),'Â','A'),'Ê','A'),'Î','A'),'Ô','A'),'Û','A'),'É','A'),'È','A'),'Ù','A'),'Ï','A'),'Ö','A'),'Ü','A'),'0','9'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'9','9') ORDER BY c ASC LIMIT 10");
        patternFreqIndicator.setInstantiatedExpression(tdExpression);
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        column1.setName("columnName"); //$NON-NLS-1$
        TdSqlDataType tdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdSqlDataType.setJavaDataType(Types.DATE);
        column1.setSqlDataType(tdSqlDataType);
        patternFreqIndicator.setAnalyzedElement(column1);
        Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisContext analysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        analysis.setContext(analysisContext);
        DatabaseConnection databaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        analysisContext.setConnection(databaseConnection);
        TaggedValueHelper.setTaggedValue(databaseConnection, TaggedValueHelper.DB_PRODUCT_NAME, "MySQL"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(databaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "5.0.22-community-nt"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(databaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "'"); //$NON-NLS-1$
        explorer.setAnalysis(analysis);
        explorer.setEnitty(entity);

        String instantiatedClause = explorer.getInstantiatedClause();
        Assert.assertEquals(
                "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`name`,'a','a'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'ç','a'),'â','a'),'ê','a'),'î','a'),'ô','a'),'û','a'),'é','a'),'è','a'),'ù','a'),'ï','a'),'ö','a'),'ü','a'),'A','A'),'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'Ç','A'),'Â','A'),'Ê','A'),'Î','A'),'Ô','A'),'Û','A'),'É','A'),'È','A'),'Ù','A'),'Ï','A'),'Ö','A'),'Ü','A'),'0','9'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'9','9') = 'entityKey'",
                instantiatedClause);
    }

}
