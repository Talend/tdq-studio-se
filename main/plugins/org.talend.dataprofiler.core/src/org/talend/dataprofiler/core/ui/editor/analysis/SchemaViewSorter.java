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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.dataquality.indicators.schema.ViewIndicator;

/**
 * 
 *The sorter extends ViewerSorter for column sorting.
 */
public class SchemaViewSorter extends ViewerSorter {

    public static final int VIEW = 1;

    public static final int ROWS = 2;

    private int sortType;

    public SchemaViewSorter(int sortType) {
        this.sortType = sortType;
    }

    public int compare(Viewer viewer, Object o1, Object o2) {
        ViewIndicator viewIndicator1 = (ViewIndicator) o1;
        ViewIndicator viewIndicator2 = (ViewIndicator) o2;

        String tableName1;
        String tableName2;
        long rowCount1;
        long rowCount2;
        switch (sortType) {
        case VIEW:
            tableName1 = viewIndicator1.getTableName();
            tableName2 = viewIndicator1.getTableName();
            return tableName1.compareTo(tableName2);
        case -VIEW:
            tableName1 = viewIndicator1.getTableName();
            tableName2 = viewIndicator2.getTableName();
            return tableName2.compareTo(tableName1);
        case ROWS:
            rowCount1 = viewIndicator1.getRowCount();
            rowCount2 = viewIndicator2.getRowCount();
            return new Long(rowCount1).compareTo(new Long(rowCount2));
        case -ROWS:
            rowCount1 = viewIndicator1.getRowCount();
            rowCount2 = viewIndicator2.getRowCount();
            return new Long(rowCount2).compareTo(new Long(rowCount1));
        default:
            return 0;
        }
    }
}
