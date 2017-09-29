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
package org.talend.dataquality.record.linkage.ui.composite.table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

/**
 * DOC yyin class global comment. Detailled comment
 */
public abstract class SortComparator implements ISortComparator {

    public static ISortComparator getSortComparator(int javaDataType) throws ParseException {
        switch (javaDataType) {
        case Types.DATE:
        case Types.TIME:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return java.sql.Date.valueOf(value1.toString()).compareTo(java.sql.Date.valueOf(value2.toString()));
                }

            };
        case Types.TIMESTAMP:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return Timestamp.valueOf(value1.toString()).compareTo(Timestamp.valueOf(value2.toString()));
                }

            };
        case Types.DOUBLE:
        case Types.REAL:
        case Types.FLOAT:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return Double.valueOf(value1.toString()).compareTo(Double.valueOf(value2.toString()));
                }

            };

        case Types.INTEGER:
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.NUMERIC:

            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return Integer.valueOf(value1.toString()).compareTo(Integer.valueOf(value2.toString()));
                }

            };
        case Types.BIGINT:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return Long.valueOf(value1.toString()).compareTo(Long.valueOf(value2.toString()));
                }

            };
        case Types.DECIMAL:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    if (value1 instanceof BigDecimal) {
                        return ((BigDecimal) value1).compareTo((BigDecimal) value2);
                    }
                    BigDecimal b1 = new BigDecimal(value1.toString());
                    BigDecimal b2 = new BigDecimal(value2.toString());
                    return b1.compareTo(b2);
                }

            };
        case Types.BOOLEAN:
        case Types.BIT:
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return Boolean.valueOf(value1.toString()).compareTo(Boolean.valueOf(value2.toString()));
                }

            };
        default:// String
            return new SortComparator() {

                @Override
                public int compareNotNull(Object value1, Object value2) throws ParseException {
                    return value1.toString().compareTo(value2.toString());
                }

            };

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.table.ISortComparator#compareTwo(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public int compareTwo(Object value1, Object value2) {
        if (value1 == null || StringUtils.EMPTY.equals(value1)) {
            return -1;
        } else if (value2 == null || StringUtils.EMPTY.equals(value2)) {
            return 1;
        }
        try {
            return compareNotNull(value1, value2);
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * compare the two object
     * 
     * @param value1
     * @param value2
     * @return
     * @throws ParseException
     */
    protected abstract int compareNotNull(Object value1, Object value2) throws ParseException;
}
