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
import org.talend.dataquality.indicators.schema.SchemaIndicator;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class SchemaSorter extends ViewerSorter {

    public static final int SCHEMA = 1;

    public static final int ROWS = 2;

    public static final int TABLES = 3;

    public static final int ROWS_TABLES = 4;

    public static final int VIEWS = 5;

    public static final int ROWS_VIEWS = 6;

    public static final int KEYS = 7;

    public static final int INDEXES = 8;

    private int sortType;

    public SchemaSorter(int sortType) {
        this.sortType = sortType;
    }

    public int compare(Viewer viewer, Object o1, Object o2) {
        SchemaIndicator indicator1 = (SchemaIndicator) ((OverviewIndUIElement) o1).getOverviewIndicator();
        SchemaIndicator indicator2 = (SchemaIndicator) ((OverviewIndUIElement) o2).getOverviewIndicator();
        String value1;
        String value2;
        long number1;
        long number2;
        switch (sortType) {
        case SCHEMA:
            value1 = indicator1.getAnalyzedElement().getName();
            value2 = indicator2.getAnalyzedElement().getName();
            return value1.compareTo(value2);

        case -SCHEMA:
            value1 = indicator1.getAnalyzedElement().getName();
            value2 = indicator2.getAnalyzedElement().getName();
            return value2.compareTo(value1);

        case ROWS:
            number1 = indicator1.getTableRowCount();
            number2 = indicator2.getTableRowCount();
            return new Long(number1).compareTo(new Long(number2));

        case -ROWS:
            number1 = indicator1.getTableRowCount();
            number2 = indicator2.getTableRowCount();
            return new Long(number2).compareTo(new Long(number1));

        case TABLES:
            number1 = indicator1.getTableCount();
            number2 = indicator2.getTableCount();
            return new Long(number1).compareTo(new Long(number2));

        case -TABLES:
            number1 = indicator1.getTableCount();
            number2 = indicator2.getTableCount();
            return new Long(number2).compareTo(new Long(number1));

        case ROWS_TABLES:
            number1 = indicator1.getTableCount() == 0 ? 0 : indicator1.getTableRowCount() / indicator1.getTableCount();
            number2 = indicator2.getTableCount() == 0 ? 0 : indicator2.getTableRowCount() / indicator2.getTableCount();
            return new Long(number1).compareTo(new Long(number2));

        case -ROWS_TABLES:
            number1 = indicator1.getTableCount() == 0 ? 0 : indicator1.getTableRowCount() / indicator1.getTableCount();
            number2 = indicator2.getTableCount() == 0 ? 0 : indicator2.getTableRowCount() / indicator2.getTableCount();
            return new Long(number2).compareTo(new Long(number1));

        case VIEWS:
            number1 = indicator1.getViewCount();
            number2 = indicator2.getViewCount();
            return new Long(number1).compareTo(new Long(number2));

        case -VIEWS:
            number1 = indicator1.getViewCount();
            number2 = indicator2.getViewCount();
            return new Long(number2).compareTo(new Long(number1));

        case ROWS_VIEWS:
            number1 = indicator1.getViewCount() == 0 ? 0 : indicator1.getTableRowCount() / indicator1.getViewCount();
            number2 = indicator2.getViewCount() == 0 ? 0 : indicator2.getTableRowCount() / indicator2.getViewCount();
            return new Long(number1).compareTo(new Long(number2));

        case -ROWS_VIEWS:
            number1 = indicator1.getViewCount() == 0 ? 0 : indicator1.getTableRowCount() / indicator1.getViewCount();
            number2 = indicator2.getViewCount() == 0 ? 0 : indicator2.getTableRowCount() / indicator2.getViewCount();
            return new Long(number2).compareTo(new Long(number1));

        case KEYS:
            number1 = indicator1.getKeyCount();
            number2 = indicator2.getKeyCount();
            return new Long(number1).compareTo(new Long(number2));

        case -KEYS:
            number1 = indicator1.getKeyCount();
            number2 = indicator2.getKeyCount();
            return new Long(number2).compareTo(new Long(number1));

        case INDEXES:
            number1 = indicator1.getIndexCount();
            number2 = indicator2.getIndexCount();
            return new Long(number1).compareTo(new Long(number2));

        case -INDEXES:
            number1 = indicator1.getIndexCount();
            number2 = indicator2.getIndexCount();
            return new Long(number2).compareTo(new Long(number1));
        default:
            return 0;

        }
    }
}
