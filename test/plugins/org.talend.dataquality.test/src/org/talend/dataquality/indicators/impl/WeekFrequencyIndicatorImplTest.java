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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

/**
 * created by talend2 on 2014-10-30 Detailled comment
 * 
 */
public class WeekFrequencyIndicatorImplTest {

    private WeekFrequencyIndicatorImpl weekFreqIndicator = null;

    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$

    private void init() {
        if (weekFreqIndicator == null) {
            weekFreqIndicator = new WeekFrequencyIndicatorImpl();
            weekFreqIndicator.setUsedMapDBMode(false);
            weekFreqIndicator.reset();
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl#handle(java.lang.Object)}.
     * 
     * @throws ParseException
     */
    @Test
    public void testHandleObject() throws ParseException {
        init();
        Date date = dFormat.parse("2012-01-04"); //$NON-NLS-1$
        weekFreqIndicator.handle(date);
        Long value = weekFreqIndicator.getValueToFreq().get("20120101"); //$NON-NLS-1$
        assertEquals(value.longValue(), 1l);

        date = dFormat.parse("2012-01-07"); //$NON-NLS-1$
        weekFreqIndicator.handle(date);
        value = weekFreqIndicator.getValueToFreq().get("20120101"); //$NON-NLS-1$
        assertEquals(value.longValue(), 2l);

    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.impl.WeekFrequencyIndicatorImpl#getFormatName(java.lang.Object)}.
     * 
     * @throws ParseException
     */
    @Test
    public void testGetFormatName() throws ParseException {
        init();
        Date date = dFormat.parse("2012-01-04"); //$NON-NLS-1$
        String formatName = weekFreqIndicator.getFormatName(date);
        assertEquals(formatName, "20120101"); //$NON-NLS-1$

        date = dFormat.parse("2011-01-04"); //$NON-NLS-1$
        formatName = weekFreqIndicator.getFormatName(date);
        assertEquals(formatName, "20110102"); //$NON-NLS-1$

        date = dFormat.parse("1987-11-13"); //$NON-NLS-1$
        formatName = weekFreqIndicator.getFormatName(date);
        assertEquals(formatName, "19871146"); //$NON-NLS-1$

        date = dFormat.parse("1988-12-4"); //$NON-NLS-1$
        formatName = weekFreqIndicator.getFormatName(date);
        assertEquals(formatName, "19881250"); //$NON-NLS-1$

        date = dFormat.parse("2011-8-17"); //$NON-NLS-1$
        formatName = weekFreqIndicator.getFormatName(date);
        assertEquals(formatName, "20110834"); //$NON-NLS-1$

    }

}
