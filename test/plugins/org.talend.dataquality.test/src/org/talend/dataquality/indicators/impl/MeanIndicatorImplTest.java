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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * mzhao Junit test for mean indicator
 */
public class MeanIndicatorImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetMean() {
        MeanIndicatorImpl meanInd = new MeanIndicatorImpl();
        meanInd.setSumStr("18");
        meanInd.setCount(6L);
        meanInd.setNullCount(3L);
        assertTrue(meanInd.getMean().doubleValue() == 3);
        meanInd.setNullCount(null);
        assertTrue(meanInd.getMean().doubleValue() == 3);
        meanInd.setNullCount(0L);
        assertTrue(meanInd.getMean().doubleValue() == 3);
        meanInd.setCount(6L);
        meanInd.setNullCount(6L);
        assertTrue(meanInd.getMean().doubleValue() == 3);
    }

}
