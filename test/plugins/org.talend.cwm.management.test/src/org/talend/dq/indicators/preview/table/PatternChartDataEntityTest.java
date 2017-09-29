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
package org.talend.dq.indicators.preview.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * created by qiongli on 2014-3-31 Detailled comment
 * 
 */
public class PatternChartDataEntityTest {

    private PatternChartDataEntity patternChartDataEntity = new PatternChartDataEntity();

    private Indicator indicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();

    /**
     * DOC qiongli Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        patternChartDataEntity.setIndicator(indicator);

    }

    /**
     * DOC qiongli Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * it should be "N/A" when the denominator is 0.
     */
    @Test
    public void testGetPerMatch_NA() {
        patternChartDataEntity.setNumMatch("0"); //$NON-NLS-1$
        indicator.setCount(new Long(0));
        String perMatch = patternChartDataEntity.getPerMatch();
        assertEquals(perMatch, "N/A"); //$NON-NLS-1$
    }

    /**
     * 
     * it should not be "N/A" when denominator is 0.
     */
    @Test
    public void testGetPerMatch_1() {
        patternChartDataEntity.setNumMatch("1"); //$NON-NLS-1$
        indicator.setCount(new Long(5));
        String perMatch = patternChartDataEntity.getPerMatch();
        assertEquals(perMatch, "20.00%"); //$NON-NLS-1$
        assertFalse(perMatch.equals("N/A")); //$NON-NLS-1$
    }

    /**
     * it should be "N/A" when the denominator is 0.
     */
    @Test
    public void testGetPerNoMatch_NA() {
        patternChartDataEntity.setNumNoMatch("0"); //$NON-NLS-1$
        indicator.setCount(new Long(0));
        String perNotMatch = patternChartDataEntity.getPerNoMatch();
        assertEquals(perNotMatch, "N/A"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.indicators.preview.table.PatternChartDataEntity#getPerNoMatch()}.
     */
    @Test
    public void testGetPerNoMatch() {
        patternChartDataEntity.setNumNoMatch("1"); //$NON-NLS-1$
        indicator.setCount(new Long(10));
        String perNotMatch = patternChartDataEntity.getPerNoMatch();
        assertEquals(perNotMatch, "10.00%"); //$NON-NLS-1$
        assertFalse(perNotMatch.equals("N/A")); //$NON-NLS-1$
    }

}
