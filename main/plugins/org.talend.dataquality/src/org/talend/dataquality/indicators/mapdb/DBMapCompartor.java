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

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * created by talend on Aug 5, 2014 Detailled comment
 * 
 */
@SuppressWarnings("rawtypes")
public class DBMapCompartor implements Comparator<Object>, Serializable {

    private static final long serialVersionUID = -3201084133371388584L;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null && o2 != null) {
            return -1;
        }

        if (o1 != null && o2 == null) {
            return 1;
        }

        if (o1.equals(o2)) {
            return 0;
        }

        if (TupleEmpty.class.isInstance(o1)) {
            return 1;
        }

        if (TupleEmpty.class.isInstance(o2)) {
            return -1;
        }

        if (Comparable.class.isInstance(o1)) {
            return ((Comparable) o1).compareTo(o2);
        }

        if (List.class.isInstance(o1)) {
            return listCompare(((List) o1).toArray(), ((List) o2).toArray());
        }

        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return listCompare((Object[]) o1, (Object[]) o2);
        }
        return -1;
    }

    /**
     * DOC talend Comment method "listCompare".
     * 
     * @param array
     * @param array2
     * @return
     */
    private int listCompare(Object[] array1, Object[] array2) {
        int i = -1;
        for (int index = 0; index < array1.length; index++) {
            i = compare(array1[index], array2[index]);
            if (i != 0) {
                return i;
            }
        }
        return i;
    }

}
