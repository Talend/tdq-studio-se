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

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.indicators.ModeIndicator;

/**
 * test for ModeIndicatorImpl class.
 */
public class ModeIndicatorImplTest {

    private ModeIndicator modeIndicator;

    /**
     * setup.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        modeIndicator = new ModeIndicatorImpl();
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.ModeIndicatorImpl#finalizeComputation()} .
     */
    @Test
    public void testfinalizeComputation() {
        HashMap<Object, Long> tempMap = new HashMap<Object, Long>();
        tempMap.put("2", 2L); //$NON-NLS-1$
        tempMap.put("1", 1L); //$NON-NLS-1$
        tempMap.put("33", 3L); //$NON-NLS-1$
        tempMap.put("bb", 4L); //$NON-NLS-1$
        tempMap.put("5a", 5L); //$NON-NLS-1$
        tempMap.put("8", 8L); //$NON-NLS-1$
        tempMap.put(null, 7L);
        modeIndicator.setValueToFreq(tempMap);
        ((ModeIndicatorImpl) modeIndicator).setUsedMapDBMode(false);
        this.modeIndicator.finalizeComputation();
        Assert.assertTrue("8".equals(modeIndicator.getMode())); //$NON-NLS-1$

        tempMap.put(null, 9L);
        modeIndicator.setValueToFreq(tempMap);
        this.modeIndicator.finalizeComputation();
        Assert.assertTrue(modeIndicator.getMode() == null);
    }

}
