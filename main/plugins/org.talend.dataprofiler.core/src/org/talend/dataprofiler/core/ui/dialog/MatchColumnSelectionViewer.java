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
package org.talend.dataprofiler.core.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.dq.nodes.ColumnSetRepNode;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchColumnSelectionViewer extends ColumnSelectionViewer {

    /**
     * DOC yyin MatchColumnSelectionViewer constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MatchColumnSelectionViewer(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * Added TDQ-8718 20140423, yyin when the selection event is a check event: if the selected item is a column set,
     * continue, else make it unchecked. for other event(not check) , calling parent's method directly.
     */
    @Override
    protected void handleSelect(SelectionEvent event) {
        if (event.detail == SWT.CHECK) {
            TreeItem item = (TreeItem) event.item;
            if (isColumnSet(item.getData())) {
                super.handleSelect(event);
            } else {
                item.setChecked(false);
            }
        } else {
            super.handleSelect(event);
        }
    }

    /**
     * if the element is a column set (table, view, dftable)
     * 
     * @param element
     * @return
     */
    private boolean isColumnSet(Object element) {
        return element instanceof ColumnSetRepNode;
    }

}
