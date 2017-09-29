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
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.factories.PatternIndicatorFactory;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;

/**
 * created by yyin on 2016年5月6日 Detailled comment
 *
 */
public class ModelElementAnalysisHandlerTest {

    private ModelElementAnalysisHandler maHandler;

    @Before
    public void setUp() throws Exception {
        maHandler = new ModelElementAnalysisHandler();
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ModelElementAnalysisHandler#initializeIndicator(org.talend.dataquality.indicators.Indicator)}
     * .
     */
    @Test
    public void testInitializeIndicator() {

        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName("old name"); //$NON-NLS-1$
        // give a pattern indicator in the analysis result, which name is different with its pattern's name
        SqlPatternMatchingIndicator patternMatchingIndicator = PatternIndicatorFactory.createSqlPatternMatchingIndicator(pattern);
        patternMatchingIndicator.setIndicatorDefinition(DefinitionFactory.eINSTANCE.createIndicatorDefinition());
        pattern.setName("new name"); //$NON-NLS-1$

        assertEquals("old name", patternMatchingIndicator.getName()); //$NON-NLS-1$
        // if the pattern's name is changed, then this method should use the newest name for the pattern indicator.
        maHandler.initializeIndicator(patternMatchingIndicator);
        assertEquals("new name", patternMatchingIndicator.getName()); //$NON-NLS-1$

        // user indicator
        Indicator userIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        userIndicator.setName("indicator name"); //$NON-NLS-1$
        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        indicatorDefinition.setName("definition name"); //$NON-NLS-1$
        userIndicator.setIndicatorDefinition(indicatorDefinition);

        assertEquals("indicator name", userIndicator.getName()); //$NON-NLS-1$
        // if the udi's name is changed, then this method should use the newest name for the udi.
        maHandler.initializeIndicator(userIndicator);
        assertEquals("definition name", userIndicator.getName()); //$NON-NLS-1$

        // should not effect system indicators
        SimpleStatIndicator simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();
        simpleStatIndicator.setName("simple stat"); //$NON-NLS-1$
        simpleStatIndicator.setIndicatorDefinition(indicatorDefinition);
        assertNotSame(indicatorDefinition.getName(), simpleStatIndicator.getName());
        maHandler.initializeIndicator(simpleStatIndicator);
        assertNotSame(indicatorDefinition.getName(), simpleStatIndicator.getName());
    }

}
