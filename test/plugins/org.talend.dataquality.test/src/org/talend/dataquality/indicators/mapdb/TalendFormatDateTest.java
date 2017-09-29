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

import java.util.Date;

import org.junit.Test;

/**
 * DOC talend2 class global comment. Detailled comment
 */
public class TalendFormatDateTest {

    /**
     * Test method for {@link org.talend.dataquality.indicators.mapdb.TalendFormatDate#toString()}.
     */
    @Test
    public void testToString() {
        Date date = new Date(115, 11, 10);
        TalendFormatDate tfd = new TalendFormatDate(date);
        assertEquals(tfd.toString(), "2015-12-10 00:00:00.000"); //$NON-NLS-1$
    }

    @Test
    public void testToString_2() {
        Date date = new Date(115, 11, 10, 10, 5, 18);
        TalendFormatDate tfd = new TalendFormatDate(date);
        assertEquals(tfd.toString(), "2015-12-10 10:05:18.000"); //$NON-NLS-1$
    }

    @Test
    public void testToString_null() {
        TalendFormatDate tfd = new TalendFormatDate(null);
        assertEquals(tfd.toString(), ""); //$NON-NLS-1$
    }

}
