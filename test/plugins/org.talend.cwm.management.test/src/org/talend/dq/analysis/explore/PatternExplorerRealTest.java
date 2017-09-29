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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.impl.RelationalFactoryImpl;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.impl.AnalysisFactoryImpl;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.impl.DomainFactoryImpl;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.pattern.impl.PatternFactoryImpl;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.impl.UserdefineFactoryImpl;
import org.talend.dataquality.indicators.impl.IndicatorsFactoryImpl;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.impl.CoreFactoryImpl;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC talend class global comment. Detailled comment
 */
public class PatternExplorerRealTest {

    private static final String ViewValidValues = "SELECT <%=__COLUMN_NAMES__%> FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String ViewInvalidValues = "SELECT <%=__COLUMN_NAMES__%> FROM <%=__TABLE_NAME__%> WHERE NOT (id>=1) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String ViewValidRows = "SELECT * FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String ViewInvalidRows = "SELECT * FROM <%=__TABLE_NAME__%> WHERE NOT (id>=1) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String RES_VALIED_ROWS = "SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\") AND  `lname` REGEXP `su.*` "; //$NON-NLS-1$

    private static final String RES_INVALIED_ROWS = "SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\") AND ( `lname`  NOT REGEXP `su.*`  OR `lname` IS NULL )"; //$NON-NLS-1$

    private static final String RES_VALIED_VALUES = "SELECT `lname` FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\") AND  `lname` REGEXP `su.*` "; //$NON-NLS-1$

    private static final String RES_INVALIED_VALUES = "SELECT `lname` FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\") AND ( `lname`  NOT REGEXP `su.*`  OR `lname` IS NULL )"; //$NON-NLS-1$

    private PatternExplorer patternExplorer;

