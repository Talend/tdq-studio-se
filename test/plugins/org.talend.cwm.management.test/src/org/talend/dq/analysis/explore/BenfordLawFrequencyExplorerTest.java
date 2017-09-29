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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class BenfordLawFrequencyExplorerTest {

    private BenfordLawFrequencyExplorer benExp;

    private BenfordLawFrequencyIndicator benfordIndicator;

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
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }

        benExp = new BenfordLawFrequencyExplorer();

        benfordIndicator = IndicatorsFactory.eINSTANCE.createBenfordLawFrequencyIndicator();
        TdColumn column = UnitTestBuildHelper.createRealTdColumn("firstName", "TEXT", Types.VARCHAR);
        benfordIndicator.setAnalyzedElement(column);
        IndicatorParameters indicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicatorParameters.setDateParameters(null);
        benfordIndicator.setParameters(indicatorParameters);

        analysis = UnitTestBuildHelper.createAndInitAnalysis();
        benExp.setAnalysis(analysis);

        ChartDataEntity chartDataEntity = new ChartDataEntity(benfordIndicator, "1", "1"); //$NON-NLS-1$  //$NON-NLS-2$
        chartDataEntity.setLabelNull(false);
        chartDataEntity.setKey("1"); //$NON-NLS-1$
        assertFalse(chartDataEntity.isLabelNull());

        benExp.setEnitty(chartDataEntity);

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
     * Test method for {@link org.talend.dq.analysis.explore.BenfordLawFrequencyExplorer#getInstantiatedClause()}.
     * normal case: for default DB
     */
    @Test
    public void testGetInstantiatedClause_1() {
        // when(mockDbLanguage.getDbmsName()).thenReturn("SQL");
        // when(mockDbLanguage.castColumnNameToChar(anyString())).thenReturn("firstName");

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("firstName LIKE '1%'", clause);
    }

    /**
     * test for Teradata db type
     */
    @Test
    public void testGetInstantiatedClause_2() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Teradata");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("cast(firstName as char) LIKE '1%'", clause);
    }

    /**
     * test for postgrsql db type
     */
    @Test
    public void testGetInstantiatedClause_3() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "PostgreSQL");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("cast(firstName as char) LIKE '1%'", clause);
    }

    /**
     * test for sybase db type
     */
    @Test
    public void testGetInstantiatedClause_4() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME,
                "Adaptive Server Enterprise | Sybase Adaptive Server IQ");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("convert(char(15),firstName) LIKE '1%'", clause);
    }

    /**
     * test for Oracle
     */
    @Test
    public void testGetInstantiatedClause_5() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Oracle");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("firstName LIKE '1%'", clause);
    }

    /**
     * test for Informix
     */
    @Test
    public void testGetInstantiatedClause_6() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Informix");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals(" SUBSTR(firstName,0,1) LIKE '1%'", clause);
    }

    /**
     * test for Vertica
     */
    @Test
    public void testGetInstantiatedClauseVertica() {
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_NAME, "Vertica");
        TaggedValueHelper.setTaggedValue(analysis.getContext().getConnection(), TaggedValueHelper.DB_PRODUCT_VERSION, "1");
        benExp.setAnalysis(analysis);

        String clause = benExp.getInstantiatedClause();
        Assert.assertEquals("to_char(firstName) LIKE '1%'", clause);
    }
}
