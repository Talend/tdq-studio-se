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
package org.talend.dq.analysis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class MultiColumnAnalysisExecutorTest {

    private Analysis analysis;

    private SimpleStatIndicator simpleStatIndicator;

    @Before
    public void setUp() throws Exception {
        analysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisContext context = AnalysisFactory.eINSTANCE.createAnalysisContext();
        analysis.setContext(context);

        Package catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();// mock(Catalog.class);
        catalog.setName("tbi"); //$NON-NLS-1$


        TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
        tdTable.setName("customer"); //$NON-NLS-1$
        tdTable.setNamespace(catalog);

        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn.setOwner(tdTable);
        tdColumn.setName("date_accnt_opened"); //$NON-NLS-1$
        tdColumn.setContentType(DataminingType.INTERVAL.getName());

        TdColumn tdColumn2 = RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn2.setOwner(tdTable);
        tdColumn2.setName("product_id"); //$NON-NLS-1$
        tdColumn2.setContentType(DataminingType.INTERVAL.getName());

        AnalysisParameters analysisPara = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        analysisPara.setStoreData(false);
        // set analysis type is columnset
        analysisPara.setAnalysisType(AnalysisType.COLUMN_SET);
        analysis.setParameters(analysisPara);
        context.getAnalysedElements().add(tdColumn);
        context.getAnalysedElements().add(tdColumn2);

        AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations info = AnalysisFactory.eINSTANCE.createExecutionInformations();
        analysisResult.setResultMetadata(info);

        simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();

        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression
                .setBody("SELECT <%=__COLUMN_NAMES__%> FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%>"); //$NON-NLS-1$
        expression.setLanguage("SQL"); //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        indicatorDefinition.getSqlGenericExpression().add(expression);

        simpleStatIndicator.setIndicatorDefinition(indicatorDefinition);
        simpleStatIndicator.getDateFunctions().add(""); //$NON-NLS-1$
        simpleStatIndicator.getNumericFunctions().add(""); //$NON-NLS-1$

        simpleStatIndicator.setDataminingType(DataminingType.NOMINAL);
        simpleStatIndicator.getAnalyzedColumns().add(tdColumn);
        simpleStatIndicator.getAnalyzedColumns().add(tdColumn2);

        analysisResult.getIndicators().add(simpleStatIndicator);
        analysis.setResults(analysisResult);
    }

    /**
     * test nominal type Test method for
     * {@link org.talend.dq.analysis.MultiColumnAnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)}
     * . this is test for columnset analysis
     */
    @Test
    public void testCreateSqlStatement_1() {
        simpleStatIndicator.setDataminingType(DataminingType.NOMINAL);

        MultiColumnAnalysisExecutor multiColumnAnalysisExecutor = new MultiColumnAnalysisExecutor();
        assertEquals("", multiColumnAnalysisExecutor.createSqlStatement(analysis)); //$NON-NLS-1$
        assertEquals(1, analysis.getResults().getIndicators().size());
        assertEquals("SELECT date_accnt_opened,product_id,COUNT(*) FROM tbi.customer  GROUP BY date_accnt_opened,product_id", //$NON-NLS-1$
                analysis.getResults().getIndicators().get(0).getInstantiatedExpressions("SQL").getBody()); //$NON-NLS-1$
    }

    /**
     * test interval type
     */
    @Test
    public void testCreateSqlStatement_2() {
        MultiColumnAnalysisExecutor multiColumnAnalysisExecutor = new MultiColumnAnalysisExecutor();
        // set the datamining type is interval, the result should be the same with nominal.
        simpleStatIndicator.setDataminingType(DataminingType.INTERVAL);
        assertEquals("", multiColumnAnalysisExecutor.createSqlStatement(analysis)); //$NON-NLS-1$
        assertEquals(1, analysis.getResults().getIndicators().size());
        assertEquals("SELECT date_accnt_opened,product_id,COUNT(*) FROM tbi.customer  GROUP BY date_accnt_opened,product_id", //$NON-NLS-1$
                analysis.getResults().getIndicators().get(0).getInstantiatedExpressions("SQL").getBody()); //$NON-NLS-1$

    }

    /**
     * test for leaf indicator's drill down sql , with interval type. MOD TDQ-7287 lost some columns(type!=norminal)
     * when view values in column set ana. yyin 20130514
     */
    @Test
    public void testCreateSqlStatement_3() {
        DistinctCountIndicator distinctCountIndicator = IndicatorsFactory.eINSTANCE.createDistinctCountIndicator();
        IndicatorDefinition definition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        String body = "SELECT COUNT(*) FROM (SELECT DISTINCT <%=__COLUMN_NAMES__%> FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>) A";
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(body);
        expression.setLanguage("SQL"); //$NON-NLS-1$
        definition.getSqlGenericExpression().add(expression);

        distinctCountIndicator.setIndicatorDefinition(definition);

        simpleStatIndicator.setDistinctCountIndicator(distinctCountIndicator);

        simpleStatIndicator.setDataminingType(DataminingType.NOMINAL);

        MultiColumnAnalysisExecutor multiColumnAnalysisExecutor = new MultiColumnAnalysisExecutor();
        assertEquals("", multiColumnAnalysisExecutor.createSqlStatement(analysis)); //$NON-NLS-1$
        String viewValues = "SELECT COUNT(*) FROM (SELECT DISTINCT date_accnt_opened,product_id FROM tbi.customer ) A";
        assertEquals(viewValues, simpleStatIndicator.getLeafIndicators().get(0).getInstantiatedExpressions("SQL").getBody());
    }
}
