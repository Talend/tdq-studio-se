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

import java.sql.Time;
import java.text.SimpleDateFormat;

import org.talend.dataquality.PluginConstant;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class TalendFormatTime extends Time {

    private Time time = null;

    // pattern for Date and Timestamp.
    private String pattern = "HH:mm:ss.SSS";//$NON-NLS-1$

    /**
     * 
     */
    private static final long serialVersionUID = 2840349445774526174L;

    public TalendFormatTime(Time time) {
        super(time != null ? time.getTime() : 0);
        this.time = time;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Timestamp#toString()
     */
    @Override
    public String toString() {
        if (time == null) {
            return PluginConstant.EMPTY_STRING;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(time);
    }

}
