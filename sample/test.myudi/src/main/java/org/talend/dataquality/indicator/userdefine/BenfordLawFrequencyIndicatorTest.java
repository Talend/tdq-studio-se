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
package org.talend.dataquality.indicator.userdefine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class BenfordLawFrequencyIndicatorTest {

    private BenfordLawFrequencyIndicator bUDI;
    /**
     * DOC yyin Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        bUDI = new BenfordLawFrequencyIndicator();
        bUDI.handle("12345");
    }

    /**
     * DOC yyin Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataquality.indicator.userdefine.BenfordLawFrequencyIndicator#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle() {
        boolean added = bUDI.handle("2234");
        assertTrue(added);

        assertTrue(bUDI.getCount(Integer.valueOf("1")) == 1L);
        assertTrue(bUDI.getCount(Integer.valueOf("2")) == 1L);

        double fre = bUDI.getFrequency(Integer.valueOf("1"));
        assertTrue(fre == 0.5d);

    }

    /**
     * Test method for {@link org.talend.dataquality.indicator.userdefine.BenfordLawFrequencyIndicator#reset()}.
     */
    @Test
    public void testReset() {
        bUDI.reset();
        assertTrue(bUDI.getCount(Integer.valueOf("1")) == 0L);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl#getValueToFreq()}.
     */
    @Test
    public void testGetValueToFreq() {
        assertNotNull(bUDI.getValueToFreq());
        assertTrue(bUDI.getValueToFreq().size() == 1);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl#getFrequency(java.lang.Object)}.
     */
    @Test
    public void testGetFrequency() {
        bUDI.handle("1234");
        double fre = bUDI.getFrequency(Integer.valueOf("1"));
        assertTrue(fre == 1d);
    }

}
