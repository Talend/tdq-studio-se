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
package org.talend.dataquality.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by talend on Sep 12, 2012 Detailled comment
 * 
 */
public class IndicatorHelperTest {

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // do nothing
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.helpers.IndicatorHelper#getRowCountIndicator(orgomg.cwm.objectmodel.core.ModelElement, java.util.Map)}
     * . get RowCountIndicator
     */
    @Test
    public void testGetRowCountIndicator1() {
        // TdColumn
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        // ~

        // Indicator
        Indicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
        Indicator nullCountIndicator = IndicatorsFactory.eINSTANCE.createNullCountIndicator();
        // ~

        // List
        List<Indicator> list = new ArrayList<Indicator>();
        list.add(rowCountIndicator);
        list.add(nullCountIndicator);
        // ~
        // Map
        Map<ModelElement, List<Indicator>> elementToIndicator = new HashMap<ModelElement, List<Indicator>>();
        elementToIndicator.put(column1, list);
        // ~
        RowCountIndicator rowCountIndicator2 = IndicatorHelper.getRowCountIndicator(column1, elementToIndicator);
        assert (rowCountIndicator2 != null);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.helpers.IndicatorHelper#getRowCountIndicator(orgomg.cwm.objectmodel.core.ModelElement, java.util.Map)}
     * . get RowCountIndicator
     */
    @Test
    public void testGetRowCountIndicator2() {
        // TdColumn
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        // ~

        // List
        List<Indicator> list = null;
        // ~
        // Map
        Map<ModelElement, List<Indicator>> elementToIndicator = new HashMap<ModelElement, List<Indicator>>();
        elementToIndicator.put(column1, list);
        // ~
        RowCountIndicator rowCountIndicator2 = IndicatorHelper.getRowCountIndicator(column1, elementToIndicator);
        assert (rowCountIndicator2 == null);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.helpers.IndicatorHelper#getNullCountIndicator(orgomg.cwm.objectmodel.core.ModelElement, java.util.Map)}
     * . get NullCountIndicator
     */
    @Test
    public void testGetNullCountIndicator1() {
        // TdColumn
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        // ~

        // Indicator
        Indicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
        Indicator nullCountIndicator = IndicatorsFactory.eINSTANCE.createNullCountIndicator();
        // ~

        // List
        List<Indicator> list = new ArrayList<Indicator>();
        list.add(rowCountIndicator);
        list.add(nullCountIndicator);
        // ~
        // Map
        Map<ModelElement, List<Indicator>> elementToIndicator = new HashMap<ModelElement, List<Indicator>>();
        elementToIndicator.put(column1, list);
        // ~
        NullCountIndicator nullCountIndicator2 = IndicatorHelper.getNullCountIndicator(column1, elementToIndicator);
        assert (nullCountIndicator2 != null);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.helpers.IndicatorHelper#getNullCountIndicator(orgomg.cwm.objectmodel.core.ModelElement, java.util.Map)}
     * . get NullCountIndicator
     */
    @Test
    public void testGetNullCountIndicator2() {
        // TdColumn
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        // ~

        // List
        List<Indicator> list = null;
        // ~
        // Map
        Map<ModelElement, List<Indicator>> elementToIndicator = new HashMap<ModelElement, List<Indicator>>();
        elementToIndicator.put(column1, list);
        // ~
        NullCountIndicator nullCountIndicator2 = IndicatorHelper.getNullCountIndicator(column1, elementToIndicator);
        assert (nullCountIndicator2 == null);
    }

    /**
     * 
     * Test method for get pattern name based on indicator.
     */
    @Test
    public void testGetPatternName() {
        Indicator indicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        IndicatorParameters parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain validData = DomainFactory.eINSTANCE.createDomain();
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        String pn = "Blank Text"; //$NON-NLS-1$
        pattern.setName(pn);
        validData.getPatterns().add(pattern);
        parameters.setDataValidDomain(validData);
        indicator.setParameters(parameters);
        String patternName = IndicatorHelper.getPatternName(indicator);
        assert (patternName.equals(pn));
    }

    /**
     * 
     * Test method for get pattern based on indicator.
     */
    @Test
    public void testGetPattern_1() {

        Indicator indicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        IndicatorParameters parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain validData = DomainFactory.eINSTANCE.createDomain();
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName("Blank Text"); //$NON-NLS-1$
        validData.getPatterns().add(pattern);
        parameters.setDataValidDomain(validData);
        indicator.setParameters(parameters);
        Pattern pattern2 = IndicatorHelper.getPattern(indicator);
        assert (pattern2 != null);
        assert (pattern2.equals(pattern));
    }

}
