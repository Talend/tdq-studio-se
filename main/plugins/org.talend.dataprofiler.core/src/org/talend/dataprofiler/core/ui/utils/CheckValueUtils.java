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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * DOC zqin class global comment. Detailled comment
 */
public final class CheckValueUtils {

    protected static Logger log = Logger.getLogger(CheckValueUtils.class);

    private CheckValueUtils() {
    }

    public static boolean getCheckValue(String regex, String inputString) {

        if (isEmpty(inputString)) {
            return false;
        }

        return Pattern.matches(regex, inputString);
    }

    public static boolean isStringValue(String inputString) {

        String regex = "^([a-z]|[A-Z])\\w*"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    public static boolean isNumberValue(String inputString) {

        String regex = "\\d+"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    public static boolean isRealNumberValue(String inputString) {
        String regex = "-?\\d+(\\.\\d+)?"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    public static boolean isNumberValue(String... strs) {
        for (String str : strs) {
            if (!isNumberValue(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRealNumberValue(String... strs) {
        for (String str : strs) {
            if (!isRealNumberValue(str)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNumberOfShownValue(String inputString) {

        String regex = "^[1-9]\\d*"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    public static boolean isNumberWithNegativeValue(String inputString) {

        String regex = "-?\\d+"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    public static boolean isPatternValue(String inputString) {

        String regex = "'.*'"; //$NON-NLS-1$
        return getCheckValue(regex, inputString);
    }

    /**
     * DOC Zqin Comment method "isAoverB".
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean isAoverB(String a, String b) {
        if (!isEmpty(a, b) && (isNumberValue(a, b) || isRealNumberValue(a, b))) {
            Double da = new Double(a);
            Double db = new Double(b);
            return da > db;
        }

        if (!isEmpty(a, b) && isDateValue(a) && isDateValue(b)) {
            try {

                String[] patterns = new String[2];
                patterns[0] = "yyyy-MM-dd"; //$NON-NLS-1$
                patterns[1] = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$

                Date ad = DateUtils.parseDate(a, patterns);
                Date bd = DateUtils.parseDate(b, patterns);

                return ad.after(bd);
            } catch (Exception e) {
                log.error(e, e);
                return false;

            }

        }
        return false;
    }

    /**
     * DOC Zqin Comment method "isEmpty".
     * 
     * @param strs
     * @return
     */
    public static boolean isEmpty(String... strs) {
        if (strs == null) {
            return false;
        }

        for (String str : strs) {
            if (str == null) {
                return true;
            } else if ("".equals(str.trim())) { //$NON-NLS-1$
                return true;
            }
        }

        return false;
    }

    /**
     * DOC Zqin Comment method "isOutRange".
     * 
     * @param min the min value of this range, and min <= max.
     * @param max the max value of this range, and max >= min
     * @param strs
     * @return
     */
    public static boolean isOutRange(double min, double max, String... strs) {

        for (String str : strs) {
            if (isNumberValue(str) || isRealNumberValue(str)) {
                Double db = new Double(str);
                if (db > max || db < min) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * DOC zqin Comment method "isDateValue".
     * 
     * @param inputString
     * @return
     */
    public static boolean isDateValue(String inputString) {

        if (!isEmpty(inputString)) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
                df.setLenient(false);
                df.parse(inputString);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

}
