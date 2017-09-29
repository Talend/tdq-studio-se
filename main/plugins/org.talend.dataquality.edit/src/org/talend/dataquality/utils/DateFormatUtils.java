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
package org.talend.dataquality.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Format the Date, and get the date string as special format.
 * @ADDED
 */
public final class DateFormatUtils {

    public static final String[] DATEFORMATS_SAVE = new String[] { "KK:mm a", "MMM dd", "MM/dd/yy" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static Map<String, DateFormat> dateFormatMap = null;

    private DateFormatUtils() {
    }

    private static void checkFormats() {
        if (dateFormatMap == null) {
            dateFormatMap = new HashMap<String, DateFormat>();
            for (String formatStr : DATEFORMATS_SAVE) {
                dateFormatMap.put(formatStr, new SimpleDateFormat(formatStr));
            }
        }
    }

    /**
     * The date should be displayed as follows : â€œhour: minuteâ€? format for the date of the current day; â€œmonth, dateâ€?
     * for the dates of the current year; full numeric format for older dates). For example: "8:14 am","July
     * 4","11/22/07".
     */
    public static String getSimpleDateString(Date date) {
        checkFormats();
        String returnStr = null;
        GregorianCalendar currentCal = new GregorianCalendar();
        currentCal.setTime(new Date());
        GregorianCalendar paremCal = new GregorianCalendar();
        paremCal.setTime(date);
        if (currentCal.get(GregorianCalendar.YEAR) == paremCal.get(GregorianCalendar.YEAR)) {
            if (currentCal.get(GregorianCalendar.DAY_OF_YEAR) == paremCal.get(GregorianCalendar.DAY_OF_YEAR)) {
                returnStr = dateFormatMap.get(DATEFORMATS_SAVE[0]).format(date).toString();
            } else {
                returnStr = dateFormatMap.get(DATEFORMATS_SAVE[1]).format(date).toString();
            }

        } else {
            returnStr = dateFormatMap.get(DATEFORMATS_SAVE[2]).format(date).toString();
        }
        return returnStr;

    }

    /**
     * DOC rli Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

}
