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
package org.talend.dataquality.indicators.mapdb;

import static org.junit.Assert.assertEquals;

import java.sql.Time;

import org.junit.Test;

/**
 * DOC talend2 class global comment. Detailled comment
 */
public class TalendFormatTimeTest {

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.TalendFormatTime#toString()}.
     */
    @Test
    public void testToString() {
        Time time = new Time(12, 30, 8);
        TalendFormatTime tft = new TalendFormatTime(time);
        assertEquals(tft.toString(), "12:30:08.000"); //$NON-NLS-1$
    }

    @Test
    public void testToString_null() {
        TalendFormatTime tft = new TalendFormatTime(null);
        assertEquals(tft.toString(), ""); //$NON-NLS-1$
    }

}