    /**
     * DOC zshen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        patternExplorer = new RegexPatternExplorer();

        // mock setEntity
        PatternMatchingIndicator indicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        // create pattern parameter
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicator.setParameters(createIndicatorParameters);
        Domain createDomain = DomainFactoryImpl.eINSTANCE.createDomain();
        createIndicatorParameters.setDataValidDomain(createDomain);
        Pattern createPattern = PatternFactoryImpl.eINSTANCE.createPattern();
        createDomain.getPatterns().add(createPattern);
        RegularExpression createPatternComponent = PatternFactoryImpl.eINSTANCE.createRegularExpression();
        createPattern.getComponents().add(createPatternComponent);
        TdExpression createTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        createPatternComponent.setExpression(createTdExpression);
        createTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createTdExpression.setBody("`su.*`"); //$NON-NLS-1$

        ModelElement element = RelationalFactoryImpl.eINSTANCE.createTdColumn();
        element.setName("lname"); //$NON-NLS-1$

        indicator.setAnalyzedElement(element);
        Expression createIndiExpression = CoreFactoryImpl.eINSTANCE.createExpression();
        createIndiExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createIndiExpression.setBody("SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\")"); //$NON-NLS-1$
        indicator.setInstantiatedExpression(createIndiExpression);
        Analysis analysis = AnalysisFactoryImpl.eINSTANCE.createAnalysis();
        AnalysisContext createAnalysisContext = AnalysisFactoryImpl.eINSTANCE.createAnalysisContext();
        analysis.setContext(createAnalysisContext);
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "1.0"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "`"); //$NON-NLS-1$
        createAnalysisContext.setConnection(createDatabaseConnection);
        patternExplorer.setAnalysis(analysis);

        ChartDataEntity cdEntity = new ChartDataEntity();
        cdEntity.setIndicator(indicator);

        patternExplorer.setEnitty(cdEntity);

    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#PatternExplorer()}.
     */
    @Test
    public void testPatternExplorer() {
        try {
            RegexPatternExplorer pe = new RegexPatternExplorer();
            Assert.assertNotNull(pe);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidRowsStatement()}.
     */
    @Test
    public void testGetInvalidRowsStatement() {
        String strStatement = patternExplorer.getInvalidRowsStatement();
        Assert.assertEquals(RES_INVALIED_ROWS, strStatement);
    }

    /**
     * test method
     */
    @Test
    public void testGetValidRowsStatement() {
        String strStatement = patternExplorer.getValidRowsStatement();
        Assert.assertEquals(RES_VALIED_ROWS, strStatement);
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidValuesStatement()}.
     */
    @Test
    public void testGetInvalidValuesStatement() {
        String strStatement = patternExplorer.getInvalidValuesStatement();
        Assert.assertEquals(RES_INVALIED_VALUES, strStatement);
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getValidValuesStatement()}.
     */
    @Test
    public void testGetValidValuesStatement() {
        String strStatement = patternExplorer.getValidValuesStatement();
        Assert.assertEquals(RES_VALIED_VALUES, strStatement);
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getValidValuesStatement()}. when the test
     * for indicator is user define indicator
     */
    @Test
    public void testGetValidValuesStatement_2() {
        patternExplorer = new RegexPatternExplorer();

        // mock setEntity
        PatternMatchingIndicator indicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        UDIndicatorDefinition indicatorDef = UserdefineFactoryImpl.eINSTANCE.createUDIndicatorDefinition();
        indicator.setIndicatorDefinition(indicatorDef);
        TdExpression udiTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        udiTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        udiTdExpression.setBody(ViewValidValues);
        indicatorDef.getViewValidValuesExpression().add(udiTdExpression);
        // create pattern parameter
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicator.setParameters(createIndicatorParameters);
        Domain createDomain = DomainFactoryImpl.eINSTANCE.createDomain();
        createIndicatorParameters.setDataValidDomain(createDomain);
        Pattern createPattern = PatternFactoryImpl.eINSTANCE.createPattern();
        createDomain.getPatterns().add(createPattern);
        RegularExpression createPatternComponent = PatternFactoryImpl.eINSTANCE.createRegularExpression();
        createPattern.getComponents().add(createPatternComponent);
        TdExpression createTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        createPatternComponent.setExpression(createTdExpression);
        createTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createTdExpression.setBody("`su.*`"); //$NON-NLS-1$

        TdColumn element = RelationalFactoryImpl.eINSTANCE.createTdColumn();
        element.setName("lname"); //$NON-NLS-1$
        TdTable createTdTable = RelationalFactoryImpl.eINSTANCE.createTdTable();
        createTdTable.setName("table1"); //$NON-NLS-1$
        TableHelper.addColumn(createTdTable, element);
        Catalog createCatalog = orgomg.cwm.resource.relational.impl.RelationalFactoryImpl.eINSTANCE.createCatalog();
        createCatalog.setName("catalog1"); //$NON-NLS-1$
        List<TdTable> tableList = new ArrayList<TdTable>();
        tableList.add(createTdTable);
        CatalogHelper.addTables(tableList, createCatalog);

        indicator.setAnalyzedElement(element);
        Expression createIndiExpression = CoreFactoryImpl.eINSTANCE.createExpression();
        createIndiExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createIndiExpression.setBody("SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\")"); //$NON-NLS-1$
        indicator.setInstantiatedExpression(createIndiExpression);
        Analysis analysis = AnalysisFactoryImpl.eINSTANCE.createAnalysis();
        AnalysisContext createAnalysisContext = AnalysisFactoryImpl.eINSTANCE.createAnalysisContext();
        analysis.setContext(createAnalysisContext);
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "1.0"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "`"); //$NON-NLS-1$
        createAnalysisContext.setConnection(createDatabaseConnection);
        patternExplorer.setAnalysis(analysis);

        ChartDataEntity cdEntity = new ChartDataEntity();
        cdEntity.setIndicator(indicator);

        patternExplorer.setEnitty(cdEntity);

        Assert.assertTrue(patternExplorer.setAnalysis(analysis));

        String clause = patternExplorer.getValidValuesStatement();

        assertEquals("SELECT `lname` FROM `catalog1`.`table1` ", clause); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidValuesStatement()}. when the test
     * for indicator is user define indicator
     */
    @Test
    public void testGetInvalidValuesStatement_3() {
        patternExplorer = new RegexPatternExplorer();

        // mock setEntity
        PatternMatchingIndicator indicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        UDIndicatorDefinition indicatorDef = UserdefineFactoryImpl.eINSTANCE.createUDIndicatorDefinition();
        indicator.setIndicatorDefinition(indicatorDef);
        TdExpression udiTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        udiTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        udiTdExpression.setBody(ViewInvalidValues);
        indicatorDef.getViewInvalidValuesExpression().add(udiTdExpression);
        // create pattern parameter
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicator.setParameters(createIndicatorParameters);
        Domain createDomain = DomainFactoryImpl.eINSTANCE.createDomain();
        createIndicatorParameters.setDataValidDomain(createDomain);
        Pattern createPattern = PatternFactoryImpl.eINSTANCE.createPattern();
        createDomain.getPatterns().add(createPattern);
        RegularExpression createPatternComponent = PatternFactoryImpl.eINSTANCE.createRegularExpression();
        createPattern.getComponents().add(createPatternComponent);
        TdExpression createTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        createPatternComponent.setExpression(createTdExpression);
        createTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createTdExpression.setBody("`su.*`"); //$NON-NLS-1$

        TdColumn element = RelationalFactoryImpl.eINSTANCE.createTdColumn();
        element.setName("lname"); //$NON-NLS-1$
        TdTable createTdTable = RelationalFactoryImpl.eINSTANCE.createTdTable();
        createTdTable.setName("table1"); //$NON-NLS-1$
        TableHelper.addColumn(createTdTable, element);
        Catalog createCatalog = orgomg.cwm.resource.relational.impl.RelationalFactoryImpl.eINSTANCE.createCatalog();
        createCatalog.setName("catalog1"); //$NON-NLS-1$
        List<TdTable> tableList = new ArrayList<TdTable>();
        tableList.add(createTdTable);
        CatalogHelper.addTables(tableList, createCatalog);

        indicator.setAnalyzedElement(element);
        Expression createIndiExpression = CoreFactoryImpl.eINSTANCE.createExpression();
        createIndiExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createIndiExpression.setBody("SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\")"); //$NON-NLS-1$
        indicator.setInstantiatedExpression(createIndiExpression);
        Analysis analysis = AnalysisFactoryImpl.eINSTANCE.createAnalysis();
        AnalysisContext createAnalysisContext = AnalysisFactoryImpl.eINSTANCE.createAnalysisContext();
        analysis.setContext(createAnalysisContext);
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "1.0"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "`"); //$NON-NLS-1$
        createAnalysisContext.setConnection(createDatabaseConnection);

