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
package org.talend.commons.MapDB.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.mapdb.ColumnSetDBMap;
import org.talend.dataquality.indicators.validation.IDataValidationFactory;

/**
 * created by talend on Nov 4, 2014 Detailled comment
 * 
 */
public class ColumnSetDBMapTest {

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map)}
     * Case 1 normal case
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectCase1() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        List<Object[]> subList = dbMap1.subList(0, 2, new HashMap<Long, List<Object>>());
        Assert.assertEquals(2, subList.size());
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map)}
     * Case 2 indexMap is null
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectCase2() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        List<Object[]> subList = dbMap1.subList(0, 2, null);
        Assert.assertEquals(2, subList.size());
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map)}
     * Case 3 fromIndex more than toIndex
     * 
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectCase3() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        List<Object[]> subList = dbMap1.subList(2, 0, null);
        Assert.assertEquals(0, subList.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map, org.talend.cwm.indicator.DataValidation)}
     * Case 1 Unique case
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectDataValidationCase1() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        UniqueCountIndicator uniqueCountIndicator = IndicatorsFactory.eINSTANCE.createUniqueCountIndicator();
        List<Object[]> subList = dbMap1.subList(0, 2, new HashMap<Long, List<Object>>(),
                IDataValidationFactory.INSTANCE.createValidation(uniqueCountIndicator));
        Assert.assertEquals(1, subList.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map, org.talend.cwm.indicator.DataValidation)}
     * Case 2 duplicate case
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectDataValidationCase2() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        DuplicateCountIndicator duplicateCountIndicator = IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator();
        List<Object[]> subList = dbMap1.subList(0, 2, new HashMap<Long, List<Object>>(),
                IDataValidationFactory.INSTANCE.createValidation(duplicateCountIndicator));
        Assert.assertEquals(1, subList.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map, org.talend.cwm.indicator.DataValidation)}
     * Case 3 distinct case
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectDataValidationCase3() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        DistinctCountIndicator distinctCountIndicator = IndicatorsFactory.eINSTANCE.createDistinctCountIndicator();
        List<Object[]> subList = dbMap1.subList(0, 2, new HashMap<Long, List<Object>>(),
                IDataValidationFactory.INSTANCE.createValidation(distinctCountIndicator));
        Assert.assertEquals(2, subList.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.mapdb.ColumnSetDBMap#subList(long, long, java.util.Map, org.talend.cwm.indicator.DataValidation)}
     * Case 4 row count case
     */
    @Test
    public void testSubListLongLongMapOfLongListOfObjectDataValidationCase4() {
        ColumnSetDBMap dbMap1 = new ColumnSetDBMap();
        Assert.assertEquals(true, dbMap1.isEmpty());
        List<Object> keyList = new ArrayList<Object>();
        keyList.add("id1"); //$NON-NLS-1$
        keyList.add("name1"); //$NON-NLS-1$
        dbMap1.put(keyList, 1l);
        keyList = new ArrayList<Object>();
        keyList.add("id2");//$NON-NLS-1$
        keyList.add("name2"); //$NON-NLS-1$
        dbMap1.put(keyList, 2l);
        Assert.assertEquals(2, dbMap1.size());
        RowCountIndicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
        List<Object[]> subList = dbMap1.subList(0, 2, new HashMap<Long, List<Object>>(),
                IDataValidationFactory.INSTANCE.createValidation(rowCountIndicator));
        Assert.assertEquals(2, subList.size());
    }

}
