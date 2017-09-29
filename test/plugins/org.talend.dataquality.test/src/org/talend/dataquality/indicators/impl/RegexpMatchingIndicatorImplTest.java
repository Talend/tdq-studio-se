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
package org.talend.dataquality.indicators.impl;

import junit.framework.Assert;

import org.eclipse.emf.common.util.EList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;

/**
 * created by talend on Nov 9, 2012 Detailled comment
 * 
 */
public class RegexpMatchingIndicatorImplTest {

    private static final String JavaRegex = "'^java expression$'"; //$NON-NLS-1$

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
     * Test method for {@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl#getJavaRegex()}.
     */
    @Test
    public void testGetRegexFail1NotJavaRegex() {
        // RegexpMatchingIndicator
        RegexpMatchingIndicator createRegexpMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        // ~RegexpMatchingIndicator

        // IndicatorParameters
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        // ~IndicatorParameters

        // Domain
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        EList<Pattern> patterns = createDomain.getPatterns();
        // ~Domain

        // Pattern
        Pattern createJavaPattern = PatternFactory.eINSTANCE.createPattern();
        Pattern createSQLPattern = PatternFactory.eINSTANCE.createPattern();
        Pattern createMSSQLPattern = PatternFactory.eINSTANCE.createPattern();
        patterns.add(createJavaPattern);
        patterns.add(createSQLPattern);
        patterns.add(createMSSQLPattern);
        // ~Pattern

        // init java Pattern data
        RegularExpression createJavaRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createJavaTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createJavaTdExpression.setBody(JavaRegex);
        createJavaTdExpression.setLanguage("Microsoft SQL Server");
        createJavaRegularExpression.setExpression(createJavaTdExpression);
        createJavaRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createJavaPattern.getComponents();
        components.add(createJavaRegularExpression);
        // ~init java Pattern data

        createIndicatorParameters.setDataValidDomain(createDomain);
        createRegexpMatchingIndicator.setParameters(createIndicatorParameters);
        TdColumn column = RelationalFactory.eINSTANCE.createTdColumn();

        MetadataTable mdColumn = RelationalFactory.eINSTANCE.createTdTable();
        column.setTable(mdColumn);
        createRegexpMatchingIndicator.setAnalyzedElement(column);
        // call getRegex()
        String regexResult = createRegexpMatchingIndicator.getRegex();
        // ~call getRegex()
        Assert.assertTrue(regexResult == null);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl#getJavaRegex()}.
     */
    @Test
    public void testGetRegexFail2DomainIsNull() {
        // RegexpMatchingIndicator
        RegexpMatchingIndicator createRegexpMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        // ~RegexpMatchingIndicator

        // IndicatorParameters
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        // ~IndicatorParameters

        createRegexpMatchingIndicator.setParameters(createIndicatorParameters);

        // call getRegex()
        String regexResult = createRegexpMatchingIndicator.getRegex();
        // ~call getRegex()
        Assert.assertTrue(regexResult == null);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl#getJavaRegex()}.
     */
    @Test
    public void testGetRegexsuccess() {
        // RegexpMatchingIndicator
        RegexpMatchingIndicator createRegexpMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        // ~RegexpMatchingIndicator

        // IndicatorParameters
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        // ~IndicatorParameters

        // Domain
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        EList<Pattern> patterns = createDomain.getPatterns();
        // ~Domain

        // Pattern
        Pattern createJavaPattern = PatternFactory.eINSTANCE.createPattern();
        Pattern createSQLPattern = PatternFactory.eINSTANCE.createPattern();
        Pattern createMSSQLPattern = PatternFactory.eINSTANCE.createPattern();
        patterns.add(createJavaPattern);
        patterns.add(createSQLPattern);
        patterns.add(createMSSQLPattern);
        // ~Pattern

        // init java Pattern data
        RegularExpression createJavaRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createJavaTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createJavaTdExpression.setBody(JavaRegex);
        createJavaTdExpression.setLanguage("Java");
        createJavaRegularExpression.setExpression(createJavaTdExpression);
        createJavaRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createJavaPattern.getComponents();
        components.add(createJavaRegularExpression);
        // ~init java Pattern data

        createIndicatorParameters.setDataValidDomain(createDomain);
        createRegexpMatchingIndicator.setParameters(createIndicatorParameters);

        // call getRegex()
        String regexResult = createRegexpMatchingIndicator.getRegex();
        String JavaRegex2 = JavaRegex.substring(1, JavaRegex.length() - 1);
        // ~call getRegex()
        Assert.assertTrue(JavaRegex2.equalsIgnoreCase(regexResult));
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl#getJavaRegex()}.
     */
    @Test
    public void testGetRegexWithDefaultSQLWhenNoJava() {
        // RegexpMatchingIndicator
        RegexpMatchingIndicator createRegexpMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        // ~RegexpMatchingIndicator

        // IndicatorParameters
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        // ~IndicatorParameters

        // Domain
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        EList<Pattern> patterns = createDomain.getPatterns();
        // ~Domain

        // Pattern
        Pattern createSQLPattern = PatternFactory.eINSTANCE.createPattern();
        Pattern createMSSQLPattern = PatternFactory.eINSTANCE.createPattern();
        patterns.add(createSQLPattern);
        patterns.add(createMSSQLPattern);
        // ~Pattern

        // init default:SQL Pattern data
        RegularExpression createDefaultExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createDefaultTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createDefaultTdExpression.setBody("'sql body'");
        createDefaultTdExpression.setLanguage("SQL");
        createDefaultExpression.setExpression(createDefaultTdExpression);
        createDefaultExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createSQLPattern.getComponents();
        components.add(createDefaultExpression);
        // ~init default Pattern data

        createIndicatorParameters.setDataValidDomain(createDomain);
        createRegexpMatchingIndicator.setParameters(createIndicatorParameters);

        // call getRegex()
        String regexResult = createRegexpMatchingIndicator.getRegex();
        // ~call getRegex()
        Assert.assertTrue("sql body".equalsIgnoreCase(regexResult));
    }
}
