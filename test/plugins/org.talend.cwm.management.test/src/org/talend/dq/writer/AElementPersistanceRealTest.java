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
package org.talend.dq.writer;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.reports.ReportsFactory;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.DQRuleWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.dq.writer.impl.IndicatorDefinitionWriter;
import org.talend.dq.writer.impl.PatternWriter;
import org.talend.dq.writer.impl.ReportWriter;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwmx.analysis.informationreporting.InformationreportingFactory;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * created by xqliu on Jul 23, 2013 Detailled comment
 * 
 */
public class AElementPersistanceRealTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty("studio.version", "6.3");
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemAnalysis() {
        AnalysisWriter createAnalysisWrite = ElementWriterFactory.getInstance().createAnalysisWrite();
        // test normal name
        String anaName = "ana1"; //$NON-NLS-1$
        String exceptedFileName = anaName + "_0.1.ana"; //$NON-NLS-1$
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        createAnalysis.setName(anaName);
        Item createItem = createAnalysisWrite.createItem(createAnalysis);
        assertTrue(createItem instanceof TDQAnalysisItem);
        TDQAnalysisItem analysisItem = (TDQAnalysisItem) createItem;
        assertTrue(analysisItem.getFilename().equals(exceptedFileName));
        // test special name
        String anaNameWithSpecialChar = "ana1~!`"; //$NON-NLS-1$
        String exceptedFileName2 = "ana1____0.1.ana"; //$NON-NLS-1$
        Analysis createAnalysis2 = AnalysisFactory.eINSTANCE.createAnalysis();
        createAnalysis2.setName(anaNameWithSpecialChar);
        Item createItem2 = createAnalysisWrite.createItem(createAnalysis2);
        assertTrue(createItem2 instanceof TDQAnalysisItem);
        TDQAnalysisItem analysisItem2 = (TDQAnalysisItem) createItem2;
        assertTrue(analysisItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemReport() {
        ReportWriter createReportWriter = ElementWriterFactory.getInstance().createReportWriter();
        // test normal name
        String repName = "rep1"; //$NON-NLS-1$
        String exceptedFileName = repName + "_0.1.rep"; //$NON-NLS-1$
        Report createReport = InformationreportingFactory.eINSTANCE.createReport();
        createReport.setName(repName);
        Item createItem = createReportWriter.createItem(createReport);
        assertTrue(createItem instanceof TDQReportItem);
        TDQReportItem reportItem = (TDQReportItem) createItem;
        assertTrue(reportItem.getFilename().equals(exceptedFileName));
        // test special name
        String repName2 = "rep1#^&"; //$NON-NLS-1$
        String exceptedFileName2 = "rep1____0.1.rep"; //$NON-NLS-1$
        Report createReport2 = InformationreportingFactory.eINSTANCE.createReport();
        createReport2.setName(repName2);
        Item createItem2 = createReportWriter.createItem(createReport2);
        assertTrue(createItem2 instanceof TDQReportItem);
        TDQReportItem reportItem2 = (TDQReportItem) createItem2;
        assertTrue(reportItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemWhereRule() {
        DQRuleWriter createdRuleWriter = ElementWriterFactory.getInstance().createdRuleWriter();
        // test normal name
        String whereRuleName = "whereRule1"; //$NON-NLS-1$
        String exceptedFileName = whereRuleName + "_0.1.rules"; //$NON-NLS-1$
        WhereRule createWhereRule = RulesFactory.eINSTANCE.createWhereRule();
        createWhereRule.setName(whereRuleName);
        Item createItem = createdRuleWriter.createItem(createWhereRule);
        assertTrue(createItem instanceof TDQBusinessRuleItem);
        TDQBusinessRuleItem ruleItem = (TDQBusinessRuleItem) createItem;
        assertTrue(ruleItem.getFilename().equals(exceptedFileName));
        // test special name
        String whereRuleName2 = "whereRule1*\\/?"; //$NON-NLS-1$
        String exceptedFileName2 = "whereRule1_____0.1.rules"; //$NON-NLS-1$
        WhereRule createWhereRule2 = RulesFactory.eINSTANCE.createWhereRule();
        createWhereRule2.setName(whereRuleName2);
        Item createItem2 = createdRuleWriter.createItem(createWhereRule2);
        assertTrue(createItem2 instanceof TDQBusinessRuleItem);
        TDQBusinessRuleItem ruleItem2 = (TDQBusinessRuleItem) createItem2;
        assertTrue(ruleItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemParserRule() {
        DQRuleWriter createdRuleWriter = ElementWriterFactory.getInstance().createdRuleWriter();
        // test normal name
        String parserRuleName = "parserRule1"; //$NON-NLS-1$
        String exceptedFileName = parserRuleName + "_0.1.rules"; //$NON-NLS-1$
        ParserRule createParserRule = RulesFactory.eINSTANCE.createParserRule();
        createParserRule.setName(parserRuleName);
        Item createItem = createdRuleWriter.createItem(createParserRule);
        assertTrue(createItem instanceof TDQBusinessRuleItem);
        TDQBusinessRuleItem ruleItem = (TDQBusinessRuleItem) createItem;
        assertTrue(ruleItem.getFilename().equals(exceptedFileName));
        // test special name
        String parserRuleName2 = "parserRule1:;\"."; //$NON-NLS-1$
        String exceptedFileName2 = "parserRule1_____0.1.rules"; //$NON-NLS-1$
        ParserRule createParserRule2 = RulesFactory.eINSTANCE.createParserRule();
        createParserRule2.setName(parserRuleName2);
        Item createItem2 = createdRuleWriter.createItem(createParserRule2);
        assertTrue(createItem2 instanceof TDQBusinessRuleItem);
        TDQBusinessRuleItem ruleItem2 = (TDQBusinessRuleItem) createItem2;
        assertTrue(ruleItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemPattern() {
        PatternWriter createPatternWriter = ElementWriterFactory.getInstance().createPatternWriter();
        // test normal name
        String patternName = "pattern1"; //$NON-NLS-1$
        String exceptedFileName = patternName + "_0.1.pattern"; //$NON-NLS-1$
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        createPattern.setName(patternName);
        Item createItem = createPatternWriter.createItem(createPattern);
        assertTrue(createItem instanceof TDQPatternItem);
        TDQPatternItem patternItem = (TDQPatternItem) createItem;
        assertTrue(patternItem.getFilename().equals(exceptedFileName));
        // test special name
        String patternName2 = "pattern1()，。"; //$NON-NLS-1$
        String exceptedFileName2 = "pattern1_____0.1.pattern"; //$NON-NLS-1$
        Pattern createPattern2 = PatternFactory.eINSTANCE.createPattern();
        createPattern2.setName(patternName2);
        Item createItem2 = createPatternWriter.createItem(createPattern2);
        assertTrue(createItem2 instanceof TDQPatternItem);
        TDQPatternItem patternItem2 = (TDQPatternItem) createItem2;
        assertTrue(patternItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#createItem(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testCreateItemIndicatorDefinition() {
        IndicatorDefinitionWriter createIndicatorDefinitionWriter = ElementWriterFactory.getInstance()
                .createIndicatorDefinitionWriter();
        // test normal name
        String indDefName = "indDef1"; //$NON-NLS-1$
        String exceptedFileName = indDefName + "_0.1.definition"; //$NON-NLS-1$
        IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        createIndicatorDefinition.setName(indDefName);
        Item createItem = createIndicatorDefinitionWriter.createItem(createIndicatorDefinition);
        assertTrue(createItem instanceof TDQIndicatorDefinitionItem);
        TDQIndicatorDefinitionItem indDefItem = (TDQIndicatorDefinitionItem) createItem;
        assertTrue(indDefItem.getFilename().equals(exceptedFileName));
        // test special name
        String indDefName2 = "indDef1'￥‘”、《，》<> "; //$NON-NLS-1$
        String exceptedFileName2 = "indDef1____________0.1.definition"; //$NON-NLS-1$
        IndicatorDefinition createIndicatorDefinition2 = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        createIndicatorDefinition2.setName(indDefName2);
        Item createItem2 = createIndicatorDefinitionWriter.createItem(createIndicatorDefinition2);
        assertTrue(createItem2 instanceof TDQIndicatorDefinitionItem);
        TDQIndicatorDefinitionItem indDefItem2 = (TDQIndicatorDefinitionItem) createItem2;
        assertTrue(indDefItem2.getFilename().equals(exceptedFileName2));
    }

    /**
     * Test method for {@link org.talend.dq.writer.AElementPersistance#save(Item, boolean)}.
     * 
     * @throws PersistenceException
     * 
     * case1: between UDI and analysis
     */
    @Ignore
    @Test
    public void testSaveAnalysisCase1() throws PersistenceException {
        // AnalysisWriter createAnalysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
        // // create analysis
        // Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        // AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        // AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        // createAnalysis.setResults(createAnalysisResult);
        // createAnalysis.setContext(createAnalysisContext);
        //        createAnalysis.setName("AElementPersistanceRealTestanalysis1"); //$NON-NLS-1$
        //
        // // create analysis item
        // TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        // createTDQAnalysisItem.setAnalysis(createAnalysis);
        // Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis1"); //$NON-NLS-1$
        // createTDQAnalysisItem.setProperty(createAnalysisProperty);
        // createAnalysisProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);
        //
        // // create UDI
        // UserDefIndicator createUserDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        // createAnalysisResult.getIndicators().add(createUserDefIndicator);
        //
        // // create definition
        // UDIndicatorDefinition createUDIndicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        // createUserDefIndicator.setIndicatorDefinition(createUDIndicatorDefinition);
        // createAnalysisResult.getIndicators().add(createUserDefIndicator);
        // TDQIndicatorDefinitionItem createTDQIndicatorDefinitionItem = PropertiesFactory.eINSTANCE
        // .createTDQIndicatorDefinitionItem();
        // createTDQIndicatorDefinitionItem.setIndicatorDefinition(createUDIndicatorDefinition);
        // Property createProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createProperty.setLabel("AElementPersistanceRealTestUDIIndicatorDefinition1"); //$NON-NLS-1$
        // createTDQIndicatorDefinitionItem.setProperty(createProperty);
        // createProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQIndicatorDefinitionItem,
        // new Path(ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS.getFolder()).removeFirstSegments(2), false);
        // // case add dependency
        // ReturnCode save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        //
        // Assert.assertEquals(1, createAnalysis.getClientDependency().size());
        // Assert.assertEquals(1, createUDIndicatorDefinition.getSupplierDependency().size());
        // Assert.assertEquals(1, createUDIndicatorDefinition.getSupplierDependency().get(0).getClient().size());
        // // case remove dependency
        // createAnalysisResult.getIndicators().remove(createUserDefIndicator);
        // save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        //
        // Assert.assertEquals(0, createAnalysis.getClientDependency().size());
        //
        // Assert.assertEquals(1, createUDIndicatorDefinition.getSupplierDependency().size());
        // Assert.assertEquals(0, createUDIndicatorDefinition.getSupplierDependency().get(0).getClient().size());
    }

    /**
     * Test method for {@link org.talend.dq.writer.AElementPersistance#save(Item, boolean)}.
     * 
     * @throws PersistenceException
     * 
     * case2: between Pattern and analysis
     */
    @Ignore
    @Test
    public void testSaveAnalysisCase2() throws PersistenceException {
        // AnalysisWriter createAnalysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
        // // create analysis
        // Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        // AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        // AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        // createAnalysis.setResults(createAnalysisResult);
        // createAnalysis.setContext(createAnalysisContext);
        //        createAnalysis.setName("AElementPersistanceRealTestanalysis2"); //$NON-NLS-1$
        //
        // // create analysis item
        // TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        // createTDQAnalysisItem.setAnalysis(createAnalysis);
        // Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis2"); //$NON-NLS-1$
        // createTDQAnalysisItem.setProperty(createAnalysisProperty);
        // createAnalysisProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);
        //
        // // create pattern match indicator
        // RegexpMatchingIndicator regexMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        //
        // // create pattern parameter
        // IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        // regexMatchingIndicator.setParameters(createIndicatorParameters);
        //
        // // create definition
        // IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        // regexMatchingIndicator.setIndicatorDefinition(createIndicatorDefinition);
        // TDQIndicatorDefinitionItem createTDQIndicatorDefinitionItem = PropertiesFactory.eINSTANCE
        // .createTDQIndicatorDefinitionItem();
        // createTDQIndicatorDefinitionItem.setIndicatorDefinition(createIndicatorDefinition);
        // Property createProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createProperty.setLabel("AElementPersistanceRealTestUDIIndicatorDefinition2"); //$NON-NLS-1$
        // createTDQIndicatorDefinitionItem.setProperty(createProperty);
        // createProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQIndicatorDefinitionItem, Path.EMPTY, false);
        //
        // // create pattern
        // Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        // createAnalysisResult.getIndicators().add(regexMatchingIndicator);
        // TDQPatternItem createTDQPatternItem = PropertiesFactory.eINSTANCE.createTDQPatternItem();
        // createTDQPatternItem.setPattern(pattern);
        // Property createPatternProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createPatternProperty.setLabel("AElementPersistanceRealTestpattern2"); //$NON-NLS-1$
        // createTDQPatternItem.setProperty(createPatternProperty);
        // createPatternProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQPatternItem,
        // new Path(ERepositoryObjectType.TDQ_PATTERN_REGEX.getFolder()).removeFirstSegments(2), false);
        //
        // // create Domain
        // Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        // createIndicatorParameters.setDataValidDomain(createDomain);
        // createIndicatorParameters.getDataValidDomain().getPatterns().add(pattern);
        // // case add dependency
        // ReturnCode save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        // Assert.assertEquals(1, createAnalysis.getClientDependency().size());
        //
        // Assert.assertEquals(1, pattern.getSupplierDependency().size());
        // Assert.assertEquals(1, pattern.getSupplierDependency().get(0).getClient().size());
        // // case remove dependency
        // createAnalysisResult.getIndicators().remove(regexMatchingIndicator);
        // save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        //
        // Assert.assertEquals(0, createAnalysis.getClientDependency().size());
        //
        // Assert.assertEquals(1, pattern.getSupplierDependency().size());
        // Assert.assertEquals(0, pattern.getSupplierDependency().get(0).getClient().size());
    }

    /**
     * Test method for {@link org.talend.dq.writer.AElementPersistance#save(Item, boolean)}.
     * 
     * @throws PersistenceException
     * 
     * case3: between Rule and analysis
     */
    @Ignore
    @Test
    public void testSaveAnalysisCase3() throws PersistenceException {
        // AnalysisWriter createAnalysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
        // // create analysis
        // Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        // AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        // AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        // createAnalysis.setResults(createAnalysisResult);
        // createAnalysis.setContext(createAnalysisContext);
        //        createAnalysis.setName("AElementPersistanceRealTestanalysis3"); //$NON-NLS-1$
        //
        // // create analysis item
        // TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        // createTDQAnalysisItem.setAnalysis(createAnalysis);
        // Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis3"); //$NON-NLS-1$
        // createTDQAnalysisItem.setProperty(createAnalysisProperty);
        // createAnalysisProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);
        //
        // // create pattern match indicator
        // WhereRuleIndicator whereRuleIndicator = IndicatorSqlFactory.eINSTANCE.createWhereRuleIndicator();
        // // create definition
        //
        // createAnalysisResult.getIndicators().add(whereRuleIndicator);
        // DQRule dqRule = RulesFactory.eINSTANCE.createWhereRule();
        // whereRuleIndicator.setIndicatorDefinition(dqRule);
        // TDQBusinessRuleItem createTDQBusinessRuleItem = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
        // createTDQBusinessRuleItem.setDqrule(dqRule);
        // Property createPatternProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createPatternProperty.setLabel("AElementPersistanceRealTestDQRule3"); //$NON-NLS-1$
        // createTDQBusinessRuleItem.setProperty(createPatternProperty);
        // createPatternProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQBusinessRuleItem, Path.EMPTY, false);
        //
        // // case add dependency
        // ReturnCode save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        // Assert.assertEquals(1, createAnalysis.getClientDependency().size());
        //
        // Assert.assertEquals(1, dqRule.getSupplierDependency().size());
        // Assert.assertEquals(1, dqRule.getSupplierDependency().get(0).getClient().size());
        // // case remove dependency
        // createAnalysisResult.getIndicators().remove(whereRuleIndicator);
        // save = createAnalysisWriter.save(createTDQAnalysisItem, true);
        // Assert.assertTrue(save.isOk());
        //
        // Assert.assertEquals(0, createAnalysis.getClientDependency().size());
        //
        // Assert.assertEquals(1, dqRule.getSupplierDependency().size());
        // Assert.assertEquals(0, dqRule.getSupplierDependency().get(0).getClient().size());

    }

    /**
     * Test method for {@link org.talend.dq.writer.AElementPersistance#save(Item, boolean)}.
     * 
     * @throws PersistenceException
     * 
     * case1: between analysis and report
     */
    @Ignore
    @Test
    public void testSaveReportCase1() throws PersistenceException {
        // ReportWriter createReportWriter = ElementWriterFactory.getInstance().createReportWriter();
        // // create analysis
        // Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        // AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        // createAnalysis.setResults(createAnalysisResult);
        //        createAnalysis.setName("AElementPersistanceRealTestanalysis4"); //$NON-NLS-1$
        //
        // // create analysis item
        // TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        // createTDQAnalysisItem.setAnalysis(createAnalysis);
        // Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis4"); //$NON-NLS-1$
        // createAnalysisProperty.setId(EcoreUtil.generateUUID());
        // createTDQAnalysisItem.setProperty(createAnalysisProperty);
        // ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);
        //
        // // create report
        // TdReport createTdReport = ReportsFactory.eINSTANCE.createTdReport();
        // TDQReportItem createTDQReportItem = PropertiesFactory.eINSTANCE.createTDQReportItem();
        // createTDQReportItem.setReport(createTdReport);
        // Property createReportProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        //        createReportProperty.setLabel("AElementPersistanceRealTestreport4"); //$NON-NLS-1$
        // createTDQReportItem.setProperty(createReportProperty);
        // createReportProperty.setId(EcoreUtil.generateUUID());
        // ProxyRepositoryFactory.getInstance().create(createTDQReportItem, Path.EMPTY, false);
        // createTdReport.addAnalysis(createAnalysis);
        //
        // // case add dependency
        // ReturnCode save = createReportWriter.save(createTDQReportItem, true);
        // Assert.assertTrue(save.isOk());
        // Assert.assertEquals(1, createTdReport.getClientDependency().size());
        //
        // Assert.assertEquals(1, createAnalysis.getSupplierDependency().size());
        // Assert.assertEquals(1, createAnalysis.getSupplierDependency().get(0).getClient().size());
        // // case remove dependency
        // createTdReport.removeAnalysis(createAnalysis);
        // save = createReportWriter.save(createTDQReportItem, true);
        // Assert.assertTrue(save.isOk());
        //
        // Assert.assertEquals(0, createTdReport.getClientDependency().size());
        //
        // Assert.assertEquals(1, createAnalysis.getSupplierDependency().size());
        // Assert.assertEquals(0, createAnalysis.getSupplierDependency().get(0).getClient().size());

    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#saveWithDependencies(org.talend.core.model.properties.Item, orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     * 
     * @throws PersistenceException
     */
    @Test
    public void testSaveWithDependenciesAnalysis() throws PersistenceException {

        AnalysisWriter createAnalysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
        String anaName = "AElementPersistanceRealTestanalysis5";
        // create analysis
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        createAnalysis.setResults(createAnalysisResult);
        createAnalysis.setContext(createAnalysisContext);
        createAnalysis.setName(anaName);

        // create analysis item
        TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        createTDQAnalysisItem.setAnalysis(createAnalysis);
        Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis5"); //$NON-NLS-1$
        createTDQAnalysisItem.setProperty(createAnalysisProperty);
        createAnalysisProperty.setId(EcoreUtil.generateUUID());
        ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);

        // create pattern match indicator
        WhereRuleIndicator whereRuleIndicator = IndicatorSqlFactory.eINSTANCE.createWhereRuleIndicator();
        // create definition

        createAnalysisResult.getIndicators().add(whereRuleIndicator);
        DQRule dqRule = RulesFactory.eINSTANCE.createWhereRule();
        whereRuleIndicator.setIndicatorDefinition(dqRule);
        TDQBusinessRuleItem createTDQBusinessRuleItem = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
        createTDQBusinessRuleItem.setDqrule(dqRule);
        Property createPatternProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createPatternProperty.setLabel("AElementPersistanceRealTestDQRule5"); //$NON-NLS-1$
        createTDQBusinessRuleItem.setProperty(createPatternProperty);
        createPatternProperty.setId(EcoreUtil.generateUUID());
        ProxyRepositoryFactory.getInstance().create(createTDQBusinessRuleItem, Path.EMPTY, false);
        String exceptedFileName = anaName + "_0.1.ana"; //$NON-NLS-1$

        ReturnCode create = createAnalysisWriter.saveWithDependencies(createTDQAnalysisItem, createAnalysis);
        assertTrue(create.isOk());
        Assert.assertEquals(exceptedFileName, createTDQAnalysisItem.getFilename());

        String anaName2 = "ana52"; //$NON-NLS-1$
        String exceptedFileName2 = anaName2 + "_0.1.ana"; //$NON-NLS-1$
        createAnalysis.setName(anaName2);

        create = createAnalysisWriter.saveWithDependencies(createTDQAnalysisItem, createAnalysis);
        assertTrue(create.isOk());
        Assert.assertEquals(exceptedFileName2, createTDQAnalysisItem.getFilename());

    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.AElementPersistance#saveWithoutDependencies(org.talend.core.model.properties.Item, orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     * 
     * @throws PersistenceException
     */
    @Test
    public void testSaveWithoutDependenciesReport() throws PersistenceException {
        ReportWriter createReportWriter = ElementWriterFactory.getInstance().createReportWriter();
        // create analysis
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        createAnalysis.setResults(createAnalysisResult);
        createAnalysis.setName("AElementPersistanceRealTestanalysis6"); //$NON-NLS-1$

        // create analysis item
        TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        createTDQAnalysisItem.setAnalysis(createAnalysis);
        Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createAnalysisProperty.setLabel("AElementPersistanceRealTestanalysis6"); //$NON-NLS-1$
        createAnalysisProperty.setId(EcoreUtil.generateUUID());
        createTDQAnalysisItem.setProperty(createAnalysisProperty);
        ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);

        // create report
        TdReport createTdReport = ReportsFactory.eINSTANCE.createTdReport();
        TDQReportItem createTDQReportItem = PropertiesFactory.eINSTANCE.createTDQReportItem();
        createTDQReportItem.setReport(createTdReport);
        Property createReportProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createReportProperty.setLabel("AElementPersistanceRealTestreport6"); //$NON-NLS-1$
        createTDQReportItem.setProperty(createReportProperty);
        createReportProperty.setId(EcoreUtil.generateUUID());
        ProxyRepositoryFactory.getInstance().create(createTDQReportItem, Path.EMPTY, false);
        createTdReport.addAnalysis(createAnalysis);

        String repName = "rep61"; //$NON-NLS-1$
        String exceptedFileName = repName + "_0.1.rep"; //$NON-NLS-1$
        createTdReport.setName(repName);

        ReturnCode create = createReportWriter.saveWithoutDependencies(createTDQReportItem, createTdReport);
        assertTrue(create.isOk());
        Assert.assertEquals(exceptedFileName, createTDQReportItem.getFilename());

        String repName2 = "rep62"; //$NON-NLS-1$
        String exceptedFileName2 = repName2 + "_0.1.rep"; //$NON-NLS-1$
        createTdReport.setName(repName2);

        create = createReportWriter.saveWithDependencies(createTDQReportItem, createTdReport);
        assertTrue(create.isOk());
        Assert.assertEquals(exceptedFileName2, createTDQReportItem.getFilename());
    }
}
