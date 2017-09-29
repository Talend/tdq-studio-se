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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * DOC talend2 class global comment. Detailled comment
 */
public class ValidRegCodeCountIndicatorImplTest {

    ValidRegCodeCountIndicatorImpl validRegCodeCountIndicatorImpl = null;

    private Object data_invalid[] = { "+08618611281173", "+086 18611281173", "86 18611281173", "086 18611281173", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
            "08618611281173", "13693273494", "123", "", null }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    private Object data_mixed[] = {
            "+86 18611281173", "+8613521588310", "+86 135 2158 8310", "+86 1352 1588 310", "+08618611281173", null };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 

    private Object data_valid[] = { "+86 18611281173", "+8613521588310", "+86 1352 1588 310", "+86 135 2158 8310" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    @Before
    public void setUp() throws Exception {
        validRegCodeCountIndicatorImpl = new ValidRegCodeCountIndicatorImpl();
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_1() {
        for (Object obj : data_invalid) {
            this.validRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(0, validRegCodeCountIndicatorImpl.getValidRegCount().longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_2() {
        for (Object obj : data_valid) {
            this.validRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(4, validRegCodeCountIndicatorImpl.getValidRegCount().longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_3() {
        for (Object obj : data_mixed) {
            this.validRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(4, validRegCodeCountIndicatorImpl.getValidRegCount().longValue());
    }

}
