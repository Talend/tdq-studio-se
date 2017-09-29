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
 * DOC qiongli class global comment. Detailled comment
 */
public class InvalidRegCodeCountIndicatorImplTest {

    InvalidRegCodeCountIndicatorImpl invalidRegCodeCountIndicatorImpl = null;

    private Object data_invalid[] = { "+08618611281173", "+086 18611281173", "86 18611281173", "086 18611281173", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
            "08618611281173", "13693273494", "123", "", null }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    private Object data_mixed[] = {
            "+41446681800", "+41 446681800", "+86 18611281173", "+8613521588310", "+86 1352 1588 310", "+86 135 2158 8310", "+08618611281173", null };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 

    private Object data_valid[] = {
            "+41446681800", "+41 446681800", "+86 18611281173", "+8613521588310", "+86 1352 1588 310", "+86 135 2158 8310" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 

    @Before
    public void setUp() throws Exception {
        invalidRegCodeCountIndicatorImpl = new InvalidRegCodeCountIndicatorImpl();
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_1() {
        for (Object obj : data_invalid) {
            this.invalidRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(9, invalidRegCodeCountIndicatorImpl.getInvalidRegCount().longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_2() {
        for (Object obj : data_valid) {
            this.invalidRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(0, invalidRegCodeCountIndicatorImpl.getInvalidRegCount().longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WellFormE164PhoneCountIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle_3() {
        for (Object obj : data_mixed) {
            this.invalidRegCodeCountIndicatorImpl.handle(obj);

        }
        assertEquals(2, invalidRegCodeCountIndicatorImpl.getInvalidRegCount().longValue());
    }

}
