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

import static org.powermock.api.mockito.PowerMockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
public class DuplicateCountIndicatorImplTest {

    private DuplicateCountIndicator dupIndicator;

    private ResultSet result;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        dupIndicator = IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator();
        ((DuplicateCountIndicatorImpl) dupIndicator).setUsedMapDBMode(false);
        // init the resultset: columnsize:2, rowscount: 6
        result = mock(ResultSet.class);
        when(result.getObject(1)).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4").thenReturn("5").thenReturn("6");
        when(result.getObject(2)).thenReturn("1000").thenReturn("2000").thenReturn("1000").thenReturn("1000").thenReturn("3000")
                .thenReturn("3000");
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl#reset()}.
     */
    @Test
    public void testReset() {
        this.dupIndicator.reset();
        Assert.assertTrue(dupIndicator.getDuplicateMap().size() < 1);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl#finalizeComputation()}.
     * after all handled.
     */
    @Test
    public void testFinalizeComputation() {
        this.dupIndicator.reset();
        this.testHandle_1();
        this.dupIndicator.finalizeComputation();
        Assert.assertTrue(this.dupIndicator.getDuplicateValueCount() == 2);
    }

    @Test
    public void testGetDuplicateValues() {
        this.dupIndicator.reset();
        this.testHandle_1();
        this.dupIndicator.finalizeComputation();
        Set<Object> dupValues = dupIndicator.getDuplicateValues();
        Assert.assertTrue(dupValues.size() == 2);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl#getDuplicateValueCount()}.
     */
    @Test
    public void testGetDuplicateValueCount() {
        this.dupIndicator.reset();
        this.testHandle_1();
        Assert.assertNull(this.dupIndicator.getDuplicateValueCount());
        this.dupIndicator.finalizeComputation();
        Assert.assertTrue(this.dupIndicator.getDuplicateValueCount() == 2);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl#handle(java.lang.Object, java.sql.ResultSet, int)}
     * .
     */
    @Test
    public void testHandle_1() {
        this.dupIndicator.reset();
        try {
            this.dupIndicator.handle("1000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 1);
            this.dupIndicator.handle("2000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 2);
            this.dupIndicator.handle("1000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 2);
            this.dupIndicator.handle("1000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 2);
            this.dupIndicator.handle("3000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 3);
            this.dupIndicator.handle("3000", result, 2);
            Assert.assertTrue(dupIndicator.getDuplicateMap().size() == 3);
        } catch (SQLException e) {
            Assert.fail("test method: DuplicateCountIndicator.handle(Object,Resultset,int) failed.");
        }

    }

}
