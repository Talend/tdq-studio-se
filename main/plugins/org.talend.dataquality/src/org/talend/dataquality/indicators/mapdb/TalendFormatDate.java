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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.talend.dataquality.PluginConstant;

/**
 * ovririd "toString()" to format Date and Timestamp to specify pattern.
 */
public class TalendFormatDate extends Date {

    /**
     * 
     */
    private static final long serialVersionUID = -510723001255244666L;

    // pattern for Date and Timestamp.
    private String pattern = "yyyy-MM-dd HH:mm:ss.SSS";//$NON-NLS-1$

    private Date dateObj = null;

    public TalendFormatDate(Date obj) {
        super(obj != null ? obj.getTime() : 0);
        this.dateObj = obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Date#toString()
     */
    @Override
    public String toString() {
        if (dateObj == null || pattern == null) {
            return PluginConstant.EMPTY_STRING;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(dateObj);
    }

}
