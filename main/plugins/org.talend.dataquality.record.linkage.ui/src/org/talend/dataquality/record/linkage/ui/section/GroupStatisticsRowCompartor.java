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
package org.talend.dataquality.record.linkage.ui.section;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.GroupStatisticsRow;

/**
 * created by msjian on 2016年9月14日
 * TDQ-12441 : Sorting data on results of match analysis
 *
 */
public class GroupStatisticsRowCompartor extends ViewerSorter {

    private int propertyIndex;

    private static final int ASCENDING = 0;

    private static final int DESCENDING = 1;

    private int direction = ASCENDING;

    /**
     * GroupStatisticsRowCompartor constructor.
     */
    public GroupStatisticsRowCompartor() {
        this.propertyIndex = 0;
        direction = ASCENDING;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // Same column as last sort; switch the direction
            direction = 1 - direction;
        } else {
            // New column; do an desc sort
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        GroupStatisticsRow row1 = (GroupStatisticsRow) e1;
        GroupStatisticsRow row2 = (GroupStatisticsRow) e2;
        int rc;

        switch (propertyIndex) {
        case 0:
            rc = row1.getGroupSize().compareTo(row2.getGroupSize());
            break;
        case 1:
            rc = row1.getGroupCount().compareTo(row2.getGroupCount());
            break;
        case 2:
            rc = row1.getRecordCount().compareTo(row2.getRecordCount());
            break;
        case 3:
            Double v1 = Double.valueOf(row1.getRecordPercentage().substring(0, row1.getRecordPercentage().length() - 1));
            Double v2 = Double.valueOf(row2.getRecordPercentage().substring(0, row2.getRecordPercentage().length() - 1));
            rc = v1.compareTo(v2);
            break;
        default:
            rc = 0;
        }
        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
