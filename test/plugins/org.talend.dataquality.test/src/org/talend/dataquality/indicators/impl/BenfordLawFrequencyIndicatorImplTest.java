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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.ResourceUtils;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class BenfordLawFrequencyIndicatorImplTest {

    private BenfordLawFrequencyIndicator benIndicator;

    private List<Object[]> values;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benIndicator = IndicatorsFactory.eINSTANCE.createBenfordLawFrequencyIndicator();
        ResourceUtils.createAnalysis(benIndicator);

        values = new ArrayList<Object[]>();
        values.add(new Object[] { "1", 30L });
        values.add(new Object[] { "2", 20L });
        values.add(new Object[] { "3", 16L });
        values.add(new Object[] { "4", 12L });
        values.add(new Object[] { "5", 9L });
        values.add(new Object[] { "6", 7L });
        values.add(new Object[] { "7", 5L });
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        benIndicator.getValueToFreq().clear();
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandleObject() {

    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl#checkValues()}.
     * test normal data
     * 
     * @throws Exception
     */
    @Test
    public void testCheckValues_1() throws Exception {
        values.add(new Object[] { "8", 3L });
        values.add(new Object[] { "9", 1L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(9, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 3L);
        Assert.assertTrue(valueMap.get("9") == 1L);
    }

    private void assertinternal(HashMap<Object, Long> valueMap) {
        Assert.assertTrue(valueMap.get("1") == 30L);
        Assert.assertTrue(valueMap.get("2") == 20L);
        Assert.assertTrue(valueMap.get("3") == 16L);
        Assert.assertTrue(valueMap.get("4") == 12L);
        Assert.assertTrue(valueMap.get("5") == 9L);
        Assert.assertTrue(valueMap.get("6") == 7L);
        Assert.assertTrue(valueMap.get("7") == 5L);
    }

    /**
     * test for miss numbers in 1~9
     */
    @Test
    public void testCheckValues_2() {
        // miss 8,9
        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(9, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 0L);
        Assert.assertTrue(valueMap.get("9") == 0L);
    }

    /**
     * test for include "0" (double type)
     */
    @Test
    public void testCheckValues_3() {
        values.add(new Object[] { "8", 9L });
        values.add(new Object[] { "9", 6L });
        values.add(new Object[] { "0", 3L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(10, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 9L);
        Assert.assertTrue(valueMap.get("9") == 6L);
        Assert.assertTrue(valueMap.get("0") == 3L);
    }

    /**
     * test for null value
     */
    @Test
    public void testCheckValues_4() {
        values.add(new Object[] { "8", 3L });
        values.add(new Object[] { "9", 1L });
        values.add(new Object[] { null, 3L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(10, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 3L);
        Assert.assertTrue(valueMap.get("9") == 1L);
        Assert.assertTrue(valueMap.get("invalid") == 3L);

    }

    /**
     * test for string type invalid value= 3+3+3=9
     */
    @Test
    public void testCheckValues_5() {
        values.add(new Object[] { "8", 3L });
        values.add(new Object[] { "9", 1L });
        values.add(new Object[] { "a", 3L });
        values.add(new Object[] { "b", 3L });
        values.add(new Object[] { "c", 3L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(10, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 3L);
        Assert.assertTrue(valueMap.get("9") == 1L);
        Assert.assertTrue(valueMap.get("invalid") == 9L);
    }

    /**
     * test for key length>1
     */
    @Test
    public void testCheckValues_6() {
        values.add(new Object[] { "8     ", 3L });
        values.add(new Object[] { "9                     ", 1L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(9, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 3L);
        Assert.assertTrue(valueMap.get("9") == 1L);
        Assert.assertTrue(valueMap.get("8     ") == null);
    }

    /**
     * test for key length>1, with some invalid(length>1) which should not be effected
     */
    @Test
    public void testCheckValues_7() {
        values.add(new Object[] { "8     ", 3L });
        values.add(new Object[] { "9                     ", 1L });
        values.add(new Object[] { "null", 3L });
        values.add(new Object[] { "a", 3L });

        benIndicator.storeSqlResults(values);
        HashMap<Object, Long> valueMap = benIndicator.getValueToFreq();
        Assert.assertEquals(10, valueMap.size());
        assertinternal(valueMap);
        Assert.assertTrue(valueMap.get("8") == 3L);
        Assert.assertTrue(valueMap.get("9") == 1L);
        Assert.assertTrue(valueMap.get("8     ") == null);
        Assert.assertTrue(valueMap.get("invalid") == 6L);

    }

    /**
     * unit test for TDQ-6480: Benford Law Frequency indicator chart doesn't show the column which data is zero in
     * generated report file
     * 
     * @throws Exception
     */
    @Test
    public void testCheckValues_ConcurrentModification() throws Exception {
        final AtomicReference<Exception> atom = new AtomicReference<Exception>();
        final HashMap<Object, Long> tempMap = prepareTempMap();

        benIndicator.setValueToFreq(new HashMap<Object, Long>(tempMap));
        final int nbLoop = 1000;

        // simulate datamart persistence
        final HashMap<Object, Long> newMap = benIndicator.getValueToFreq();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < nbLoop; i++) {
                        Iterator<Object> it = newMap.keySet().iterator();
                        while (it.hasNext()) {
                            Object next = it.next();
                        }
                    }
                } catch (Exception e) {
                    atom.set(e);
                }
            }
        });
        thread.start();

        // simulate saving analysis to file
        try {
            for (int i = 0; i < nbLoop; i++) {
                HashMap<Object, Long> newMap1 = benIndicator.getValueToFreq();
            }
        } catch (Exception e) {
            atom.set(e);
        }

        thread.join();
        if (atom.get() != null) {
            throw atom.get();
        }

    }

    @Test
    public void testCheckValues_VerifyDataSQL() throws Exception {

        final HashMap<Object, Long> tempMap = prepareTempMap();

        benIndicator.setValueToFreq(new HashMap<Object, Long>(tempMap));
        HashMap<Object, Long> newMapSQL = benIndicator.getValueToFreq();
        // compare newMap and tempMap, should be still the same.
        Iterator<Object> it = newMapSQL.keySet().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            assertTrue(next + " not found in map.", tempMap.containsKey(next));
        }
    }

    @Test
    public void testCheckValues_VerifyDataJAVA() throws Exception {
        // init indicator
        assertTrue(benIndicator.reset());

        final HashMap<Object, Long> tempMap = prepareTempMap();
        Iterator<Object> it = tempMap.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            Long value = tempMap.get(key);
            for (long i = 0; i < value; i++) {
                benIndicator.handle(key);
            }
        }

        // check content of value2freq map
        HashMap<Object, Long> newMapJava1 = new HashMap<Object, Long>(benIndicator.getValueToFreq());
        Iterator<Object> itCheck1 = newMapJava1.keySet().iterator();
        while (itCheck1.hasNext()) {
            Object key = itCheck1.next();
            Long count = newMapJava1.get(key);
            if (count == 0L) {
                assertFalse(key + " should not exist in map.", tempMap.containsKey(key));
            } else if (!"invalid".equals(key.toString())) {
                assertTrue(key + " not found in map.", tempMap.containsKey(key));
            }
        }

        // call finalize
        benIndicator.finalizeComputation();

        // check content of value2freq map
        HashMap<Object, Long> newMapJava2 = new HashMap<Object, Long>(benIndicator.getValueToFreq());
        Iterator<Object> itCheck2 = tempMap.keySet().iterator();
        while (itCheck2.hasNext()) {
            Object next = itCheck2.next();
            String key = String.valueOf(next);
            if (key.length() > 1) { // text values
                assertFalse(next + " must not be found in map.", newMapJava2.containsKey(next));
            }
        }

        // compare them
        compare(newMapJava1, newMapJava2);
    }

    private void compare(HashMap<Object, Long> newMapJava1, HashMap<Object, Long> newMapJava2) {
        Assert.assertEquals("size is different", newMapJava1.size(), newMapJava2.size());
        for (Object key : newMapJava1.keySet()) {
            Assert.assertEquals("value changed for " + key, newMapJava1.get(key), newMapJava2.get(key));
        }
    }

    private HashMap<Object, Long> prepareTempMap() {
        final HashMap<Object, Long> tempMap = new HashMap<Object, Long>();
        tempMap.put("tytttt", 15L);
        tempMap.put("uugf", 15L);
        tempMap.put("jzefds", 15L);
        tempMap.put("gfrtj", 15L);
        tempMap.put("gre", 15L);
        tempMap.put("wcbt", 15L);
        tempMap.put("pitg", 15L);
        tempMap.put("rtuj", 15L);
        tempMap.put("qdn", 15L);
        tempMap.put("lakhf", 15L);
        tempMap.put("1", 15L);
        tempMap.put("2", 15L);
        tempMap.put("3", 15L);
        tempMap.put("5", 15L);
        tempMap.put("8", 15L);
        return tempMap;
    }
}
