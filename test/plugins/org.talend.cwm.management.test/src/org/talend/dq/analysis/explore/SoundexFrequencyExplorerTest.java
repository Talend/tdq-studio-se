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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Types;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.SoundexFreqIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.dbms.PostgresqlDbmsLanguage;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * test for class SoundexFrequencyExplorer.
 */
public class SoundexFrequencyExplorerTest {

    private static final String POSTGRESQL = "PostgreSQL"; //$NON-NLS-1$

    private SoundexFrequencyExplorer freqExp;

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
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.SoundexFrequencyExplorer#getFreqRowsStatement()} case_1:
     * for the column javaType is varchar
     */
    @Test
    public void testGetFreqRowsStatement_1() {
        Analysis analysis = UnitTestBuildHelper.createAndInitAnalysis();
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME,
                POSTGRESQL);
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION,
                "10.5"); //$NON-NLS-1$

        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(analysis.getContext().getConnection());
        assertTrue(dbmsLanguage instanceof PostgresqlDbmsLanguage);

        freqExp = new SoundexFrequencyExplorer();
        freqExp.setAnalysis(analysis);

        SoundexFreqIndicator indicator = createSoundexFreqIndicator("gender", "VARCHAR", Types.VARCHAR); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(indicator.getInstantiatedExpressions(POSTGRESQL));
        assertNotNull(dbmsLanguage.getInstantiatedExpression(indicator));

        ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, "M", "2"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("M"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());
        freqExp.setEnitty(chartDataEntity);

        String instantiatedClause = freqExp.getInstantiatedClause();
        assertEquals("SOUNDEX(\"gender\") = SOUNDEX('M')", instantiatedClause); //$NON-NLS-1$

        String FreqRowsStatement = freqExp.getFreqRowsStatement();
        assertEquals("SELECT * FROM \"TDQ_CALENDAR\" WHERE  (SOUNDEX(\"gender\") = SOUNDEX('M')) ", FreqRowsStatement); //$NON-NLS-1$
    }

    private SoundexFreqIndicator createSoundexFreqIndicator(String columnName, String tdSqlName, int javaType) {
        // create database construction
        TdColumn column = UnitTestBuildHelper.createRealTdColumn(columnName, tdSqlName, javaType);

        // create indicator
        SoundexFreqIndicator indicator = IndicatorsFactory.eINSTANCE.createSoundexFreqIndicator();
        indicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        indicator.setParameters(indicatorParameters);

        // create indicator definition
        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        indicatorDefinition.setLabel("Soundex Frequency Table"); //$NON-NLS-1$

        // create TdExpression
        TdExpression createTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        String sqlGenericExpressionBody =
                "SELECT MAX(\"gender\"), SOUNDEX(\"gender\"),  COUNT(*), COUNT(DISTINCT \"gender\") FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY SOUNDEX(\"gender\") ORDER BY COUNT(DISTINCT \"gender\") DESC , COUNT(*) DESC"; //$NON-NLS-1$
        createTdExpression.setBody(sqlGenericExpressionBody);
        createTdExpression.setLanguage(POSTGRESQL);
        indicator.getInstantiatedExpressions().add(createTdExpression);
        indicator.setIndicatorDefinition(indicatorDefinition);

        return indicator;
    }

}
