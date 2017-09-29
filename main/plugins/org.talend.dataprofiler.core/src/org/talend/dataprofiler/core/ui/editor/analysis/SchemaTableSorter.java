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
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.indicators.schema.TableIndicator;

/**
 * 
 * The sorter extends ViewerSorter for column sorting.
 */
public class SchemaTableSorter extends ViewerSorter {

    public static final int TABLE = 1;

    public static final int ROWS = 2;

    public static final int KEYS = 3;

    public static final int INDEXES = 4;

    private int sortType;

    public SchemaTableSorter(int sortType) {
        this.sortType = sortType;
    }

    public int compare(Viewer viewer, Object o1, Object o2) {
        TableIndicator tableIndicator1 = (TableIndicator) ((OverviewIndUIElement) o1).getOverviewIndicator();
        TableIndicator tableIndicator2 = (TableIndicator) ((OverviewIndUIElement) o2).getOverviewIndicator();
        String value1;
        String value2;
        long number1;
        long number2;
        switch (sortType) {
        case TABLE:
            value1 = tableIndicator1.getTableName();
            value2 = tableIndicator2.getTableName();
            return value1.compareTo(value2);

        case -TABLE:
            value1 = tableIndicator1.getTableName();
            value2 = tableIndicator2.getTableName();
            return value2.compareTo(value1);
        case ROWS:
            number1 = tableIndicator1.getRowCount();
            number2 = tableIndicator2.getRowCount();
            return new Long(number1).compareTo(new Long(number2));

        case -ROWS:
            number1 = tableIndicator1.getRowCount();
            number2 = tableIndicator2.getRowCount();
            return new Long(number2).compareTo(new Long(number1));

        case KEYS:
            number1 = tableIndicator1.getKeyCount();
            number2 = tableIndicator2.getKeyCount();
            return new Long(number1).compareTo(new Long(number2));

        case -KEYS:
            number1 = tableIndicator1.getKeyCount();
            number2 = tableIndicator2.getKeyCount();
            return new Long(number2).compareTo(new Long(number1));

        case INDEXES:
            number1 = tableIndicator1.getIndexCount();
            number2 = tableIndicator2.getIndexCount();
            return new Long(number1).compareTo(new Long(number2));

        case -INDEXES:
            number1 = tableIndicator1.getIndexCount();
            number2 = tableIndicator2.getIndexCount();
            return new Long(number2).compareTo(new Long(number1));
        default:
            return 0;

        }
    }
}
