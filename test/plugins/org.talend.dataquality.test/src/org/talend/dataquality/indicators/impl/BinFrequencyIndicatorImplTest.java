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

import java.util.HashMap;

import org.junit.Test;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * created by talend2 on 2014-10-30 Detailled comment
 * 
 */
public class BinFrequencyIndicatorImplTest {

    BinFrequencyIndicatorImpl binFreqIndicator = null;

    private void init() {
        if (binFreqIndicator == null) {
            binFreqIndicator = new BinFrequencyIndicatorImpl();
            binFreqIndicator.setUsedMapDBMode(false);
            binFreqIndicator.setUniqueValueCount(0l);
            TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
            tdColumn.setName("id");
            binFreqIndicator.setAnalyzedElement(tdColumn);

        }
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl#handle(java.lang.Object)}
     * domain parameter is set in this case. .
     */
    @Test
    public void testHandleObject_1() {
        init();
        IndicatorParameters parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain domain = DomainHelper.createContiguousClosedBinsIntoDomain("test", 2, 1, 9); //$NON-NLS-1$
        parameters.setBins(domain);
        binFreqIndicator.setParameters(parameters);
        binFreqIndicator.reset();
        int i = 1;
        binFreqIndicator.handle(i);
        HashMap<Object, Long> valueToFreq = binFreqIndicator.getValueToFreq();
        Long value = valueToFreq.get("id >= 1.0 AND id < 5.0");
        assertEquals(1l, value.longValue());

        i = 3;
        binFreqIndicator.handle(i);
        value = valueToFreq.get("id >= 1.0 AND id < 5.0");
        assertEquals(2l, value.longValue());

        i = 6;
        binFreqIndicator.handle(i);
        value = valueToFreq.get("id >= 5.0 AND id < 9.0");
        assertEquals(1l, value.longValue());

        Object obj = null;
        binFreqIndicator.handle(obj);
        value = valueToFreq.get(null);
        assertNull(value);
    }

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl#handle(java.lang.Object)}
     * domain parameter isn't set in this case. .
     */
    @Test
    public void testHandleObject_2() {
        init();
        int i = 1;
        binFreqIndicator.handle(i);
        HashMap<Object, Long> valueToFreq = binFreqIndicator.getValueToFreq();
        Long value = valueToFreq.get(1);
        assertEquals(1l, value.longValue());

        i = 1;
        binFreqIndicator.handle(i);
        value = valueToFreq.get(1);
        assertEquals(2l, value.longValue());

        i = 6;
        binFreqIndicator.handle(i);
        value = valueToFreq.get(6);
        assertEquals(1l, value.longValue());

        Object obj = null;
        binFreqIndicator.handle(obj);
        value = valueToFreq.get(null);
        assertEquals(1l, value.longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl#getFormatName(java.lang.Object)}. the
     * domain parameter is set in this case.
     */
    @Test
    public void testGetGroupLabel_1() {
        init();
        IndicatorParameters parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain domain = DomainHelper.createContiguousClosedBinsIntoDomain("test", 2, 1, 9); //$NON-NLS-1$
        parameters.setBins(domain);
        binFreqIndicator.setParameters(parameters);
        binFreqIndicator.reset();
        int i = 1;
        String formatName = binFreqIndicator.getGroupLabel(i);
        assertEquals(formatName, "id >= 1.0 AND id < 5.0");

        i = 6;
        formatName = binFreqIndicator.getGroupLabel(i);
        assertEquals(formatName, "id >= 5.0 AND id < 9.0");

        i = 12;
        formatName = binFreqIndicator.getGroupLabel(i);
        assertNull(formatName);

    }

    @Test
    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.BinFrequencyIndicatorImpl#getFormatName(java.lang.Object)}.
     * the domain parameter isn't set in this case.
     */
    public void testGetGroupLabel_2() {
        init();
        int i = 1;
        String formatName = binFreqIndicator.getGroupLabel(i);
        assertEquals(formatName, "1");

        i = 2;
        formatName = binFreqIndicator.getGroupLabel(i);
        assertEquals(formatName, "2");

        formatName = binFreqIndicator.getGroupLabel(null);
        assertNull(formatName);

    }
}
