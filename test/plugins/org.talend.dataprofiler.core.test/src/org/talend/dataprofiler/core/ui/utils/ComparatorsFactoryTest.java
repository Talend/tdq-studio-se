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
package org.talend.dataprofiler.core.ui.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.dq.indicators.ext.FrequencyExt;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class ComparatorsFactoryTest {

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.utils.ComparatorsFactory#sort(java.lang.Object[], int)}.
     */
    @Test
    public void testSortObject_1() {
        FrequencyExt[] frequencyExt = new FrequencyExt[9];
        Long value = 100l;
        for (int i = 0; i < 9; i++) {
            frequencyExt[i] = new FrequencyExt();
            frequencyExt[i].setKey(i);
            frequencyExt[i].setValue(value);

            if (i > 5) {
                value = value * 2;
            } else {
                value = value / 2;
            }
        }

        Assert.assertEquals("0", frequencyExt[0].getKey().toString());
        ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.BENFORDLAW_FREQUENCY_COMPARATOR_ID);
        for (int i = 0; i < 7; i++) {
            Assert.assertTrue(frequencyExt[i].getKey().toString().compareTo(frequencyExt[i + 1].getKey().toString()) < 0);
        }
        Assert.assertEquals("0", frequencyExt[8].getKey().toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSortObject_2() {
        FrequencyExt[] frequencyExt = new FrequencyExt[9];
        Long value = 100l;
        double key = 1d;
        for (int i = 0; i < 9; i++) {
            frequencyExt[i] = new FrequencyExt();
            frequencyExt[i].setKey(key);
            frequencyExt[i].setValue(value);
            if (i > 5) {
                value = value * 2;
            } else {
                value = value / 2;
            }
            key++;
        }

        ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.BENFORDLAW_FREQUENCY_COMPARATOR_ID);
        for (int i = 0; i < 8; i++) {
            Assert.assertTrue(frequencyExt[i].getKey().toString().compareTo(frequencyExt[i + 1].getKey().toString()) < 0);
        }
    }


}
