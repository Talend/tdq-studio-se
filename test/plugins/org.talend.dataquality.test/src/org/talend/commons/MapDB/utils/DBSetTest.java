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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.dataquality.indicators.mapdb.DBMapParameter;
import org.talend.dataquality.indicators.mapdb.DBSet;

/**
 * created by talend on Aug 4, 2014 Detailled comment
 * 
 */
public class DBSetTest {

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#hashCode()}.
     */
    @Test
    public void testHashCode() {
        DBSet<String> dbSet1 = new DBSet<String>();
        DBMapParameter dbMapParameter = new DBMapParameter();
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(dbSet1.hashCode(), dbSet2.hashCode());
        dbSet1.add("key1"); //$NON-NLS-1$
        dbSet2.add("key1"); //$NON-NLS-1$
        Assert.assertEquals(dbSet1.hashCode(), dbSet2.hashCode());
        dbSet1.add("key2"); //$NON-NLS-1$
        Assert.assertNotSame(dbSet1.hashCode(), dbSet2.hashCode());

    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#iterator()}.
     */
    @Test
    public void testIterator() {
        DBSet<String> dbSet1 = new DBSet<String>();
        dbSet1.add("key1"); //$NON-NLS-1$
        dbSet1.add("key2"); //$NON-NLS-1$
        Iterator<String> iterator = dbSet1.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Assert.assertEquals(true, key.equals("key2") ? true : key.equals("key1"));
        }
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#size()}.
     */
    @Test
    public void testSize() {
        DBSet<String> dbSet1 = new DBSet<String>();
        DBMapParameter dbMapParameter = new DBMapParameter();
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(0, dbSet1.size());
        Assert.assertEquals(0, dbSet2.size());
        dbSet1.add("key1"); //$NON-NLS-1$
        Assert.assertEquals(1, dbSet1.size());
        dbSet2.add("key1"); //$NON-NLS-1$
        Assert.assertEquals(1, dbSet2.size());
        dbSet2.add("key2"); //$NON-NLS-1$
        Assert.assertEquals(2, dbSet2.size());
        dbSet1.add("key2"); //$NON-NLS-1$
        Assert.assertEquals(2, dbSet1.size());
        dbSet2.add("key2"); //$NON-NLS-1$
        Assert.assertEquals(2, dbSet2.size());
        dbSet1.add("key2"); //$NON-NLS-1$
        Assert.assertEquals(2, dbSet1.size());
        dbSet1.close();
        dbSet2.close();
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#contains(java.lang.Object)}.
     */
    @Test
    public void testContains() {
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet2.add("key1"); //$NON-NLS-1$
        boolean containsKey = dbSet2.contains("key1"); //$NON-NLS-1$
        Assert.assertEquals(true, containsKey);
        containsKey = dbSet2.contains("key2"); //$NON-NLS-1$
        Assert.assertEquals(false, containsKey);
        dbSet2.close();
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#toArray()}.
     */
    @Test
    public void testToArray() {
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet2.add("key1"); //$NON-NLS-1$
        dbSet2.add("key2"); //$NON-NLS-1$
        boolean containsKey = dbSet2.contains("key1"); //$NON-NLS-1$
        Assert.assertEquals(true, containsKey);
        Object[] array = dbSet2.toArray();
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(true, array[0].equals("key1") ? true : array[1].equals("key1"));
        Assert.assertEquals(true, array[0].equals("key2") ? true : array[1].equals("key2"));
        dbSet2.close();
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#add(java.lang.Object)}.
     */
    @Test
    public void testAdd() {
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet2.add("key1"); //$NON-NLS-1$
        try {
            dbSet2.add(null);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
        dbSet2.close();
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#remove(java.lang.Object)}.
     */
    @Test
    public void testRemove() {
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet2.add("key1"); //$NON-NLS-1$
        dbSet2.remove("key1"); //$NON-NLS-1$
        Assert.assertEquals(true, dbSet2.isEmpty());
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#addAll(java.util.Collection)}.
     */
    @Test
    public void testAddAll() {
        Set<String> tempSet = new HashSet<String>();
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        tempSet.add("name1");
        tempSet.add("name2");
        dbSet2.addAll(tempSet);
        Assert.assertEquals(true, dbSet2.contains("name1"));
        Assert.assertEquals(true, dbSet2.contains("name2"));
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#removeAll(java.util.Collection)}.
     */
    @Test
    public void testRemoveAll() {
        Set<String> tempSet = new HashSet<String>();
        DBMapParameter dbMapParameter = null;
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        tempSet.add("name1");
        tempSet.add("name2");
        dbSet2.addAll(tempSet);
        Assert.assertEquals(true, dbSet2.contains("name1"));
        Assert.assertEquals(true, dbSet2.contains("name2"));
        dbSet2.removeAll(tempSet);
        Assert.assertEquals(true, dbSet2.isEmpty());
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.DBSet#clear()}.
     */
    @Test
    public void testClear() {
        DBSet<String> dbSet1 = new DBSet<String>();
        DBMapParameter dbMapParameter = new DBMapParameter();
        DBSet<String> dbSet2 = new DBSet<String>(dbMapParameter);
        Assert.assertEquals(true, dbSet1.isEmpty());
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet1.add("key1"); //$NON-NLS-1$
        dbSet2.add("key1"); //$NON-NLS-1$
        Assert.assertEquals(false, dbSet1.isEmpty());
        Assert.assertEquals(false, dbSet2.isEmpty());
        dbSet1.clear();
        dbSet2.clear();
        Assert.assertEquals(true, dbSet1.isEmpty());
        Assert.assertEquals(true, dbSet2.isEmpty());
        dbSet1.close();
        dbSet2.close();
    }
}
