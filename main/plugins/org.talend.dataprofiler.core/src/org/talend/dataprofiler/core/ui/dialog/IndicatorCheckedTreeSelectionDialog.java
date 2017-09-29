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

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class IndicatorCheckedTreeSelectionDialog extends CheckedTreeSelectionDialog {

    public IndicatorCheckedTreeSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
    }

    public void setCheckedElements(Object[] elements) {
        CheckboxTreeViewer treeViewer = this.getTreeViewer();
        if (treeViewer != null) {
            treeViewer.setCheckedElements(elements);
        }
    }
}
