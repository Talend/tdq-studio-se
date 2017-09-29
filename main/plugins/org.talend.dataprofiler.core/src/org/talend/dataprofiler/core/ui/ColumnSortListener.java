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
package org.talend.dataprofiler.core.ui;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.talend.dataprofiler.core.ImageLib;

/**
 * The class extends SelectionAdapter can be used on column sorting for StructuredViewer.
 */
public class ColumnSortListener extends SelectionAdapter {

    Image ascImage = ImageLib.getImage(ImageLib.ASC_SORT); //$NON-NLS-1$

    Image descImage = ImageLib.getImage(ImageLib.DESC_SORT); //$NON-NLS-1$

    StructuredViewer viewer;

    ViewerSorter[][] sorters = null;

    boolean flag = false;

    int i = 0;

    Item[] columns;

    public ColumnSortListener(Item[] t, int columnIndex, StructuredViewer stViewer, ViewerSorter[][] viewerSorter) {
        i = columnIndex;
        columns = t;
        this.viewer = stViewer;
        this.sorters = viewerSorter;
    }

    public void setSorter(ViewerSorter[][] viewerSorter) {
        this.sorters = viewerSorter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e) {
        flag = !flag;
        for (int j = 0; j < columns.length; j++) {
            if (j == i) {
                if (flag) {
                    columns[j].setImage(ascImage);
                } else {
                    columns[j].setImage(descImage);
                }
            } else {
                columns[j].setImage(null);
            }
        }
        viewer.setSorter(flag ? sorters[i][0] : sorters[i][1]);
    }

    public void dispose() {
        ascImage.dispose();
        descImage.dispose();
    }

}
