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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * DOC hcheng class global comment. Detailled comment
 */
public class CorrelationDataSorter extends ViewerSorter {

    private int arrayIndex;

    public CorrelationDataSorter(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public int compare(Viewer viewer, Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            Object[] objs1 = (Object[]) o1;
            Object obj1 = objs1[Math.abs(arrayIndex) - 1];
            Object[] objs2 = (Object[]) o2;
            Object obj2 = objs2[Math.abs(arrayIndex) - 1];

            if (obj1 == null && obj2 != null) {
                if (arrayIndex > 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (obj1 != null && obj2 == null) {
                if (arrayIndex > 0) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (obj1 == null && obj2 == null) {
                return 0;
            }

            if (obj1 instanceof String) {
                if (arrayIndex > 0) {
                    return ((String) obj1).compareTo((String) obj2);
                } else {
                    return ((String) obj2).compareTo((String) obj1);
                }
            } else if (obj1 instanceof Long) {
                if (arrayIndex > 0) {
                    return ((Long) obj1).compareTo((Long) obj2);
                } else {
                    return ((Long) obj2).compareTo((Long) obj1);
                }
            } else if (obj1 instanceof Date) {
                if (arrayIndex > 0) {
                    return ((Date) obj1).compareTo((Date) obj2);
                } else {
                    return ((Date) obj2).compareTo((Date) obj1);
                }
            } else if (obj1 instanceof Comparable) {
                // MOD yyi 2010-08-17 for 13868
                if (arrayIndex > 0) {
                    return ((Comparable) obj1).compareTo((Comparable) obj2);
                } else {
                    return ((Comparable) obj2).compareTo((Comparable) obj1);
                }
            } else {
                if (arrayIndex > 0) {
                    return obj1.toString().compareTo(obj2.toString());
                } else {
                    return obj2.toString().compareTo(obj1.toString());
                }
            }
        }
        return 0;
    }
}