        ChartDataEntity cdEntity = new ChartDataEntity();
        cdEntity.setIndicator(indicator);

        Assert.assertTrue(patternExplorer.setAnalysis(analysis));
        patternExplorer.setEnitty(cdEntity);

        String clause = patternExplorer.getInvalidValuesStatement();

        assertEquals("SELECT `lname` FROM `catalog1`.`table1` WHERE NOT (id>=1) ", clause); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getInvalidRowsStatement()}. when the test
     * for indicator is user define indicator
     */
    @Test
    public void testGetInvalidRowsStatement_4() {
        patternExplorer = new RegexPatternExplorer();

        // mock setEntity
        PatternMatchingIndicator indicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        UDIndicatorDefinition indicatorDef = UserdefineFactoryImpl.eINSTANCE.createUDIndicatorDefinition();
        indicator.setIndicatorDefinition(indicatorDef);
        TdExpression udiTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        udiTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        udiTdExpression.setBody(ViewInvalidRows);
        indicatorDef.getViewInvalidRowsExpression().add(udiTdExpression);
        // create pattern parameter
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicator.setParameters(createIndicatorParameters);
        Domain createDomain = DomainFactoryImpl.eINSTANCE.createDomain();
        createIndicatorParameters.setDataValidDomain(createDomain);
        Pattern createPattern = PatternFactoryImpl.eINSTANCE.createPattern();
        createDomain.getPatterns().add(createPattern);
        RegularExpression createPatternComponent = PatternFactoryImpl.eINSTANCE.createRegularExpression();
        createPattern.getComponents().add(createPatternComponent);
        TdExpression createTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        createPatternComponent.setExpression(createTdExpression);
        createTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createTdExpression.setBody("`su.*`"); //$NON-NLS-1$

        TdColumn element = RelationalFactoryImpl.eINSTANCE.createTdColumn();
        element.setName("lname"); //$NON-NLS-1$
        TdTable createTdTable = RelationalFactoryImpl.eINSTANCE.createTdTable();
        createTdTable.setName("table1"); //$NON-NLS-1$
        TableHelper.addColumn(createTdTable, element);
        Catalog createCatalog = orgomg.cwm.resource.relational.impl.RelationalFactoryImpl.eINSTANCE.createCatalog();
        createCatalog.setName("catalog1"); //$NON-NLS-1$
        List<TdTable> tableList = new ArrayList<TdTable>();
        tableList.add(createTdTable);
        CatalogHelper.addTables(tableList, createCatalog);

        indicator.setAnalyzedElement(element);
        Expression createIndiExpression = CoreFactoryImpl.eINSTANCE.createExpression();
        createIndiExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createIndiExpression.setBody("SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\")"); //$NON-NLS-1$
        indicator.setInstantiatedExpression(createIndiExpression);
        Analysis analysis = AnalysisFactoryImpl.eINSTANCE.createAnalysis();
        AnalysisContext createAnalysisContext = AnalysisFactoryImpl.eINSTANCE.createAnalysisContext();
        analysis.setContext(createAnalysisContext);
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "1.0"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "`"); //$NON-NLS-1$
        createAnalysisContext.setConnection(createDatabaseConnection);

        ChartDataEntity cdEntity = new ChartDataEntity();
        cdEntity.setIndicator(indicator);

        Assert.assertTrue(patternExplorer.setAnalysis(analysis));
        patternExplorer.setEnitty(cdEntity);

        String clause = patternExplorer.getInvalidRowsStatement();

        assertEquals("SELECT * FROM `catalog1`.`table1` WHERE NOT (id>=1) ", clause); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#getValidRowsStatement()}. when the test for
     * indicator is user define indicator
     */
    @Test
    public void testGetValidRowsStatement_5() {
        patternExplorer = new RegexPatternExplorer();

        // mock setEntity
        PatternMatchingIndicator indicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        UDIndicatorDefinition indicatorDef = UserdefineFactoryImpl.eINSTANCE.createUDIndicatorDefinition();
        indicator.setIndicatorDefinition(indicatorDef);
        TdExpression udiTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        udiTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        udiTdExpression.setBody(ViewValidRows);
        indicatorDef.getViewValidRowsExpression().add(udiTdExpression);
        // create pattern parameter
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        indicator.setParameters(createIndicatorParameters);
        Domain createDomain = DomainFactoryImpl.eINSTANCE.createDomain();
        createIndicatorParameters.setDataValidDomain(createDomain);
        Pattern createPattern = PatternFactoryImpl.eINSTANCE.createPattern();
        createDomain.getPatterns().add(createPattern);
        RegularExpression createPatternComponent = PatternFactoryImpl.eINSTANCE.createRegularExpression();
        createPattern.getComponents().add(createPatternComponent);
        TdExpression createTdExpression = RelationalFactoryImpl.eINSTANCE.createTdExpression();
        createPatternComponent.setExpression(createTdExpression);
        createTdExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createTdExpression.setBody("`su.*`"); //$NON-NLS-1$

        TdColumn element = RelationalFactoryImpl.eINSTANCE.createTdColumn();
        element.setName("lname"); //$NON-NLS-1$
        TdTable createTdTable = RelationalFactoryImpl.eINSTANCE.createTdTable();
        createTdTable.setName("table1"); //$NON-NLS-1$
        TableHelper.addColumn(createTdTable, element);
        Catalog createCatalog = orgomg.cwm.resource.relational.impl.RelationalFactoryImpl.eINSTANCE.createCatalog();
        createCatalog.setName("catalog1"); //$NON-NLS-1$
        List<TdTable> tableList = new ArrayList<TdTable>();
        tableList.add(createTdTable);
        CatalogHelper.addTables(tableList, createCatalog);

        indicator.setAnalyzedElement(element);
        Expression createIndiExpression = CoreFactoryImpl.eINSTANCE.createExpression();
        createIndiExpression.setLanguage(SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        createIndiExpression.setBody("SELECT *  FROM `tbi`.`customer`  WHERE (customer.lname = \"sunny\")"); //$NON-NLS-1$
        indicator.setInstantiatedExpression(createIndiExpression);
        Analysis analysis = AnalysisFactoryImpl.eINSTANCE.createAnalysis();
        AnalysisContext createAnalysisContext = AnalysisFactoryImpl.eINSTANCE.createAnalysisContext();
        analysis.setContext(createAnalysisContext);
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_NAME,
                SupportDBUrlType.HIVEDEFAULTURL.getLanguage());
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_PRODUCT_VERSION, "1.0"); //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(createDatabaseConnection, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING, "`"); //$NON-NLS-1$
        createAnalysisContext.setConnection(createDatabaseConnection);

        ChartDataEntity cdEntity = new ChartDataEntity();
        cdEntity.setIndicator(indicator);

        Assert.assertTrue(patternExplorer.setAnalysis(analysis));
        patternExplorer.setEnitty(cdEntity);

        String clause = patternExplorer.getValidRowsStatement();

        assertEquals("SELECT * FROM `catalog1`.`table1` ", clause); //$NON-NLS-1$
    }

    /**
     * 
     * Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#isImplementRegexFunction()}.test return
     * true;
     */
    @Test
    public void testIsImplementRegexFunction_True() {
        String label = DataExplorer.MENU_VIEW_VALID_ROWS;
        boolean implementRegexFunction = patternExplorer.isImplementRegexFunction(label);
        assertTrue(implementRegexFunction);
        label = DataExplorer.MENU_VIEW_VALID_VALUES;
        implementRegexFunction = patternExplorer.isImplementRegexFunction(label);
        assertTrue(implementRegexFunction);
        label = DataExplorer.MENU_VIEW_INVALID_VALUES;
        implementRegexFunction = patternExplorer.isImplementRegexFunction(label);
        assertTrue(implementRegexFunction);
        label = DataExplorer.MENU_VIEW_INVALID_ROWS;
        assertTrue(implementRegexFunction);
    }

    /**
     * 
     * Mock Test method for {@link org.talend.dq.analysis.explore.PatternExplorer#isImplementRegexFunction()}.test
     * return false;
     */
    @Test
    public void testIsImplementRegexFunction_False() {
        String label = null;

        boolean implementRegexFunction = this.patternExplorer.isImplementRegexFunction(label);
        assertFalse(implementRegexFunction);

        label = "ab";//$NON-NLS-1$
        implementRegexFunction = this.patternExplorer.isImplementRegexFunction(label);
        assertFalse(implementRegexFunction);
    }

}
