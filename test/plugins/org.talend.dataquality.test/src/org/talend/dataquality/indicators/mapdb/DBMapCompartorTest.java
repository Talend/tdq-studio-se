// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.mapdb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.dataquality.indicators.mapdb.DBMapCompartor.NullCompareStrategy;

public class DBMapCompartorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.mapdb.DBMapCompartor#compare(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testCompare() {
        DBMapCompartor dbMapCompartor = new DBMapCompartor();
        int compare = dbMapCompartor.compare(null, 1);
        Assert.assertEquals("null should be less than 1", -1, compare);
        compare = dbMapCompartor.compare(1, null);
        Assert.assertEquals("1 should be more than null", 1, compare);
        compare = dbMapCompartor.compare(null, null);
        Assert.assertEquals("null should be equals with null", 0, compare);

        // empty constrator == NullCompareStrategy.nullLessThanOthers
        dbMapCompartor = new DBMapCompartor(NullCompareStrategy.nullLessThanOthers);
        compare = dbMapCompartor.compare(null, 1);
        Assert.assertEquals("null should be less than 1", -1, compare);
        compare = dbMapCompartor.compare(1, null);
        Assert.assertEquals("1 should be more than null", 1, compare);
        compare = dbMapCompartor.compare(null, null);
        Assert.assertEquals("null should be equals with null", 0, compare);

        dbMapCompartor = new DBMapCompartor(NullCompareStrategy.nullMoreThanOthers);
        compare = dbMapCompartor.compare(null, 1);
        Assert.assertEquals("null should be more than 1", 1, compare);
        compare = dbMapCompartor.compare(1, null);
        Assert.assertEquals("1 should be less than null", -1, compare);
        compare = dbMapCompartor.compare(null, null);
        Assert.assertEquals("null should be equals with null", 0, compare);
    }

}
